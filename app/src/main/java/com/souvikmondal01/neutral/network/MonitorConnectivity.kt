package com.souvikmondal01.neutral.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object MonitorConnectivity {
    fun isConnected(connectivityManager: ConnectivityManager): Boolean =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}
