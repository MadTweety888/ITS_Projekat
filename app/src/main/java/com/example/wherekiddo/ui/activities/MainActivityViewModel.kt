package com.example.wherekiddo.ui.activities

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherekiddo.ui.navigation.Routes.HOME_SCREEN
import com.example.wherekiddo.ui.navigation.Routes.PROFILE_SCREEN
import com.example.wherekiddo.ui.navigation.Routes.TRACKING_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val bottomNavBarVisibilityState = (mutableStateOf(false))

    fun setBottomNavBarVisibilityState(currentRoute: String?) = viewModelScope.launch {

        bottomNavBarVisibilityState.value = currentRoute in listOf(
            HOME_SCREEN,
            TRACKING_SCREEN,
            PROFILE_SCREEN
        )
    }

    fun onBottomNavItemClicked(itemRoute: String) {

        when (itemRoute) {

            HOME_SCREEN        -> navigateToHome()
            PROFILE_SCREEN     -> navigateToProfile()
            TRACKING_SCREEN    -> navigateToTracking()
        }
    }

    //region Event Helpers

    private fun navigateToHome() = viewModelScope.launch {

        events.emit(Events.NavigateToHome)
    }

    private fun navigateToProfile() = viewModelScope.launch {

        events.emit(Events.NavigateToProfile)
    }

    private fun navigateToTracking() = viewModelScope.launch {

        events.emit(Events.NavigateToTracking)
    }

    fun clearEventChannel() = viewModelScope.launch {

        events.emit(null)
    }

    //endregion

    sealed class Events {

        object NavigateToHome: Events()
        object NavigateToTracking: Events()
        object NavigateToProfile: Events()
    }
}