package com.bangkit.crowdwisebali.ui.favorite

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.crowdwisebali.BuildConfig.apiKey
import com.bangkit.crowdwisebali.R
import com.bangkit.crowdwisebali.data.local.FavoriteEntity
import com.bangkit.crowdwisebali.databinding.ItemFavBinding
import com.bumptech.glide.Glide

class FavoriteAdapter (private val onClick: (FavoriteEntity) -> Unit): RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private var favorite: List<FavoriteEntity> = emptyList()
    @SuppressLint("NotifyDataSetChanged")
    fun submitList(favorite: List<FavoriteEntity>) {
        this.favorite = favorite
        notifyDataSetChanged()
    }

    class FavoriteViewHolder(private val binding: ItemFavBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(place: FavoriteEntity, onClick: (FavoriteEntity) -> Unit) {
            binding.tvDestName.text = place.name
            binding.tvDestLoc.text = place.location
            binding.rating.text = place.rating?.toString() ?: "0"
            binding.userCount.text = "(${place.userRatingCount ?: 0})"

            val photoReference = place.photosItem?.firstOrNull() ?: ""
            val photoUrl = if (photoReference.isNotEmpty()) {
                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=$photoReference&key=$apiKey"
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
                    .load(R.drawable.image_placeholder)
                    .into(binding.imgDestPhoto)
            }
            binding.root.setOnClickListener {
                onClick(place)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun getItemCount(): Int = favorite.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val place = favorite[position]
        holder.bind(place, onClick)
    }
}