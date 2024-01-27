package com.example.dateguessr.model.wiki_api.imageinfo_pojo;

import com.google.gson.annotations.SerializedName;

public class Extmetadata{

	@SerializedName("DateTimeOriginal")
	private DateTimeOriginal dateTimeOriginal;

	@SerializedName("ImageDescription")
	private ImageDescription imageDescription;

	public DateTimeOriginal getDateTimeOriginal(){
		return dateTimeOriginal;
	}

	public ImageDescription getImageDescription(){
		return imageDescription;
	}
}