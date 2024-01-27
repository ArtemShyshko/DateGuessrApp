package com.example.dateguessr.model.wiki_api.randompage_pojo;

import com.google.gson.annotations.SerializedName;

public class RandomPage {

	@SerializedName("title")
	private String title;

	@SerializedName("original")
	private RandomPageImage image;

	public String getTitle(){
		return title;
	}

	public RandomPageImage getImage() { return image; }
}