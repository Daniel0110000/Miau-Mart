package com.daniel.miaumart.ui.activities

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.daniel.miaumart.databinding.ActivityMyProfileBinding
import com.daniel.miaumart.domain.extensions.loadWithGlide
import com.daniel.miaumart.domain.models.History
import com.daniel.miaumart.domain.utilities.NetworkStateReceiver
import com.daniel.miaumart.ui.adapters.RecyclerHistoryAdapter
import com.daniel.miaumart.ui.commons.AlertDialog
import com.daniel.miaumart.ui.commons.BasicUserData
import com.daniel.miaumart.ui.commons.Snackbar
import com.daniel.miaumart.ui.viewModels.MyProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProfile : AppCompatActivity() {

    private lateinit var binding: ActivityMyProfileBinding
    private lateinit var networkStateReceiver: NetworkStateReceiver

    private val viewModel: MyProfileViewModel by viewModels()
    private var documentExists = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

    }

    override fun onResume() {
        super.onResume()
        networkStateReceiver = NetworkStateReceiver(binding.noInternetAccessLayoutMp)
        val connectivity = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkStateReceiver, connectivity)
    }

    private fun initUI() {
        binding.backLayout.setOnClickListener {
            finish()
            Animatoo.animateSlideDown(this)
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        binding.profileImageMp.loadWithGlide(this, BasicUserData.profileImage)
        binding.username.text = BasicUserData.username
        viewModel.completed.observe(this) { completed -> if (completed) finish() }
        binding.changeProfileImage.setOnClickListener { openGallery() }
        viewModel.message.observe(this) { message ->
            Snackbar.showMessage(message, binding.myProfileLayout)
        }
        viewModel.getAllHistory(BasicUserData.username) {
            viewModel.history.value = it
            documentExists = true
            onDataRetrieved(it)
        }
        binding.signOff.setOnClickListener {
            AlertDialog.buildAlertDialog("My Profile", "Do you want to log out?", this) { result ->
                if (result) viewModel.signOff()
                else viewModel.message.value = "Session not closed!"
            }
        }
        binding.deleteAllHistoryLayout.setOnClickListener {
            if(viewModel.history.value?.isNotEmpty() == true){
                AlertDialog.buildAlertDialog(
                    "History",
                    "Do you want to delete your history?",
                    this
                ) { result ->
                    if (result) viewModel.deleteAllHistory()
                    else viewModel.message.value = "History not cleared!"
                }
            }else viewModel.message.value =  "No data to delete"
        }

        if (!documentExists) {
            binding.recyclerHistory.visibility = View.GONE
            binding.emptyHistoryLayout.visibility = View.VISIBLE
            viewModel.history.value = arrayListOf()
        }

    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        gallery.type = "image/*"
        getResult.launch(gallery)
    }

    private var getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                AlertDialog.buildAlertDialog(
                    "My Profile",
                    "Do you want to update your profile image?",
                    this
                ) { resultAlert ->
                    if (resultAlert) {
                        binding.profileImageMp.setImageURI(result.data?.data)
                        result.data?.data?.let { viewModel.updateProfileImage(it) }
                    } else viewModel.message.value = "Profile picture not updated!"
                }
            }
        }

    private fun onDataRetrieved(historyList: ArrayList<History>) {
        binding.numberHistoryItems.text = "(${historyList.size})"
        binding.recyclerHistory.visibility = if(historyList.isNotEmpty()) View.VISIBLE else View.GONE
        binding.emptyHistoryLayout.visibility = if(historyList.isNotEmpty()) View.GONE else View.VISIBLE
        binding.recyclerHistory.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
            adapter = RecyclerHistoryAdapter(historyList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopListening(BasicUserData.username) { onDataRetrieved(it) }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkStateReceiver)
    }

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
            Animatoo.animateSlideDown(this@MyProfile)
        }

    }

}