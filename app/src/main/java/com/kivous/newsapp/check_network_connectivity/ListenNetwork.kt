package com.kivous.newsapp.check_network_connectivity

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ListenNetwork @Inject constructor(private val connectivityManager: ConnectivityManager) {

    val isConnected = callbackFlow {
        val callBack = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }
        }
        val request =
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build()

        trySend(MonitorConnectivity.isConnected(connectivityManager))
        connectivityManager.registerNetworkCallback(request, callBack)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callBack)
        }
    }
}