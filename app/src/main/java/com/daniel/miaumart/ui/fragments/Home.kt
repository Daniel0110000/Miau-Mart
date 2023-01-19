package com.daniel.miaumart.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.daniel.miaumart.R
import com.daniel.miaumart.databinding.FragmentHomeBinding
import com.daniel.miaumart.domain.extensions.load
import com.daniel.miaumart.ui.activities.Login
import com.daniel.miaumart.ui.activities.MyProfile
import com.daniel.miaumart.ui.adapters.ViewPagerAdapter
import com.daniel.miaumart.ui.viewModels.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Home : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private var registeredUser: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initUI()

        return binding.root
    }

    private fun initUI() {
        initTabAndViewPager()
        binding.profileImageLayout.setOnClickListener {
            if (registeredUser) startActivity(Intent(context, MyProfile::class.java))
            else startActivity(Intent(context, Login::class.java))
        }

        viewModel.unregisteredUser.observe(viewLifecycleOwner) { unregistered ->
            if (unregistered) {
                binding.profileImage.setImageResource(R.drawable.ic_login)
            } else {
                registeredUser = true
                loadProfileImage()
            }
        }
    }

    private fun loadProfileImage() {
        viewModel.profileImage.observe(viewLifecycleOwner) { image ->
            if (image != null) {
                binding.profileImage.load(image.toString())
            }
        }
    }

    private fun initTabAndViewPager() {
        val categoriesArray =
            arrayOf(R.string.foods, R.string.medicines, R.string.accessories, R.string.toys)
        val iconsArray = arrayOf(
            R.drawable.ic_food,
            R.drawable.ic_medicine,
            R.drawable.ic_accessory,
            R.drawable.ic_toy
        )
        binding.viewPager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(binding.categories, binding.viewPager) { tab, position ->
            tab.text = resources.getString(categoriesArray[position])
            tab.icon = ResourcesCompat.getDrawable(resources, iconsArray[position], null)
        }.attach()
    }

}