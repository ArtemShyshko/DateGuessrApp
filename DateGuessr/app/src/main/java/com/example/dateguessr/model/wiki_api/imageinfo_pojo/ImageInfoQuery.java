package com.example.dateguessr.model.wiki_api.imageinfo_pojo;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class ImageInfoQuery {

	@SerializedName("pages")
	private Map<String, ImageInfoPage> pages;

	public Map<String, ImageInfoPage> getPages() { return pages;	}
}