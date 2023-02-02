package com.daniel.miaumart.ui.activities

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.daniel.miaumart.databinding.ActivityLoginBinding
import com.daniel.miaumart.domain.utilities.NetworkStateReceiver
import com.daniel.miaumart.ui.commons.Snackbar
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

        binding.apply {
            login = viewModel
            redirectToRegister.setOnClickListener {
                startActivity(Intent(this@Login, Register::class.java)); Animatoo.animateSlideLeft(
                this@Login
            )
            }
        }

        binding.backLayout.setOnClickListener { finish(); Animatoo.animateSlideDown(this) }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        viewModel.isLoading.observe(this) { isLoading ->
            binding.loginProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.loginButton.visibility = if (isLoading) View.GONE else View.VISIBLE

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
                    Animatoo.animateSlideDown(this)
                }
            }
        }

    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkStateReceiver)
    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
                Animatoo.animateSlideDown(this@Login)
            }

        }

}