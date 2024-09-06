package com.rolandoselvera.pokeapp.presentation.viewmodels.splashscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    var liveData: MutableLiveData<SplashViewModel> = MutableLiveData()

    fun startDelaySplashScreen() {
        viewModelScope.launch {
            delay(2500)
            updateLiveData()
        }
    }

    private fun updateLiveData() {
        val splashViewModel = SplashViewModel()
        liveData.value = splashViewModel
    }
}