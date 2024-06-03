package com.dicoding.userrespon

import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.userrespon.helper.SettingPref
import com.dicoding.userrespon.helper.dataStore
import com.dicoding.userrespon.ui.ViewModel.ModelViewFactory
import com.dicoding.userrespon.ui.ViewModel.SwitchThemeViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

class SwitchThemeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_switch_theme)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.switch_theme)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val themeSwitch = findViewById<SwitchMaterial>(R.id.switch_theme)

        val pref =
            com.dicoding.userrespon.helper.SettingPref.getInstance(application.dataStore)

        val switchViewModel = ViewModelProvider(this, ModelViewFactory(pref)).get(
            SwitchThemeViewModel::class.java
        )
        switchViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                themeSwitch.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                themeSwitch.isChecked = false
            }
        }
        themeSwitch.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            switchViewModel.saveThemeSettings(isChecked)
        }
    }
}