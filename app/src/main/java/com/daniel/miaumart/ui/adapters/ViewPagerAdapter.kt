package com.daniel.miaumart.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.daniel.miaumart.domain.utilities.Constants.NUMB_TABS
import com.daniel.miaumart.ui.fragments.Accessories
import com.daniel.miaumart.ui.fragments.Foods
import com.daniel.miaumart.ui.fragments.Medicines
import com.daniel.miaumart.ui.fragments.Toys

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = NUMB_TABS

    override fun createFragment(position: Int): Fragment {
        when (position){
            0 -> return Foods()
            1 -> return Medicines()
            2 -> return Accessories()
            3 -> return Toys()
        }
        return Foods()
    }


}