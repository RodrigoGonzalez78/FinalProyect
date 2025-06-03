package com.example.finalproyect.presenter.navigator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproyect.utils.PreferenceManager
import com.example.finalproyect.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(private val dataStoreRepository: PreferenceManager) :
    ViewModel() {

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    init {
        viewModelScope.launch {
            dataStoreRepository.getAuthToken().collect { token ->
                _isAuthenticated.value = Utils.isJwtValid(token)
            }
        }
    }
}