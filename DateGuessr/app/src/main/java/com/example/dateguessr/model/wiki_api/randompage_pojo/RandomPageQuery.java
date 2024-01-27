package com.example.dateguessr.model.wiki_api.randompage_pojo;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class RandomPageQuery {

	@SerializedName("pages")
	private Map<String, RandomPage> pages;

	public Map<String, RandomPage> getPages() { return pages;	}
}