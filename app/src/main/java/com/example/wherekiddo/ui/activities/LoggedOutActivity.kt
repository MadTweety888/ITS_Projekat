package com.example.wherekiddo.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.wherekiddo.ui.navigation.AccountActivityNavigation
import com.example.wherekiddo.ui.theme.WhereKiddoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleNavigationBugOnXiaomiDevices()

        setContent {

            WhereKiddoTheme {

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(androidx.compose.material.MaterialTheme.colors.background)
                ) {

                    AccountActivityNavigation()
                }
            }
        }
    }

    private fun handleNavigationBugOnXiaomiDevices() {
        window.decorView.post {
            window.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }
}