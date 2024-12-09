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
        binding.rvLodging.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPointOfInterest.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvEstablishment.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        homeViewModel.recommendation.observe(viewLifecycleOwner) { response ->
            val groupedData = homeViewModel.filterPlacesByType(response)

            homeAdapter.submitList(response)
            binding.rvRecommendation.adapter = homeAdapter

            // Use proper adapter for each RecyclerView
            lodgingAdapter.submitList(groupedData["lodging"])
            binding.rvLodging.adapter = lodgingAdapter

            poiAdapter.submitList(groupedData["point_of_interest"])
            binding.rvPointOfInterest.adapter = poiAdapter

            establishmentAdapter.submitList(groupedData["establishment"])
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
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latitude: Double = location.latitude
                    val longitude: Double = location.longitude

                    homeViewModel.fetchRecommendation(latitude, longitude, "restaurant") // Misalnya, "restaurant" untuk placeType
                } else {
                    Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

}