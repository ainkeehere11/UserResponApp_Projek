package com.dicoding.userrespon.ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.userrespon.helper.SettingPref
import kotlinx.coroutines.launch

class SwitchThemeViewModel(private val pref: SettingPref) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSettings().asLiveData()
    }

    fun saveThemeSettings(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSettings(isDarkModeActive)
        }
    }
}