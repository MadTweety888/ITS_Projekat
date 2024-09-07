package com.example.wherekiddo.ui.screens.main.home

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherekiddo.domain.models.UserData
import com.example.wherekiddo.repository.interactors.AuthInteractor
import com.example.wherekiddo.repository.interactors.UsersDataInteractor
import com.example.wherekiddo.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val authRepository: AuthInteractor,
    private val userDataRepository: UsersDataInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData = _userData.asStateFlow()

    val vehiclePlates = mutableStateOf("")

    val isButtonEnabled = derivedStateOf { vehiclePlates.value.isNotBlank() }

    val driverData = mutableStateOf<UserData?>(null)

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
                vehiclePlates.value = userData?.vehiclePlates ?: ""
            }
        )
    }

    fun onVehiclePlatesChanged(plates: String) {

        vehiclePlates.value = plates
    }

    fun onTrackClicked() {

        fetchDriverData()
    }

    private fun fetchDriverData() = viewModelScope.launch {

        userDataRepository.getDriverData(vehiclePlates.value).collect { result ->

            when (result) {

                is Resource.Success -> handleFetchDriverDataSuccess(result.data)
                is Resource.Error   -> handleFetchDriverDataError(result.message)
                is Resource.Loading -> handleFetchDriverDataLoading()
            }
        }
    }

    //region Fetch Driver Data Helpers

    private fun handleFetchDriverDataSuccess(data: UserData?) {

        driverData.value = data
        isLoading.value = false

        try {

            if(authRepository.hasUser()) setParentVehiclePlates(authRepository.getUserId())

        } catch(e: Exception) {

            makeGenericErrorToast()
            e.printStackTrace()
        }
    }

    private fun handleFetchDriverDataError(error: String?) {

        driverData.value = null
        error?.let { makeErrorToast(it) } ?: run { makeGenericErrorToast() }
        isLoading.value = false
    }

    private fun handleFetchDriverDataLoading() {

        isLoading.value = true
    }

    private fun setParentVehiclePlates(userId: String) = viewModelScope.launch {

        userDataRepository.setUserVehiclePlates(
            userId = userId,
            vehiclePlates = vehiclePlates.value,
            onComplete = { isSuccessful ->

                if (!isSuccessful) makeErrorToast("Error saving vehicle plates!")
            }
        )
    }

    //endregion

    fun onLocationClicked() {

        navigateToTrackingScreen()
    }

    fun onDriveClicked() {

        isLoading.value = true

        if (vehiclePlates.value == userData.value?.vehiclePlates) {

            isLoading.value = false
            navigateToTrackingScreen()

        } else {

            try {

                if(authRepository.hasUser()) setDriverVehiclePlates(authRepository.getUserId())

            } catch(e: Exception) {

                isLoading.value = false
                makeGenericErrorToast()
                e.printStackTrace()
            }
        }
    }

    private fun setDriverVehiclePlates(userId: String) = viewModelScope.launch {

        userDataRepository.setUserVehiclePlates(
            userId = userId,
            vehiclePlates = vehiclePlates.value,
            onComplete = { isSuccessful ->

                if (isSuccessful) {

                    isLoading.value = false
                    navigateToTrackingScreen()

                } else {

                    isLoading.value = false
                    makeErrorToast("Error saving vehicle plates!")
                }
            }
        )
    }

    private fun navigateToTrackingScreen() = viewModelScope.launch {
        events.emit(Events.NavigateToTracking)
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
        object NavigateToTracking: Events()
    }
}