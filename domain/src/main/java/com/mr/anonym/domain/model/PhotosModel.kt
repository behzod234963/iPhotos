package com.mr.anonym.domain.model

import com.google.gson.annotations.SerializedName

data class PhotosModel(

	@field:SerializedName("hits")
	val hits: List<HitsItem>,

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("totalHits")
	val totalHits: Int? = null
)

data class HitsItem(

	@field:SerializedName("webformatHeight")
	val webformatHeight: Int? = null,

	@field:SerializedName("largeImageURL")
	val largeImageURL: String = "",

	@field:SerializedName("imageWidth")
	val imageWidth: Int? = null,

	@field:SerializedName("previewHeight")
	val previewHeight: Int? = null,

	@field:SerializedName("webformatURL")
	val webformatURL: String? = null,

	@field:SerializedName("userImageURL")
	val userImageURL: String? = null,

	@field:SerializedName("previewURL")
	val previewURL: String? = null,

	@field:SerializedName("comments")
	val comments: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("imageHeight")
	val imageHeight: Int? = null,

	@field:SerializedName("tags")
	val tags: String? = null,

	@field:SerializedName("previewWidth")
	val previewWidth: Int? = null,

	@field:SerializedName("downloads")
	val downloads: Int? = null,

	@field:SerializedName("collections")
	val collections: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("pageURL")
	val pageURL: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("imageSize")
	val imageSize: Int? = null,

	@field:SerializedName("webformatWidth")
	val webformatWidth: Int? = null,

	@field:SerializedName("user")
	val user: String? = null,

	@field:SerializedName("views")
	val views: Int? = null,

	@field:SerializedName("likes")
	val likes: Int? = null
)
