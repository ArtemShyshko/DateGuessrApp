package com.example.dateguessr.model.images_repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ImageData.class}, version = 1, exportSchema = false)
public abstract class ImagesDatabase extends RoomDatabase {

    private static ImagesDatabase INSTANCE;

    public abstract ImageDataDao imageDataDao();

    private static final String DATABASE_NAME = "images_database";

    public static synchronized ImagesDatabase getDatabaseInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = createImagesDatabase(context);
        }
        return INSTANCE;
    }

    private static ImagesDatabase createImagesDatabase(Context context) {
        return Room.databaseBuilder(
                context.getApplicationContext(),
                ImagesDatabase.class,
                DATABASE_NAME
        ).build();
    }
}
