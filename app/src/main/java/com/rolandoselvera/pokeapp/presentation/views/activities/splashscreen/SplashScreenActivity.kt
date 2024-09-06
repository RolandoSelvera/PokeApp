package com.rolandoselvera.pokeapp.presentation.views.activities.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.rolandoselvera.pokeapp.presentation.views.activities.main.MainActivity
import com.rolandoselvera.pokeapp.R
import com.rolandoselvera.pokeapp.presentation.viewmodels.splashscreen.SplashViewModel

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        observeSplashLiveData()
    }

    private fun observeSplashLiveData() {

        splashViewModel = ViewModelProvider(this)[SplashViewModel::class.java]

        splashViewModel.startDelaySplashScreen()

        splashViewModel.liveData.observe(this) {
            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            finish()
        }
    }
}