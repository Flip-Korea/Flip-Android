package com.team.flip

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.DataStoreManager
import com.team.domain.type.DataStoreType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
): ViewModel() {
    private val TAG = this.javaClass.simpleName

    private val _loggedIn: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val loggedIn: StateFlow<Boolean?> = _loggedIn.asStateFlow()

    init {
        viewModelScope.launch {
            val accessToken = dataStoreManager.getStringData(DataStoreType.TokenType.ACCESS_TOKEN).first()
            // test.accesstoken.240615
            Log.d("access_token_log", accessToken ?: "")
            delay(500L)
            if (!accessToken.isNullOrEmpty()) {
                Log.d(TAG, accessToken)
                _loggedIn.update { true }
            } else {
                _loggedIn.update { false }
            }
        }
    }
}