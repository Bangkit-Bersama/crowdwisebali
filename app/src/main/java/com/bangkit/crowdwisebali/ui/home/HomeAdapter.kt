package com.bangkit.crowdwisebali.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.crowdwisebali.R
import com.bangkit.crowdwisebali.data.remote.response.SearchResultItem // Assuming SearchResultItem is the correct data model
import com.bangkit.crowdwisebali.databinding.ItemHomeBinding
import com.bumptech.glide.Glide

class HomeAdapter(private val onClick: (SearchResultItem) -> Unit) : // Update to use SearchResultItem
    ListAdapter<SearchResultItem, HomeAdapter.HomeViewHolder>(DIFF_CALLBACK) { // Update the adapter to use SearchResultItem

    class HomeViewHolder(private val binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(response: SearchResultItem, onClick: (SearchResultItem) -> Unit) { // Update the bind method to use SearchResultItem
            binding.tvDestName.text = response.placeName // Assuming 'name' is a property of SearchResultItem
            binding.rating.text = response.rating?.toString() ?: "0"
            binding.userCount.text = response.userRatingCount?.toString() ?: "(0)"
            binding.tvDestLoc.text = response.googleMapsLink // Assuming 'location' is a property of SearchResultItem

            val photoReference = response.photos?.firstOrNull()?.photoReference
            val photoUrl = if (photoReference != null) {
                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=$photoReference&key=API_KEY" // Replace with actual API key
            } else {
                null
            }
            if (!photoUrl.isNullOrEmpty()) {
                Glide.with(binding.root.context)
                    .load(photoUrl)
                    .into(binding.imgDestPhoto)
                Log.d("API_Response", "Photo Reference: $photoReference")
                Log.d("API_Response", "Photo URL: $photoUrl")
            } else {
                Glide.with(binding.root.context)
                    .load(R.drawable.welcome)
                    .into(binding.imgDestPhoto)
            }

            binding.root.setOnClickListener {
                onClick(response) // Trigger onClick with SearchResultItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.HomeViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place, onClick)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchResultItem>() { // Update to use SearchResultItem
            override fun areItemsTheSame(oldItem: SearchResultItem, newItem: SearchResultItem): Boolean {
                return oldItem.placeId == newItem.placeId // Assuming placeId is a unique identifier in SearchResultItem
            }

            override fun areContentsTheSame(oldItem: SearchResultItem, newItem: SearchResultItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
