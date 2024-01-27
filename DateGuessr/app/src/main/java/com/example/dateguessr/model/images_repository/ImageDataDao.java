package com.example.dateguessr.model.images_repository;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ImageDataDao {

    @Upsert
    void upsert(ImageData imageData);

    @Query("SELECT COUNT(*) FROM image_data")
    Flowable<Integer> getImagesCount();

    @Query("SELECT * FROM image_data")
    Flowable<List<ImageData>> getAllImageDataList();

    @Query("SELECT * FROM image_data WHERE fileName = :fileName")
    ImageData getImageDataByFileName(String fileName);

    @Query("SELECT * FROM image_data WHERE id = :id")
    ImageData getImageDataById(int id);

    @Query("DELETE FROM image_data")
    void deleteAllImageData();

    @Query("DELETE FROM image_data WHERE fileName = :fileName")
    void deleteImageDataByFileName(String fileName);
}