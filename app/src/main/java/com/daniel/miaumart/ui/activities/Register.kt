package com.daniel.miaumart.ui.activities

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.daniel.miaumart.R
import com.daniel.miaumart.databinding.ActivityRegisterBinding
import com.daniel.miaumart.domain.utilities.NetworkStateReceiver
import com.daniel.miaumart.ui.commons.Snackbar
import com.daniel.miaumart.ui.viewModels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var networkStateReceiver: NetworkStateReceiver

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

    }

    override fun onResume() {
        super.onResume()
        networkStateReceiver = NetworkStateReceiver(binding.noInternetAccessLayoutRegister)
        val connectivity = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkStateReceiver, connectivity)
    }

    private fun initUI(){

        binding.register = viewModel

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        binding.backLayout.setOnClickListener { finish(); Animatoo.animateSlideRight(this) }
        binding.redirectToLogin.setOnClickListener { finish(); Animatoo.animateSlideRight(this) }
        binding.openGallery.setOnClickListener { openGallery() }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.registerProgressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
            binding.registerButton.visibility = if(isLoading) View.GONE else View.VISIBLE
        }

        viewModel.message.observe(this){ message ->
            if(message.isNotEmpty()){
                Snackbar.showMessage(message, binding.registerLayout)
                viewModel.message.value = ""
            }
        }

        viewModel.completed.observe(this){ completed ->
            if(completed){
                binding.apply {
                    inputUsername.text.clear()
                    inputPassword.text.clear()
                    inputRepeatPassword.text.clear()
                    imagePreview.visibility = View.GONE
                    iconAddImage.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun openGallery(){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        gallery.type = "image/*"
        getResult.launch(gallery)
    }

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK){
            binding.apply {
                iconAddImage.visibility = View.GONE
                imagePreview.apply {
                    visibility = View.VISIBLE
                    setImageURI(result?.data?.data)
                }
            }
            viewModel.profileImage.value = result.data?.data
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkStateReceiver)
    }

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
            Animatoo.animateSlideRight(this@Register)
        }

    }


}