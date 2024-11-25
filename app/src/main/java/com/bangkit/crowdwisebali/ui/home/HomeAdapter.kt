package com.bangkit.crowdwisebali.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.crowdwisebali.databinding.ItemHomeBinding

//NamaResponse : Need API (response from API)
class HomeAdapter{
//class HomeAdapter(private val onClick: (NamaResponse) -> Unit) :
//    ListAdapter<NamaResponse, HomeAdapter.HomeViewHolder>(DIFF_CALLBACK){

//    class HomeViewHolder(private val binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(------){
//            binding.tvDestName.text = //titleObject.name --> sesuain sama nama di API
//            binding.tvDestLoc.text = //titleObject.Location --> sesuain sama nama di API
//            binding.tvDestCrowd.text = //titleObject.Prediction --> sesuain sama nama di API
//            Glide.with(binding.root.context)
//                .load(titleObject.namaPhoto) //namaPhoto sesuaiin sama nama di API
//                .into(binding.imgDestPhoto)
//        }
//    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.HomeViewHolder {
//        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return HomeViewHolder(binding)
//    }

//    override fun onBindViewHolder(holder: HomeViewHolder, position: Int){
//        val dest = getItem(position)  //val dest masih sementara bingung, nanti mau seusaiin sm response
//        holder.bind(dest, onClick)
//    }

    //NamaResponse nanti disesuaikan
//    companion object{
//        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NamaResponse>(){
//            override fun areItemsTheSame(oldItem: NamaResponse, newItem: NamaResponse): Boolean {
//                return oldItem.id == newItem.id
//            }
//
//            override fun areContentsTheSame(oldItem: NamaResponse, newItem: NamaResponse): Boolean {
//                return oldItem == newItem
//            }
//        }
//    }
}