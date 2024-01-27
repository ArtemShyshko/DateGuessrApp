package com.example.dateguessr.model.wiki_api.randompage_pojo;

import com.google.gson.annotations.SerializedName;

public class RandomPageResponse{

	@SerializedName("query")
	private RandomPageQuery query;

	public RandomPageQuery getQuery(){
		return query;
	}
}