package com.example.dateguessr.model.wiki_api.imageinfo_pojo;

import com.google.gson.annotations.SerializedName;

public class ImageDescription{

	@SerializedName("source")
	private String source;

	@SerializedName("value")
	private String value;

	public String getSource(){
		return source;
	}

	public String getValue(){
		return value;
	}
}