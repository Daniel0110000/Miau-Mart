package com.daniel.miaumart.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager.widget.ViewPager
import com.daniel.miaumart.R
import com.daniel.miaumart.databinding.FragmentHomeBinding
import com.daniel.miaumart.ui.activities.MainActivity
import com.daniel.miaumart.ui.activities.ProductDetails
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
        startTimer()

        return binding.root
    }

    private fun initUI(){
        initTabAndViewPager()
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

    private fun startTimer(){
        object : CountDownTimer(3000L, 1000L){
            override fun onTick(p0: Long) {}

            override fun onFinish() {
                startActivity(Intent(context, ProductDetails::class.java))
            }
        }.start()
    }


}