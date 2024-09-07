@file:OptIn(ExperimentalAnimationApi::class)

package com.example.wherekiddo.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.wherekiddo.R
import com.example.wherekiddo.ui.activities.MainActivityViewModel
import com.example.wherekiddo.ui.activities.MainActivityViewModel.Events.NavigateToHome
import com.example.wherekiddo.ui.activities.MainActivityViewModel.Events.NavigateToProfile
import com.example.wherekiddo.ui.activities.MainActivityViewModel.Events.NavigateToTracking
import com.example.wherekiddo.ui.navigation.Routes.HOME_SCREEN
import com.example.wherekiddo.ui.navigation.Routes.PROFILE_SCREEN
import com.example.wherekiddo.ui.navigation.Routes.TRACKING_SCREEN
import com.example.wherekiddo.ui.screens.main.home.HomeScreen
import com.example.wherekiddo.ui.screens.main.profile.ProfileScreen
import com.example.wherekiddo.ui.screens.main.tracking.TrackingScreen
import com.example.wherekiddo.ui.util.bottomnav.BottomNavBar
import com.example.wherekiddo.ui.util.bottomnav.BottomNavItem
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@Composable
fun MainActivityLayoutAndNavigation(
    viewModel: MainActivityViewModel
) {

    val navController = rememberAnimatedNavController()

    NavHostAndBottomNavigation(navController, viewModel)

    EventsHandler(navController, viewModel)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun NavHostAndBottomNavigation(
    navController: NavHostController,
    viewModel: MainActivityViewModel
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavBarVisibilityState = rememberSaveable { viewModel.bottomNavBarVisibilityState }

    viewModel.setBottomNavBarVisibilityState(currentRoute)

    val bottomNavBarItems = listOf(
        BottomNavItem(
            name = stringResource(R.string.bottom_nav_bar_home),
            route = HOME_SCREEN,
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            name = stringResource(R.string.bottom_nav_bar_tracking),
            route = TRACKING_SCREEN,
            icon = Icons.Default.LocationOn
        ),
        BottomNavItem(
            name = stringResource(R.string.bottom_nav_bar_profile),
            route = PROFILE_SCREEN,
            icon = Icons.Default.Person
        )
    )

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                items = bottomNavBarItems,
                bottomBarState = bottomNavBarVisibilityState.value,
                onItemClick = { viewModel.onBottomNavItemClicked(it.route) }
            )
        }
    ) {

        AnimatedNavigation(navController)
    }
}

@Composable
private fun AnimatedNavigation(
    navController: NavHostController
) {

    AnimatedNavHost(
        navController = navController,
        startDestination = HOME_SCREEN,
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
            route = HOME_SCREEN
        ) {
            HomeScreen(navController)
        }

        composable(
            route = PROFILE_SCREEN
        ) {
            ProfileScreen(navController)
        }

        composable(
            route = TRACKING_SCREEN
        ) {
            TrackingScreen(navController)
        }
    }
}

@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: MainActivityViewModel
) {

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {

        when (event.value) {

            NavigateToHome -> {
                navController.popBackStack()
                navController.navigate(HOME_SCREEN)
                viewModel.clearEventChannel()
            }
            NavigateToProfile -> {
                navController.popBackStack()
                navController.navigate(PROFILE_SCREEN)
                viewModel.clearEventChannel()
            }
            NavigateToTracking -> {
                navController.popBackStack()
                navController.navigate(TRACKING_SCREEN)
                viewModel.clearEventChannel()
            }
            else -> {}
        }
    }
}