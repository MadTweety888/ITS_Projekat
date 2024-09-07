package com.example.wherekiddo.ui.screens.account.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.wherekiddo.R
import com.example.wherekiddo.ui.navigation.Routes.LOG_IN_SCREEN
import com.example.wherekiddo.ui.navigation.Routes.SIGN_UP_SCREEN
import com.example.wherekiddo.ui.screens.account.onboarding.OnBoardingScreenViewModel.Events.NavigateToLogIn
import com.example.wherekiddo.ui.screens.account.onboarding.OnBoardingScreenViewModel.Events.NavigateToSignUp
import com.example.wherekiddo.ui.theme.spacing
import com.example.wherekiddo.ui.util.BoxWithBackgroundPattern
import com.example.wherekiddo.ui.util.PrimaryButton
import com.example.wherekiddo.ui.util.PrimaryLogoImage

@Composable
fun OnBoardingScreen(
    navController: NavHostController
) {

    val viewModel = hiltViewModel<OnBoardingScreenViewModel>()

    EventsHandler(navController, viewModel)

    OnBoardingScreenView(viewModel)
}

@Composable
private fun OnBoardingScreenView(
    viewModel: OnBoardingScreenViewModel
) {

    BoxWithBackgroundPattern {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LogoImageWithSlogan()

            NavigationButtonsRow(viewModel)
        }
    }
}

@Composable
private fun ColumnScope.LogoImageWithSlogan() {

    Column(
        modifier = Modifier.weight(2f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        PrimaryLogoImage()
    }
}

@Composable
private fun ColumnScope.NavigationButtonsRow(
    viewModel: OnBoardingScreenViewModel
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
    ) {

        PrimaryButton(
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = MaterialTheme.spacing.large,
                    end = MaterialTheme.spacing.medium
                ),
            text = stringResource(id = R.string.on_boarding_screen_log_in_button),
            onClick = viewModel::navigateToLogIn
        )

        PrimaryButton(
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.large
                ),
            text = stringResource(id = R.string.on_boarding_screen_sign_up_button),
            onClick = viewModel::navigateToSignUp
        )
    }
}

@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: OnBoardingScreenViewModel
) {

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {

        when (event.value) {

            NavigateToLogIn  -> { navController.navigate(LOG_IN_SCREEN) }
            NavigateToSignUp -> { navController.navigate(SIGN_UP_SCREEN) }
            else             -> {}
        }
    }
}