package com.daniel.miaumart.ui.activities

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.daniel.miaumart.R
import com.daniel.miaumart.databinding.ActivityLoginBinding
import com.daniel.miaumart.domain.utilities.NetworkStateReceiver
import com.daniel.miaumart.ui.commons.Snackbar
import com.daniel.miaumart.ui.fragments.Home
import com.daniel.miaumart.ui.viewModels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var networkStateReceiver: NetworkStateReceiver

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

    }

    override fun onResume() {
        super.onResume()
        networkStateReceiver = NetworkStateReceiver(binding.noInternetAccessLayoutLogin)
        val connectivity = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkStateReceiver, connectivity)
    }

    private fun initUI() {

        binding.login = viewModel
        binding.redirectToRegister.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    Register::class.java
                )
            )
        }
        binding.backLayout.setOnClickListener {
            finish()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.loginProgressBar.visibility = View.VISIBLE
                binding.loginButton.visibility = View.GONE
            } else {
                binding.loginProgressBar.visibility = View.GONE
                binding.loginButton.visibility = View.VISIBLE
            }

            viewModel.message.observe(this) { message ->
                if (message.isNotEmpty()) {
                    Snackbar.showMessage(message, binding.loginLayout)
                    viewModel.message.value = ""
                }
            }

            viewModel.completed.observe(this) { completed ->
                if (completed) {
                    binding.inputUsername.text.clear()
                    binding.inputPassword.text.clear()
                    viewModel.completed.value = false
                    finish()
                }
            }
        }

    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkStateReceiver)
    }

}