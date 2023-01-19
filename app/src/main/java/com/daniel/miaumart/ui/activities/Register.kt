package com.daniel.miaumart.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.daniel.miaumart.R
import com.daniel.miaumart.databinding.ActivityRegisterBinding
import com.daniel.miaumart.ui.commons.Snackbar
import com.daniel.miaumart.ui.viewModels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

    }

    private fun initUI(){

        binding.register = viewModel

        binding.backLayout.setOnClickListener { finish() }
        binding.redirectToLogin.setOnClickListener { finish() }
        binding.openGallery.setOnClickListener { openGallery() }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.registerProgressBar.visibility = View.VISIBLE
                binding.registerButton.visibility = View.GONE
            } else {
                binding.registerProgressBar.visibility = View.GONE
                binding.registerButton.visibility = View.VISIBLE
            }
        }

        viewModel.message.observe(this){ message ->
            if(message.isNotEmpty()){
                Snackbar.showMessage(message, binding.registerLayout)
                viewModel.message.value = ""
            }
        }

        viewModel.completed.observe(this){ completed ->
            if(completed){
                binding.inputUsername.text.clear()
                binding.inputPassword.text.clear()
                binding.inputRepeatPassword.text.clear()
                binding.imagePreview.visibility = View.GONE
                binding.iconAddImage.visibility = View.VISIBLE
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
            binding.iconAddImage.visibility = View.GONE
            binding.imagePreview.visibility = View.VISIBLE
            binding.imagePreview.setImageURI(result.data?.data)
            viewModel.profileImage.value = result.data?.data
        }
    }

}