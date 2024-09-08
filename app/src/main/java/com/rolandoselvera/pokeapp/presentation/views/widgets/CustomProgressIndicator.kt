package com.rolandoselvera.pokeapp.presentation.views.widgets

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.airbnb.lottie.LottieAnimationView
import com.rolandoselvera.pokeapp.R

class CustomProgressIndicator(context: Context) : Dialog(context, R.style.Transparent) {

    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_progress_bar)

        val lottieAnimationView: LottieAnimationView = findViewById(R.id.lottie_animation_view)
        lottieAnimationView.playAnimation()
    }
}