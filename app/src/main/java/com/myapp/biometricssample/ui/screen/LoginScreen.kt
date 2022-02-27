package com.myapp.biometricssample.ui.screen

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import com.myapp.biometricssample.ui.Screens

private const val TAG = "LoginScreen"

@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    // 生体認証実施
    val biometricAction = {
        if (checkBiometric(context)) {
            val biometricPrompt = createBiometricPrompt(context, navController)
            showId(biometricPrompt)
        }
    }

    LoginContent(biometricAction)
}

/**
 * 生体認証画面
 *
 * @param biometricAction　生体認証アクション
 */
@Composable
fun LoginContent(biometricAction: () -> Unit) {

    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "生体認証サンプル",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )
        Button(
            onClick = { biometricAction() },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(text = "認証")
        }
    }
}

//====================
//　生体認証間関連
// ①　checkBiometricで使用できるかチェック
// ②　createBiometricPromptで設定
// ③　showIdで生体認証実施
//====================

// 端末が生体認証に対応しているか確認
private fun checkBiometric(context: Context): Boolean {
    val biometricManager = BiometricManager.from(context)
    val authenticate = if(Build.VERSION.SDK_INT < 30) {
        biometricManager.canAuthenticate()
    } else {
        biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
    }
    return when (authenticate) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            Log.d(TAG, "checkBiometric::生体認証利用可能")
            true
        }
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            Log.e(TAG, "checkBiometric::この端末では生体認証は利用できません")
            false
        }
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            Log.e(TAG, "checkBiometric::ユーザーには生体認証が登録されていません。")
            val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                )
            }
            context.startActivity(enrollIntent)
            false
        }
        else -> {
            Log.e("MainActivity", "Nothing supported")
            false
        }
    }
}

// 生体認証設定
private fun createBiometricPrompt(context: Context, navController: NavHostController): BiometricPrompt {
    val executor = ContextCompat.getMainExecutor(context)
    val callback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            Toast.makeText(
                context,
                "Authentication error: $errString", Toast.LENGTH_SHORT
            ).show()
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            Toast.makeText(
                context, "Authentication failed",
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            Toast.makeText(
                context,
                "Authentication succeeded!", Toast.LENGTH_SHORT
            ).show()
            navController.navigate(Screens.Home.route)
        }
    }
    return BiometricPrompt(context as FragmentActivity, executor, callback)
}

// 生体認証表示
fun showId(biometricPrompt: BiometricPrompt) {
    val promptInfo = if (Build.VERSION.SDK_INT < 30) {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("指紋認証")
            .setSubtitle("サンプルです")
            .setConfirmationRequired(true)
            .setDeviceCredentialAllowed(true)
            .build()
    } else {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("指紋認証")
            .setSubtitle("サンプルです")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL or
                        BiometricManager.Authenticators.BIOMETRIC_WEAK
            )
            .setConfirmationRequired(false)
            .build()
    }
    biometricPrompt.authenticate(promptInfo)
}