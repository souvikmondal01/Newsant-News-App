package com.kivous.newsapp.check_network_connectivity

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object MonitorConnectivity {
    private val impl = Impl
    fun isConnected(connectivityManager: ConnectivityManager): Boolean {
        return impl.isConnected(connectivityManager)
    }
}

interface ConnectedCompat {
    fun isConnected(connectivityManager: ConnectivityManager): Boolean
}

object Impl : ConnectedCompat {
    override fun isConnected(connectivityManager: ConnectivityManager): Boolean {
        return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            ) == true
    }

}