package com.example.wherekiddo.ui.screens.main.tracking

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherekiddo.domain.enums.UserTypeEnum
import com.example.wherekiddo.domain.models.UserData
import com.example.wherekiddo.repository.interactors.AuthInteractor
import com.example.wherekiddo.repository.interactors.TrackingInteractor
import com.example.wherekiddo.repository.interactors.UsersDataInteractor
import com.example.wherekiddo.util.Resource
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackingScreenViewModel @Inject constructor(
    private val authRepository: AuthInteractor,
    private val userDataRepository: UsersDataInteractor,
    private val trackingRepository: TrackingInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData = _userData.asStateFlow()

    val trackedLocation = mutableStateOf<GeoPoint?>(null)

    val isDriving = mutableStateOf(false)

    val isLoading = mutableStateOf(false)

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

    fun startTracking() = viewModelScope.launch {

        trackingRepository
            .getVehicleLocation(_userData.value?.vehiclePlates ?: "")
            .collect { result ->

                when (result) {

                    is Resource.Success -> handleTrackingSuccess(result.data)
                    is Resource.Error   -> handleTrackingError(result.message)
                    is Resource.Loading -> handleTrackingLoading()
                }
            }
    }

    //region Tracking Helpers

    private fun handleTrackingSuccess(location: GeoPoint?) {

        location?.let { trackedLocation.value = location }
        isLoading.value = false
    }

    private fun handleTrackingError(error: String?) {

        error?.let { makeErrorToast(it) } ?: run { makeGenericErrorToast() }
        isLoading.value = false
    }

    private fun handleTrackingLoading() {

        isLoading.value = true
    }

    //endregion

    fun onSetPlatesClicked() {

        navigateToHomeScreen()
    }

    fun onStartDrivingClicked() {

        isDriving.value = true
    }

    fun onStopDrivingClicked() {

        isDriving.value = false
    }

    private fun navigateToHomeScreen() = viewModelScope.launch {
        events.emit(Events.NavigateToHome)
    }

    private fun makeErrorToast(error: String) = viewModelScope.launch {
        events.emit(Events.MakeErrorToast(error))
    }

    private fun makeGenericErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeGenericErrorToast)
    }

    fun clearEventChannel() = viewModelScope.launch {
        events.emit(null)
    }

    sealed class Events {
        data class MakeErrorToast(val error: String): Events()
        object MakeGenericErrorToast: Events()
        object NavigateToHome: Events()
    }
}