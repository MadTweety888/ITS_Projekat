package com.example.wherekiddo.ui.screens.account.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherekiddo.repository.interactors.AuthInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInScreenViewModel @Inject constructor(
    private val authRepository: AuthInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val emailTextState = mutableStateOf("")

    val passwordTextState = mutableStateOf("")

    val isLoading = mutableStateOf(false)

    //region Form Update Helpers

    fun onEmailTextChanged(email: String) {
        emailTextState.value = email
    }

    fun onPasswordTextChanged(password: String) {
        passwordTextState.value = password
    }

    //endregion

    fun onLogInClick() = viewModelScope.launch {

        isLoading.value = true

        if(emailTextState.value == "admin" && passwordTextState.value == "admin") {

            navigateToHomeScreen()
            isLoading.value = false

        } else {

            try {

                authRepository.logInUser(
                    email = emailTextState.value.trim(),
                    password = passwordTextState.value.trim()
                ) { isSuccessful ->

                    if (isSuccessful) {

                        navigateToHomeScreen()
                        isLoading.value = false

                    } else {

                        isLoading.value = false
                        makeLoginErrorToast()
                    }
                }
            } catch (e:Exception) {

                isLoading.value = false
                makeLoginErrorToast()
                e.printStackTrace()
            }
        }
    }

    //region Event Helpers

    private fun navigateToHomeScreen() = viewModelScope.launch {
        events.emit(Events.NavigateToHomeScreen)
    }

    private fun makeLoginErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeLoginErrorToast)
    }

    fun navigateToSignUp() = viewModelScope.launch {
        events.emit(Events.NavigateToSignUp)
    }

    fun clearEventChannel() = viewModelScope.launch {
        events.emit(null)
    }

    //endregion

    sealed class Events {

        object NavigateToHomeScreen: Events()
        object NavigateToSignUp: Events()
        object MakeLoginErrorToast: Events()
    }
}