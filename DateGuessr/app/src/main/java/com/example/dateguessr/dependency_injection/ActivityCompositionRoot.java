package com.example.dateguessr.dependency_injection;

import static com.example.dateguessr.model.images_repository.ImagesDatabase.getDatabaseInstance;

import android.content.Context;

import com.example.dateguessr.model.images_repository.ImageDataDao;
import com.example.dateguessr.model.images_repository.ImagesDatabase;
import com.example.dateguessr.model.images_repository.ImagesRepository;

public class ActivityCompositionRoot {

    private final Context context;

    private final CompositionRoot compositionRoot;

    private static ImagesRepository repo;

    public ActivityCompositionRoot(Context context, CompositionRoot compositionRoot) {
        this.context = context;
        this.compositionRoot = compositionRoot;
    }

    public ImagesRepository getImagesRepository() {
        if (repo == null) {
            ImagesDatabase imagesDatabase = getDatabaseInstance(context);
            ImageDataDao imageDataDao = imagesDatabase.imageDataDao();
            repo = new ImagesRepository(compositionRoot.getWikiApi(), imageDataDao);
        }
        return repo;
    }
}
