package com.example.dateguessr.model.wiki_api.randompage_pojo;

import com.google.gson.annotations.SerializedName;

public class RandomPageImage {

    @SerializedName("source")
    private String source;

    @SerializedName("width")
    private int width;

    @SerializedName("height")
    private int height;

    public String getSource(){
        return source;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
}
