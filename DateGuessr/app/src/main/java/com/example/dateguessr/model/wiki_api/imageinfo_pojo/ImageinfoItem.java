package com.example.dateguessr.model.wiki_api.imageinfo_pojo;

import com.google.gson.annotations.SerializedName;

public class ImageinfoItem{

	@SerializedName("extmetadata")
	private Extmetadata extmetadata;

	public Extmetadata getExtmetadata(){
		return extmetadata;
	}
}