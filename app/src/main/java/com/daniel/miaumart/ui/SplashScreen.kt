package com.daniel.miaumart.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import com.daniel.miaumart.MainActivity
import com.daniel.miaumart.R
import com.daniel.miaumart.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        startTimer()

    }

    private fun startTimer(){
        object : CountDownTimer(3000L, 1000L){
            override fun onTick(p0: Long) {}

            override fun onFinish() {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        }.start()
    }

}