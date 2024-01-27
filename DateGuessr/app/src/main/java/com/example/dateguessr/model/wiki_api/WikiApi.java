package com.example.dateguessr.model.wiki_api;

import com.example.dateguessr.model.wiki_api.imageinfo_pojo.ImageInfoResponse;
import com.example.dateguessr.model.wiki_api.randompage_pojo.RandomPageResponse;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WikiApi {

    @GET("api.php")
    Single<RandomPageResponse> getRandomPage(
            @Query("format") String format,
            @Query("action") String action,
            @Query("generator") String generator,
            @Query("grnnamespace") int grnnamespace,
            @Query("prop") String prop,
            @Query("piprop") String piprop,
            @Query("grnlimit") int grnlimit
    );

    @GET("api.php")
    Single<ImageInfoResponse> getImageInfo(
            @Query("format") String format,
            @Query("action") String action,
            @Query("titles") String titles,
            @Query("prop") String prop,
            @Query("iiprop") String iiprop
    );
}
