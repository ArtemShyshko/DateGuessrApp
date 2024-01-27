package com.example.dateguessr.model.wiki_api.imageinfo_pojo;

import com.google.gson.annotations.SerializedName;

public class ImageInfoResponse{

	@SerializedName("query")
	private ImageInfoQuery query;

	public ImageInfoQuery getQuery(){
		return query;
	}
}