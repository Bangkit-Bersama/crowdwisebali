package com.bangkit.crowdwisebali.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bangkit.crowdwisebali.R
import com.bangkit.crowdwisebali.databinding.FragmentHomeBinding
import com.bangkit.crowdwisebali.ui.detail.DetailActivity
import com.bumptech.glide.Glide
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import java.io.IOException
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var lodgingAdapter: HomeAdapter
    private lateinit var poiAdapter: HomeAdapter
    private lateinit var establishmentAdapter: HomeAdapter

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var profilePicture: ImageView
    private lateinit var name: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profilePicture = view.findViewById(R.id.profile_picture)
        name = view.findViewById(R.id.name)


        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            name.text = it.displayName
            it.photoUrl?.let { photoUrl ->
                Glide.with(this).load(photoUrl).into(profilePicture)
            }
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // Lanjutkan proses
        } else {
            Log.e("HomeFragment", "User is not logged in")
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // Initialize all adapters
        homeAdapter = HomeAdapter { response ->
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("place_id", response.placeId)
            }
            startActivity(intent)
        }

        lodgingAdapter = HomeAdapter { response ->
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("place_id", response.placeId)
            }
            startActivity(intent)
        }
        poiAdapter = HomeAdapter { response ->
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("place_id", response.placeId)
            }
            startActivity(intent)
        }
        establishmentAdapter = HomeAdapter { response ->
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("place_id", response.placeId)
            }
            startActivity(intent)
        }

        binding.rvRecommendation.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecommendation.isNestedScrollingEnabled = true

        // Fix the mistake with setting adapter for other RecyclerViews
//        binding.rvLodging.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPointOfInterest.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvEstablishment.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        homeViewModel.recommendation.observe(viewLifecycleOwner) { response ->
            val groupedData = homeViewModel.filterPlacesByType(response)

            homeAdapter.submitList(response)
            binding.rvRecommendation.adapter = homeAdapter

            // Use proper adapter for each RecyclerView
//            lodgingAdapter.submitList(groupedData["lodging"])
//            binding.rvLodging.adapter = lodgingAdapter

            poiAdapter.submitList(groupedData["shopping_mall"])
            binding.rvPointOfInterest.adapter = poiAdapter

            establishmentAdapter.submitList(groupedData["restaurant"])
            binding.rvEstablishment.adapter = establishmentAdapter
        }

        homeViewModel.isRecommendationLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoadingRecommendation(isLoading)
        }

        homeViewModel.snackBarText.observe(viewLifecycleOwner) { message ->
            message?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        }

        getMyLastLocation()
    }

    private fun showLoadingRecommendation(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        val safeContext = context ?: return false
        return ContextCompat.checkSelfPermission(safeContext, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        Log.d("HomeFragment", "getMyLastLocation called")

        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latitude: Double = location.latitude
                    val longitude: Double = location.longitude
                    Log.d("HomeFragment", "Location obtained: Latitude=$latitude, Longitude=$longitude")

                    val safeContext = context ?: return@addOnSuccessListener
                    val geocoder = Geocoder(safeContext, Locale.getDefault())
                    try {
                        @Suppress("DEPRECATION")
                        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                        if (!addresses.isNullOrEmpty()) {
                            val address = addresses[0]
                            val subLocality = address.subLocality
                            val locality = address.locality
                            val locationText = if (!subLocality.isNullOrEmpty() && !locality.isNullOrEmpty()) {
                                "$subLocality, $locality"
                            } else if (!locality.isNullOrEmpty()) {
                                locality
                            } else {
                                "Lokasi tidak ditemukan"
                            }

                            Log.d("HomeFragment", "Lokasi: $locationText")
                            binding.tvLocation.text = locationText
                        } else {
                            Log.d("HomeFragment", "No addresses found")
                            binding.tvLocation.text = "Lokasi tidak ditemukan"
                        }
                    } catch (e: IOException) {
                        Log.e("HomeFragment", "Geocoder failed", e)
                        Toast.makeText(safeContext, "Gagal mendapatkan alamat", Toast.LENGTH_SHORT).show()
                    }

                    val mUser = FirebaseAuth.getInstance().currentUser
                    mUser?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            val token = tokenTask.result?.token
                            Log.d("HomeFragment", "Firebase token obtained: $token")

                            if (token != null) {
                                Log.d("HomeFragment", "Fetching recommendations with token...")
                                homeViewModel.fetchRecommendation(latitude, longitude, "restaurant", token)

                                Log.i("HomeFragment", "Token sent to ViewModel: $token")
                            } else {
                                Log.w("HomeFragment", "Firebase token is null")
                            }
                        } else {
                            Log.e("HomeFragment", "Failed to get Firebase token", tokenTask.exception)
                        }
                    } ?: Log.e("HomeFragment", "User is null, cannot retrieve token")
                } else {
                    Log.d("HomeFragment", "Location is null")
                    Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e ->
                Log.e("HomeFragment", "Failed to get last location", e)
            }
        } else {
            Log.d("HomeFragment", "Permissions not granted, requesting permissions")
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}