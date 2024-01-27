package com.example.dateguessr.model.images_repository;

import androidx.annotation.NonNull;

import com.example.dateguessr.Util;
import com.example.dateguessr.model.wiki_api.WikiApi;
import com.example.dateguessr.model.wiki_api.imageinfo_pojo.DateTimeOriginal;
import com.example.dateguessr.model.wiki_api.imageinfo_pojo.ImageDescription;
import com.example.dateguessr.model.wiki_api.imageinfo_pojo.ImageInfoPage;
import com.example.dateguessr.model.wiki_api.randompage_pojo.RandomPageImage;
import com.example.dateguessr.model.wiki_api.randompage_pojo.RandomPageResponse;
import com.example.dateguessr.model.wiki_api.imageinfo_pojo.Extmetadata;
import com.example.dateguessr.model.wiki_api.imageinfo_pojo.ImageInfoResponse;
import com.example.dateguessr.model.wiki_api.randompage_pojo.RandomPage;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagesRepository {
    private final WikiApi apiService;
    private final ImageDataDao imageDataDao;

    public ImagesRepository(WikiApi apiService, ImageDataDao imageDataDao) {
        this.apiService = apiService;
        this.imageDataDao = imageDataDao;
    }

    public void fetchDataAndSaveToDatabase() {
        String format = "json";
        String action = "query";
        String generator = "random";
        String prop = "pageimages";
        String piprop = "original";
        int grnnamespace = 0;
        int grnlimit = 25;

        Call<RandomPageResponse> call = apiService.getRandomPage(
                format, action, generator, grnnamespace, prop, piprop, grnlimit
        );

        call.enqueue(new Callback<RandomPageResponse>() {
            @Override
            public void onResponse(
                    @NonNull Call<RandomPageResponse> call,
                    @NonNull Response<RandomPageResponse> response
            ) {
                if (response.isSuccessful()) {
                    RandomPageResponse rpResponse = response.body();
                    assert rpResponse != null;

                    List<RandomPage> list = new ArrayList<>(rpResponse.getQuery().getPages().values());
                    AtomicReference<String> imageNames = new AtomicReference<>("");

                    new Thread(() -> {
                        for (RandomPage p : list) {
                            RandomPageImage i = p.getImage();
                            if (i != null) {
                                String url = i.getSource();
                                String pageTitle = p.getTitle();
                                String fileName = Util.getFileNameFromUrl(url);

                                if (fileName != null
                                        && (fileName.toLowerCase().endsWith("jpg")
                                        || fileName.toLowerCase().endsWith("jpeg"))) {
                                    ImageData imageData = new ImageData();

                                    imageData.setFileName(fileName);
                                    imageData.setImageUrl(url);
                                    imageData.setPageTitle(pageTitle);
                                    imageDataDao.upsert(imageData);

                                    String tempName = imageNames.get();
                                    tempName = tempName.concat(fileName + "|");
                                    imageNames.set(tempName);
                                }
                            }
                        }
                        imageNames.set(imageNames.get().substring(0, imageNames.get().length() - 1));
                        getImageInfo(imageNames.get());
                    }).start();

                } else {
                    System.out.println("ERROR " + response.errorBody());
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<RandomPageResponse> call,
                    @NonNull Throwable t
            ) {
                System.out.println("ALARM " + t.getLocalizedMessage());
            }
        });
    }

    private void getImageInfo(String titles) {
        String format = "json";
        String action = "query";
        String prop = "imageinfo";
        String iiprop = "extmetadata";

        Call<ImageInfoResponse> call = apiService.getImageInfo(format, action, titles, prop, iiprop);

        call.enqueue(new Callback<ImageInfoResponse>() {
            @Override
            public void onResponse(
                    @NonNull Call<ImageInfoResponse> call,
                    @NonNull Response<ImageInfoResponse> response
            ) {
                if (response.isSuccessful()) {
                    ImageInfoResponse iiResponse = response.body();
                    assert iiResponse != null;

                    List<ImageInfoPage> list = new ArrayList<>(iiResponse.getQuery().getPages().values());

                    AtomicInteger i = new AtomicInteger(0);
                    String[] fileNames = titles.split("\\|");

                    new Thread(() -> {
                        for (ImageInfoPage p : list) {
                            Extmetadata imgData = p.getImageinfo().get(0).getExtmetadata();
                            DateTimeOriginal dateTimeOriginal = imgData.getDateTimeOriginal();
                            ImageDescription imageDescription = imgData.getImageDescription();
                            String date = Util.extractYear(dateTimeOriginal);

                            if (date != null && imageDescription != null) {
                                String desc = Jsoup.parse(imageDescription.getValue()).text();
                                ImageData imageData = imageDataDao.getImageDataByFileName(fileNames[i.getAndIncrement()]);
                                imageData.setDate(date);
                                imageData.setDescription(desc);
                                imageDataDao.upsert(imageData);
                            } else {
                                imageDataDao.deleteImageDataByFileName(fileNames[i.getAndIncrement()]);
                            }
                        }
                    }).start();

                } else {
                    System.out.println("ERROR " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ImageInfoResponse> call, @NonNull Throwable t) {
                System.out.println("ALARM " + t.getLocalizedMessage());
            }
        });
    }

    public Flowable<Integer> getImagesCount() {
        return imageDataDao.getImagesCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<ImageData>> getAllImageData() {
        return imageDataDao.getAllImageDataList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void cleanUpDb() {
        new Thread(imageDataDao::deleteAllImageData).start();
    }
}
