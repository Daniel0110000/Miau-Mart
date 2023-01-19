package com.daniel.miaumart.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.daniel.miaumart.databinding.ActivityLoginBinding
import com.daniel.miaumart.ui.commons.Snackbar
import com.daniel.miaumart.ui.viewModels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

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
        binding.backLayout.setOnClickListener { finish() }

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

}