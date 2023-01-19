package com.daniel.miaumart.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.daniel.miaumart.R
import com.daniel.miaumart.databinding.FragmentHomeBinding
import com.daniel.miaumart.ui.activities.Login
import com.daniel.miaumart.ui.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class Home : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initUI()

        return binding.root
    }

    private fun initUI(){
        initTabAndViewPager()
        binding.profileImage.setOnClickListener { startActivity(Intent(context, Login::class.java)) }
    }

    private fun initTabAndViewPager(){
        val categoriesArray = arrayOf(R.string.foods, R.string.medicines, R.string.accessories, R.string.toys)
        val iconsArray = arrayOf(R.drawable.ic_food, R.drawable.ic_medicine, R.drawable.ic_accessory, R.drawable.ic_toy)
        binding.viewPager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(binding.categories, binding.viewPager){ tab, position ->
            tab.text = resources.getString(categoriesArray[position])
            tab.icon = ResourcesCompat.getDrawable(resources, iconsArray[position], null)
        }.attach()
    }

}