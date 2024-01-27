package com.example.dateguessr.dependency_injection;

import com.example.dateguessr.model.wiki_api.WikiApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CompositionRoot {

    private static final String BASE_URL_WIKI = "https://en.wikipedia.org/w/";
    private static WikiApi apiInstance;

    public WikiApi getWikiApi() {
        if (apiInstance == null) {
            apiInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL_WIKI)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(WikiApi.class);
        }
        return apiInstance;
    }
}
