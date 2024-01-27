package com.example.dateguessr.model.images_repository;

import android.text.TextUtils;
import android.util.Pair;

import com.example.dateguessr.Util;
import com.example.dateguessr.model.wiki_api.WikiApi;
import com.example.dateguessr.model.wiki_api.imageinfo_pojo.DateTimeOriginal;
import com.example.dateguessr.model.wiki_api.imageinfo_pojo.ImageDescription;
import com.example.dateguessr.model.wiki_api.imageinfo_pojo.ImageInfoPage;
import com.example.dateguessr.model.wiki_api.randompage_pojo.RandomPageImage;
import com.example.dateguessr.model.wiki_api.imageinfo_pojo.Extmetadata;
import com.example.dateguessr.model.wiki_api.randompage_pojo.RandomPage;

import org.jsoup.Jsoup;

import java.util.Collection;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ImagesRepository {
    private final WikiApi apiService;
    private final ImageDataDao imageDataDao;

    public ImagesRepository(WikiApi apiService, ImageDataDao imageDataDao) {
        this.apiService = apiService;
        this.imageDataDao = imageDataDao;
    }

    public Completable cleanUpData() {
        return imageDataDao.deleteAllImageData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Completable fetchNewData(int limit) {
        String format = "json";
        String action = "query";
        String generator = "random";
        String page_prop = "pageimages";
        String image_prop = "imageinfo";
        String piprop = "original";
        String iiprop = "extmetadata";
        int grnnamespace = 0;

        return apiService.getRandomPage(format, action, generator, grnnamespace, page_prop, piprop, limit)
                .flatMap(randomPageResponse -> {
                    Collection<RandomPage> itemList = randomPageResponse.getQuery().getPages().values();
                    return Observable.fromIterable(itemList)
                            .filter(page -> page.getImage() != null)
                            .filter(page -> Util.isExtensionValid(page.getImage().getSource()))
                            .map(this::insertEntryAndGetFilename)
                            .toList();
                })
                .flatMap(fileNames -> {
                    String concatenatedFileNames = TextUtils.join("|", fileNames);
                    return apiService.getImageInfo(format, action, concatenatedFileNames, image_prop, iiprop)
                            .flatMap(imageInfoResponse -> {
                                Collection<ImageInfoPage> imageInfoList = imageInfoResponse.getQuery().getPages().values();
                                return Observable.fromIterable(imageInfoList)
                                        .zipWith(fileNames, Pair::create)
                                        .doOnNext(this::insertImageInfo)
                                        .toList()
                                        .flatMap(imageDataPairs -> imageDataDao.getImagesCount());
                            });
                })
                .repeat()
                .takeWhile(count -> count <= limit)
                .ignoreElements()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<List<ImageData>> getAllImageData() {
        return imageDataDao.getAllImageData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    private String insertEntryAndGetFilename(RandomPage page) {
        RandomPageImage img = page.getImage();
        String fileName = Util.getFileNameFromUrl(img.getSource());
        ImageData imageData = new ImageData();
        imageData.setPageTitle(page.getTitle());
        imageData.setImageUrl(img.getSource());
        imageData.setFileName(fileName);
        imageDataDao.upsert(imageData);
        return fileName;
    }

    private void insertImageInfo(Pair<ImageInfoPage, String> pair) {
        Extmetadata imgData = pair.first.getImageinfo().get(0).getExtmetadata();
        DateTimeOriginal dateTimeOriginal = imgData.getDateTimeOriginal();
        ImageDescription imageDescription = imgData.getImageDescription();
        String date = Util.extractYear(dateTimeOriginal);

        if (date != null && imageDescription != null) {
            String desc = Jsoup.parse(imageDescription.getValue()).text();
            ImageData imageData = imageDataDao.getImageDataByFileName(pair.second);
            imageData.setDate(date);
            imageData.setDescription(desc);
            imageDataDao.upsert(imageData);
        } else {
            imageDataDao.deleteImageDataByFileName(pair.second);
        }
    }
}
