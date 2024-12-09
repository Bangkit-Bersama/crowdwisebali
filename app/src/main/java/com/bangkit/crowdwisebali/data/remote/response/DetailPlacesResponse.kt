package com.bangkit.crowdwisebali.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailPlacesResponse(

	@field:SerializedName("data")
	val data: DataDetail? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataDetail(

	@field:SerializedName("google_maps_link")
	val googleMapsLink: String? = null,

	@field:SerializedName("place_name")
	val placeName: String? = null,

	@field:SerializedName("user_rating_count")
	val userRatingCount: Int? = null,

	@field:SerializedName("formatted_address")
	val formattedAddress: String? = null,

	@field:SerializedName("reviews")
	val reviews: List<ReviewsItem?>? = null,

	@field:SerializedName("place_type")
	val placeType: List<String?>? = null,

	@field:SerializedName("rating:")
	val rating: Any? = null,

	@field:SerializedName("photos")
	val photos: List<PhotosItemDetail?>? = null,

	@field:SerializedName("place_id")
	val placeId: String? = null
)

data class ReviewsItem(

	@field:SerializedName("author_name")
	val authorName: String? = null,

	@field:SerializedName("profile_photo_url")
	val profilePhotoUrl: String? = null,

	@field:SerializedName("author_url")
	val authorUrl: String? = null,

	@field:SerializedName("rating")
	val rating: Int? = null,

	@field:SerializedName("language")
	val language: String? = null,

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("time")
	val time: Int? = null,

	@field:SerializedName("relative_time_description")
	val relativeTimeDescription: String? = null
)

data class PhotosItemDetail(

	@field:SerializedName("photo_reference")
	val photoReference: String? = null,

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("html_attributions")
	val htmlAttributions: List<String?>? = null,

	@field:SerializedName("height")
	val height: Int? = null
)
