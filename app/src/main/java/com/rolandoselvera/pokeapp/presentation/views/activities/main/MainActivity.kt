package com.rolandoselvera.pokeapp.presentation.views.activities.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rolandoselvera.pokeapp.R
import com.rolandoselvera.pokeapp.presentation.views.widgets.CustomProgressIndicator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var progressIndicator: CustomProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressIndicator = CustomProgressIndicator(this)
    }

    fun showProgress() {
        if (!progressIndicator.isShowing) progressIndicator.show()
    }

    fun hideProgress() {
        if (progressIndicator.isShowing) progressIndicator.dismiss()
    }
}