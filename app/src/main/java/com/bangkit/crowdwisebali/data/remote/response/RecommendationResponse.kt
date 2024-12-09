package com.bangkit.crowdwisebali.data.remote.response

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("search_result")
	val searchResult: List<SearchResultItem> = listOf()
)


data class SearchResultItem(

	@field:SerializedName("google_maps_link")
	val googleMapsLink: String? = null,

	@field:SerializedName("place_name")
	val placeName: String? = null,

	@field:SerializedName("user_rating_count")
	val userRatingCount: Int? = null,

	@field:SerializedName("place_type")
	val placeType: List<String?>? = null,

	@field:SerializedName("rating")
	val rating: Any? = null,

	@field:SerializedName("photos")
	val photos: List<PhotosItem?>? = null,

	@field:SerializedName("place_id")
	val placeId: String? = null
)

data class PhotosItem(

	@field:SerializedName("photo_reference")
	val photoReference: String? = null,

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("html_attributions")
	val htmlAttributions: List<String?>? = null,

	@field:SerializedName("height")
	val height: Int? = null
)

