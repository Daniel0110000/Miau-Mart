package com.daniel.miaumart.ui.activities

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.daniel.miaumart.R
import com.daniel.miaumart.databinding.ActivityMainBinding
import com.daniel.miaumart.domain.utilities.NetworkStateReceiver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var networkStateReceiver: NetworkStateReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()

    }

    override fun onResume() {
        super.onResume()
        networkStateReceiver = NetworkStateReceiver(binding.noInternetAccessLayout)
        val connectivity = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkStateReceiver, connectivity)
    }

    private fun initBottomNavigation(){
        val navController = findNavController(R.id.fragment)
        binding.bottomNavigation.setupWithNavController(navController)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkStateReceiver)
    }

}