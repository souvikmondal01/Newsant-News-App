package com.souvikmondal01.neutral.network

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(listenNetwork: ListenNetwork) : ViewModel() {
    val isConnected: Flow<Boolean> = listenNetwork.isConnected
}