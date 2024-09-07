package com.example.wherekiddo.ui.screens.account.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingScreenViewModel @Inject constructor(

): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    fun navigateToLogIn() = viewModelScope.launch {
        events.emit(Events.NavigateToLogIn)
    }

    fun navigateToSignUp() = viewModelScope.launch {
        events.emit(Events.NavigateToSignUp)
    }

    sealed class Events {

        object NavigateToLogIn: Events()
        object NavigateToSignUp: Events()
    }
}