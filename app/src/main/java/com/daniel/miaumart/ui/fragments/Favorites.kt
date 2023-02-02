package com.daniel.miaumart.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.daniel.miaumart.R
import com.daniel.miaumart.databinding.FragmentFavoritesBinding
import com.daniel.miaumart.domain.extensions.loadWithGlide
import com.daniel.miaumart.domain.models.FavoritesML
import com.daniel.miaumart.ui.activities.Login
import com.daniel.miaumart.ui.activities.ProductDetails
import com.daniel.miaumart.ui.adapters.FavoritesItemClickListener
import com.daniel.miaumart.ui.adapters.RecyclerFavoritesAdapter
import com.daniel.miaumart.ui.commons.BasicUserData
import com.daniel.miaumart.ui.commons.Snackbar
import com.daniel.miaumart.ui.viewModels.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Favorites : Fragment(), FavoritesItemClickListener {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels()

    private var documentExists = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        if (BasicUserData.isRegistered) initUI()
        else {
            startActivity(Intent(requireContext(), Login::class.java))
            Animatoo.animateSlideLeft(requireActivity())
            val navController = Navigation.findNavController(requireActivity(), R.id.fragment)
            navController.popBackStack()
        }

        return binding.root

    }

    private fun initUI() {
        binding.recyclerFavorites.visibility = View.GONE
        binding.profileImageFav.loadWithGlide(requireContext(), BasicUserData.profileImage)
        viewModel.getAllProductsFavorites(BasicUserData.username) {
            documentExists = true
            onDataRetrieved(it)
        }
        viewModel.message.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Snackbar.showMessage(message, binding.favoriteLayout)
                viewModel.message.value = ""
            }
        }

        if (!documentExists) binding.emptyFavoritesLayout.visibility = View.VISIBLE

    }

    private fun onDataRetrieved(favoritesList: ArrayList<FavoritesML>) {
        binding.numberFavorites.text = favoritesList.size.toString()
        binding.emptyFavoritesLayout.visibility =
            if (favoritesList.isEmpty()) View.VISIBLE else View.GONE
        binding.recyclerFavorites.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
            adapter = RecyclerFavoritesAdapter(favoritesList, this@Favorites)
            visibility = if (favoritesList.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    override fun onDestroy() {
        viewModel.stopListening(BasicUserData.username) { onDataRetrieved(it) }
        super.onDestroy()
    }

    override fun onItemClickListener(pid: String, category: String) {
        val productsDetails = Intent(requireContext(), ProductDetails::class.java)
        productsDetails.putExtra("pid", pid)
        productsDetails.putExtra("category", category)
        startActivity(productsDetails)
        Animatoo.animateSlideLeft(requireActivity())
    }

    override fun onDeleteClickListener(favoriteId: String) {
        viewModel.deleteFavorite(favoriteId)
    }
}