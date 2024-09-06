package com.rolandoselvera.pokeapp.common.internet

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * Check internet connection from device.
 *
 * @param context
 */
class ConnectivityUtil(private val context: Context?) {

    fun checkConnectivity(): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val isNetworkActive = connectivityManager.activeNetwork ?: return false

        val capabilitiesNetwork =
            connectivityManager.getNetworkCapabilities(isNetworkActive) ?: return false

        return when {
            capabilitiesNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilitiesNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}