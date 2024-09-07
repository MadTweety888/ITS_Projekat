package com.example.wherekiddo.ui.screens.account.splash

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.wherekiddo.R
import com.example.wherekiddo.ui.activities.MainActivity
import com.example.wherekiddo.ui.navigation.Routes.ON_BOARDING_SCREEN
import com.example.wherekiddo.ui.screens.account.splash.SplashScreenViewModel.Events.NavigateToHome
import com.example.wherekiddo.ui.screens.account.splash.SplashScreenViewModel.Events.NavigateToOnBoarding
import com.example.wherekiddo.ui.util.BoxWithBackgroundPattern
import com.example.wherekiddo.util.extensions.findActivity
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController
) {

    val viewModel = hiltViewModel<SplashScreenViewModel>()

    SplashScreenView(viewModel)

    EventsHandler(navController, viewModel)
}

@Composable
private fun SplashScreenView(
    viewModel: SplashScreenViewModel
) {

    val interactionSource = remember { MutableInteractionSource() }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.bus_lottie))

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    LaunchedEffect(key1 = Unit) {

        delay(2500)

        viewModel.onEndOfAnimation()
    }

    BoxWithBackgroundPattern(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                viewModel.onEndOfAnimation()
            }
    ) {

        LottieAnimation(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.Center),
            composition = composition,
            progress = { progress }
        )
    }
}

@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: SplashScreenViewModel
) {

    val context = LocalContext.current

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {

        when (event.value) {

            NavigateToOnBoarding -> {
                navController.popBackStack()
                navController.navigate(ON_BOARDING_SCREEN)
            }

            NavigateToHome -> {
                context.startActivity(Intent(context, MainActivity::class.java))
                context.findActivity()?.finish()
            }

            else -> {}
        }
    }
}