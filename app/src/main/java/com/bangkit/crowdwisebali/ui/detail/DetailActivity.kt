package com.bangkit.crowdwisebali.ui.detail

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bangkit.crowdwisebali.BuildConfig.apiKey
import com.bangkit.crowdwisebali.R
import com.bangkit.crowdwisebali.data.local.FavoriteRepository
import com.bangkit.crowdwisebali.data.pref.SharedPreferenceManager
import com.bangkit.crowdwisebali.data.remote.response.DataDetail
import com.bangkit.crowdwisebali.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    private var isFavorite: Boolean = false
    private lateinit var currentPlace: DataDetail

    private val calendar = Calendar.getInstance()

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferenceManager = SharedPreferenceManager(this)

        sharedPreferenceManager.getFirebaseAuthToken { token ->
            if (token != null) {
                val repository = FavoriteRepository(application)
                val factory = DetailFactory(repository, token)

                detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

                val placeId = intent.getStringExtra("place_id")
                if (placeId != null) {
                    detailViewModel.fetchPlacesDetail(placeId)
                } else {
                    Log.e("DetailActivity", "place_id is missing")
                }
                observeViewModel()
            } else {
                Log.e("DetailActivity", "Failed to retrieve token")
            }
        }

        binding.edDate.setOnClickListener {
            val dateListener = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val formattedDate = String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth)
                binding.edDate.setText(formattedDate)
            }

            DatePickerDialog(
                this,
                dateListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.edTime.setOnClickListener {
            val timeListener = TimePickerDialog.OnTimeSetListener { _: TimePicker, hourOfDay: Int, _: Int ->
                val formattedTime = String.format("%02d", hourOfDay)
                binding.edTime.setText(formattedTime)
            }

            TimePickerDialog(
                this,
                timeListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        detailViewModel.placesDetail.observe(this) { placesDetail ->
            Log.d("DetailActivity", "Received place details: $placesDetail")
            if (placesDetail != null) {
                currentPlace = placesDetail
                with(binding) {
                    tvDestName.text = placesDetail.placeName
                    tvDestLoc.text = placesDetail.formattedAddress
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
                setupFabFavorite(placesDetail.placeId)

                binding.btnPrediction.setOnClickListener {
                    val placesId = placesDetail.placeId ?: return@setOnClickListener
                    val date = binding.edDate.text.toString().takeIf { it.isNotEmpty() } ?: return@setOnClickListener
                    val hourString = binding.edTime.text.toString().takeIf { it.isNotEmpty() } ?: return@setOnClickListener

                    if (date.isEmpty()) {
                        Snackbar.make(binding.root, "Masukkan tanggal", Snackbar.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    if (hourString.isEmpty()) {
                        Snackbar.make(binding.root, "Masukkan waktu", Snackbar.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    val hour = hourString.toIntOrNull()
                    if(hour == null) {
                        Snackbar.make(binding.root, "Waktu tidak valid", Snackbar.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    lifecycleScope.launch {
                        detailViewModel.fetchPrediction(placesId, date, hour)
                    }
                }

                detailViewModel.predictionResult.observe(this) { predictionResult ->
                    if (predictionResult != null) {
                        val occupancy = predictionResult.occupancy
                        val category = categorizeOccupancy(occupancy,this)
                        binding.resultPrediction.text = "$occupancy% ($category)"
                    } else {
                        Snackbar.make(binding.root, "Tidak ada hasil prediksi", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

        detailViewModel.isPredictionLoading.observe(this) { isPredictionLoading ->
            binding.progressBarDetail.visibility = if (isPredictionLoading) View.VISIBLE else View.GONE
        }

        detailViewModel.statusPrediction.observe(this) { statusPrediction ->
            if (statusPrediction != null ) {
                Snackbar.make(binding.root, statusPrediction, Snackbar.LENGTH_SHORT).show()
            }
        }

        detailViewModel.isDetailLoading.observe(this) { isDetailLoading ->
            binding.progressBarDetail.visibility = if (isDetailLoading) View.VISIBLE else View.GONE
        }

        detailViewModel.snackBarText.observe(this) { snackBarText ->
            if (snackBarText != null) {
                Snackbar.make(binding.root, snackBarText, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun categorizeOccupancy(occupancy: Double, context: Context): String {
        return when (occupancy) {
            in 0.0..25.0 -> context.getString(R.string.quiet)
            in 25.1..50.0 -> context.getString(R.string.somewhat_busy)
            in 50.1..75.0 -> context.getString(R.string.crowded)
            in 75.1..100.0 -> context.getString(R.string.very_crowded)
            else -> context.getString(R.string.invalid)
        }
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