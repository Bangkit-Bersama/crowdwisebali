package com.bangkit.crowdwisebali.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit.crowdwisebali.BuildConfig.apiKey
import com.bangkit.crowdwisebali.R
import com.bangkit.crowdwisebali.data.local.FavoriteRepository
import com.bangkit.crowdwisebali.data.remote.response.DataDetail
import com.bangkit.crowdwisebali.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    private var isFavorite: Boolean = false
    private lateinit var currentPlace: DataDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = FavoriteRepository(application)
        val factory = DetailFactory(repository)

        detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        val placeId = intent.getStringExtra("place_id")
        if (placeId != null) {
            detailViewModel.fetchPlacesDetail(placeId)
        }else {
            Log.e("DetailActivity", "place_id is missing")
        }

        detailViewModel.placesDetail.observe(this) { placesDetail ->
            Log.d("DetailActivity", "Received place details: $placesDetail")
            if (placesDetail != null) {
                with(binding) {
                    tvDestName.text = placesDetail.placeName
                    tvDestLoc.text = placesDetail.googleMapsLink
                    rating.text = placesDetail.rating?.toString() ?: "0"
                    userCount.text = "(${placesDetail.userRatingCount ?: 0})"

                    val photoReference = placesDetail.photos?.firstOrNull()?.photoReference
                    val photoUrl = if (photoReference != null) {
                        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=${photoReference}&key=$apiKey"
                    } else {
                        null
                    }
                    if (!photoUrl.isNullOrEmpty()) {
                        Glide.with(this@DetailActivity)
                            .load(photoUrl)
                            .into(binding.imgDestPhoto)
                        Log.d("API_Response", "Photo Reference: $photoReference")
                        Log.d("API_Response", "Photo URL: $photoUrl")
                    } else {
                        Glide.with(binding.root.context)
                            .load(R.drawable.image_placeholder)
                            .into(binding.imgDestPhoto)
                    }
                }
                updateUI(placesDetail)
                currentPlace = placesDetail
            }
        }



        detailViewModel.isDetailLoading.observe(this) { isDetailLoading ->
            binding.progressBarDetail.visibility = if (isDetailLoading) View.VISIBLE else View.GONE
        }

        detailViewModel.snackBarText.observe(this) { snackBarText ->
            if(snackBarText != null) {
                Snackbar.make(binding.root, snackBarText, Snackbar.LENGTH_LONG).show()
            }
        }

        setupFabFavorite(placeId)
    }

    private fun setupFabFavorite(placesId: String?) {
        detailViewModel.checkFavoriteStatus(placesId.toString())

        detailViewModel.isFavorite.observe(this) { isFav ->
            isFavorite = isFav
            updateFabIcon(isFavorite)
        }

        binding.fabFav.setOnClickListener {
            if (isFavorite) {
                detailViewModel.deleteFromFavorite(currentPlace.placeId.toString())
                Snackbar.make(binding.root, "${currentPlace.placeName} dihapus dari favorit", Snackbar.LENGTH_SHORT).show()
            } else {
                detailViewModel.addToFavorite(currentPlace)
                Snackbar.make(binding.root, "${currentPlace.placeName} ditambahkan ke favorit", Snackbar.LENGTH_SHORT).show()
            }
            isFavorite = !isFavorite
            updateFabIcon(isFavorite)
        }
    }

    private fun updateFabIcon(isFavorite: Boolean) {
        binding.fabFav.setImageResource(
            if (isFavorite) R.drawable.baseline_favorite_24
            else R.drawable.baseline_favorite_border_24
        )
    }

    private fun updateUI(placeDetail: DataDetail) {
        binding.tvDestName.text = placeDetail.placeName
        binding.tvDestLoc.text = placeDetail.formattedAddress
        val photoReference = placeDetail.photos?.firstOrNull()?.photoReference
        val photoUrl = if (photoReference != null) {
            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=${photoReference}&key=$apiKey"
        } else {
            null
        }

        if (!photoUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(photoUrl)
                .into(binding.imgDestPhoto)
        } else {
            Glide.with(this)
                .load(R.drawable.image_placeholder)
                .into(binding.imgDestPhoto)
        }

        binding.btnMaps.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(placeDetail.googleMapsLink))
            startActivity(intent)
        }
    }
}