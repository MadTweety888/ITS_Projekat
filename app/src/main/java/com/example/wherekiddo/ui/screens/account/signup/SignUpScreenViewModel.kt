package com.example.wherekiddo.ui.screens.account.signup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherekiddo.domain.enums.UserTypeEnum
import com.example.wherekiddo.domain.enums.UserTypeEnum.DRIVER
import com.example.wherekiddo.domain.enums.UserTypeEnum.PARENT
import com.example.wherekiddo.repository.interactors.AuthInteractor
import com.example.wherekiddo.repository.interactors.UsersDataInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    private val authRepository: AuthInteractor,
    private val usersDataRepository: UsersDataInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val nameTextState = mutableStateOf("")

    val surnameTextState = mutableStateOf("")

    val emailTextState = mutableStateOf("")

    val passwordTextState = mutableStateOf("")

    val repeatPasswordTextState = mutableStateOf("")

    val selectedUserType = mutableStateOf<UserTypeEnum?>(null)

    val isLoading = mutableStateOf(false)

    //region Form Update Helpers

    fun onNameTextChanged(name: String) {
        nameTextState.value = name
    }

    fun onSurnameTextChanged(surname: String) {
        surnameTextState.value = surname
    }

    fun onEmailTextChanged(email: String) {
        emailTextState.value = email
    }

    fun onPasswordTextChanged(password: String) {
        passwordTextState.value = password
    }

    fun onRepeatPasswordTextChanged(repeatPassword: String) {
        repeatPasswordTextState.value = repeatPassword
    }

    fun onParentClicked() {

        selectedUserType.value = if (selectedUserType.value == PARENT) null else PARENT
    }

    fun onDriverClicked() {

        selectedUserType.value = if (selectedUserType.value == DRIVER) null else DRIVER
    }

    //endregion

    fun onSignUpClick() = viewModelScope.launch {

        isLoading.value = true

        try {

            authRepository.signUpUser(
                email = emailTextState.value.trim(),
                password = passwordTextState.value.trim()
            ) { isSuccessful ->

                if (isSuccessful) {

                    usersDataRepository.addUserData(
                        userId = authRepository.getUserId(),
                        name = nameTextState.value.trim(),
                        surname = surnameTextState.value.trim(),
                        email = emailTextState.value.trim(),
                        userType = selectedUserType.value ?: return@signUpUser
                    ) { isSuccessfulUserData ->

                        if (isSuccessfulUserData) {

                            navigateToHomeScreen()

                        } else {

                            makeSignUpErrorToast()
                        }
                    }

                } else {

                    makeSignUpErrorToast()
                }
            }
        } catch (e:Exception) {

            makeSignUpErrorToast()
            e.printStackTrace()

        } finally {

            isLoading.value = false
        }
    }

    //region Event Helpers

    private fun makeSignUpErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeSignupErrorToast)
    }

    private fun navigateToHomeScreen() = viewModelScope.launch {
        events.emit(Events.NavigateToHomeScreen)
    }

    fun navigateToLogIn() = viewModelScope.launch {
        events.emit(Events.NavigateToLogIn)
    }

    fun clearEventChannel() = viewModelScope.launch {
        events.emit(null)
    }

    //endregion

    sealed class Events {

        object NavigateToHomeScreen: Events()
        object NavigateToLogIn: Events()
        object MakeSignupErrorToast: Events()
    }
}