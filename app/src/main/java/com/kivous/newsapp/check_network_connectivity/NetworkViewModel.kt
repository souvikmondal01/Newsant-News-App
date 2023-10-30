package com.kivous.newsapp.check_network_connectivity

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(listenNetwork: ListenNetwork) : ViewModel() {
    val isConnected: Flow<Boolean> = listenNetwork.isConnected
}