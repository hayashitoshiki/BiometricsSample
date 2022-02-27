package com.myapp.biometricssample.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.myapp.biometricssample.ui.theme.BiometricsSampleTheme


class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BiometricsSampleTheme {
                Surface(Modifier.background(MaterialTheme.colors.background)) {
                    DiaryAppNavHost()
                }
            }
        }
    }
}
