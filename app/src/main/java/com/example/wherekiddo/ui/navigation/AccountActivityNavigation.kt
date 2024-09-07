@file:OptIn(ExperimentalAnimationApi::class)

package com.example.wherekiddo.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import com.example.wherekiddo.ui.screens.account.login.LogInScreen
import com.example.wherekiddo.ui.screens.account.onboarding.OnBoardingScreen
import com.example.wherekiddo.ui.screens.account.signup.SignUpScreen
import com.example.wherekiddo.ui.screens.account.splash.SplashScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@Composable
fun AccountActivityNavigation() {

    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = Routes.SPLASH_SCREEN,
        enterTransition = {
            fadeIn(
                initialAlpha = 0.1f,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutLinearInEasing
                )
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutLinearInEasing
                )
            )
        }
    ) {

        composable(
            route = Routes.SPLASH_SCREEN
        ) {
            SplashScreen(navController)
        }

        composable(
            route = Routes.ON_BOARDING_SCREEN
        ) {
            OnBoardingScreen(navController)
        }

        composable(
            route = Routes.LOG_IN_SCREEN
        ) {
            LogInScreen(navController)
        }

        composable(
            route = Routes.SIGN_UP_SCREEN
        ) {
            SignUpScreen(navController)
        }
    }
}