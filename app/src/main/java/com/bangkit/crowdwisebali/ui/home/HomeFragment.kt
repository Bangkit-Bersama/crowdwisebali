package com.bangkit.crowdwisebali.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.crowdwisebali.databinding.FragmentHomeBinding
import com.bangkit.crowdwisebali.ui.detail.DetailActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        //recommendation RV
//        homeAdapter = HomeAdapter{ titleObject -> //titleObject sesuaiin sm response di Adapter
//            val intent = Intent(context, DetailActivity::class.java).apply {
//                putExtra("places_id", places.id) //places --> endpoints detail
//            }
//            startActivity(intent)
//        }

        binding.rvRecommendation.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        binding.rvRecommendation.adapter = homeAdapter
        binding.rvRecommendation.isNestedScrollingEnabled = true

//        observeViewModel()
    }

    //LiveData
//    private fun observeViewModel(){
//        homeViewModel.isRecommendationLoading.observe(viewLifecycleOwner){ isLoading ->
//            showLoadingRecommendation(isLoading)
//        }
//
//        homeViewModel.recommendation.observe(viewLifecycleOwner) { events ->
//            recommendation.submitList(events.take(5)) //maks 5 muncul
//        }
//    }

    private fun showLoadingRecommendation(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}