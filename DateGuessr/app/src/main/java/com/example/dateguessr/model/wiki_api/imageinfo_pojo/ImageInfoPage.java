package com.example.dateguessr.model.wiki_api.imageinfo_pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ImageInfoPage {

	@SerializedName("imageinfo")
	private List<ImageinfoItem> imageinfo;

	@SerializedName("title")
	private String title;

	public List<ImageinfoItem> getImageinfo(){
		return imageinfo;
	}

	public String getTitle(){
		return title;
	}
}