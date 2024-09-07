package com.example.wherekiddo.ui.screens.main.profile

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherekiddo.domain.models.UserData
import com.example.wherekiddo.repository.interactors.AuthInteractor
import com.example.wherekiddo.repository.interactors.UsersDataInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val authRepository: AuthInteractor,
    private val userDataRepository: UsersDataInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData = _userData.asStateFlow()

    private val _isUploading = MutableStateFlow(false)
    val isUploading = _isUploading.asStateFlow()

    var isLoading = mutableStateOf(false)

    init {
        viewModelScope.launch {
            try {
                if(authRepository.hasUser()) {
                    loadUserData(authRepository.getUserId())
                }
            } catch(e: Exception) {
                makeGenericErrorToast()
                e.printStackTrace()
            }
        }
    }

    private fun loadUserData(userId: String) = viewModelScope.launch {

        isLoading.value = true

        userDataRepository.getUserData(
            userId = userId,
            onError = {
                isLoading.value = false
                makeGenericErrorToast()
                it?.printStackTrace()
            },
            onSuccess = { userData ->
                isLoading.value = false
                _userData.value = userData
            }
        )
    }

    //region Profile Photo Logic

    fun onProfilePhotoClicked(photoUri: Uri?) = viewModelScope.launch {

        if (photoUri != null) {

            uploadUserPhoto(authRepository.getUserId(), photoUri)

        } else {

            makeGenericErrorToast()
            Timber.i("Logging: Error with photo picker")
        }
    }

    private fun uploadUserPhoto(userId: String, photoUri: Uri) = viewModelScope.launch {

        _isUploading.value = true

        userDataRepository.uploadUserPhoto(
            userId = userId,
            photoUri = photoUri,
            onError = {
                _isUploading.value = false
                makeGenericErrorToast()
                it?.printStackTrace()
            },
            onSuccess = {
                makeSuccessfullyChangedProfilePictureToast()
                loadUserData(userId)
                _isUploading.value = false
            }
        )
    }

    //endregion

    //region Log Out Logic

    fun onLogOut() = viewModelScope.launch {
        try {
            authRepository.logOutUser()
            events.emit(Events.NavigateToSplashScreen)
        } catch (e: Exception) {
            makeLogOutErrorToast()
            e.printStackTrace()
        }
    }

    //endregion

    //region Events Helpers

    private fun makeSuccessfullyChangedProfilePictureToast() = viewModelScope.launch {
        events.emit(Events.MakeSuccessfullyChangedProfilePictureToast)
    }

    private fun makeGenericErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeGenericErrorToast)
    }

    private fun makeLogOutErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeLogOutErrorToast)
    }

    fun clearEventChannel() = viewModelScope.launch {
        events.emit(null)
    }

    //endregion

    sealed class Events {
        object NavigateToSplashScreen: Events()
        object MakeLogOutErrorToast: Events()
        object MakeGenericErrorToast: Events()
        object MakeSuccessfullyChangedProfilePictureToast: Events()
    }
}