package com.bangkit.crowdwisebali.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.crowdwisebali.databinding.FragmentFavoriteBinding
import com.bangkit.crowdwisebali.ui.detail.DetailActivity
import com.bangkit.crowdwisebali.R

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FavoriteAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteViewModel = ViewModelProvider(this, FavoriteFactory.getInstance(requireContext()))[FavoriteViewModel::class.java]
        setDynamicSpanCount()

        val displayMetrics = requireContext().resources.displayMetrics
        val width = displayMetrics.widthPixels
        val cardWidth = resources.getDimension(R.dimen.card_width)
        val spanCount = (width / cardWidth).toInt()

        binding.rvItemFav.layoutManager = GridLayoutManager(requireContext(), spanCount)
        adapter = FavoriteAdapter { favorite ->
            val placeId = favorite.id
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("place_id", placeId)
            }
            startActivity(intent)
        }
        binding.rvItemFav.adapter = adapter


        observeViewModel()
    }

    private fun observeViewModel() {
        favoriteViewModel.favorite.observe(viewLifecycleOwner) { favorite ->
            if (favorite == null ||favorite.isEmpty()) {
                Toast.makeText(requireContext(), "Tidak ada acara favorit", Toast.LENGTH_SHORT).show()
                binding.rvItemFav.visibility = View.GONE
            } else {
                adapter.submitList(favorite)
            }
        }

        favoriteViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun setDynamicSpanCount() {
        val displayMetrics = requireContext().resources.displayMetrics
        val width = displayMetrics.widthPixels
        val cardWidth = resources.getDimension(R.dimen.card_width)
        val spanCount = (width / cardWidth).toInt()

        (binding.rvItemFav.layoutManager as? GridLayoutManager)?.spanCount = spanCount
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}