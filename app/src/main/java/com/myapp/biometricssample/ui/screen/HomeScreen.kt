package com.myapp.biometricssample.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun HomeScreen() {
    HomeContent()
}

/**
 * 認証完了後の画面
 *
 */
@Composable
fun HomeContent() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "ホーム",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )
        Text(
            text = "認証完了",
            modifier = Modifier.align(Alignment.Center)
        )
    }

}