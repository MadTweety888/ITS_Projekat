package com.example.wherekiddo.ui.screens.main.tracking

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.wherekiddo.R
import com.example.wherekiddo.domain.enums.UserTypeEnum.PARENT
import com.example.wherekiddo.domain.models.UserData
import com.example.wherekiddo.ui.navigation.Routes.HOME_SCREEN
import com.example.wherekiddo.ui.screens.main.tracking.TrackingScreenViewModel.Events.MakeErrorToast
import com.example.wherekiddo.ui.screens.main.tracking.TrackingScreenViewModel.Events.MakeGenericErrorToast
import com.example.wherekiddo.ui.screens.main.tracking.TrackingScreenViewModel.Events.NavigateToHome
import com.example.wherekiddo.ui.theme.spacing
import com.example.wherekiddo.ui.util.BoxWithBackgroundPattern
import com.example.wherekiddo.ui.util.ComponentSizes
import com.example.wherekiddo.ui.util.IntentExtras.VEHICLE_PLATES
import com.example.wherekiddo.ui.util.PrimaryButton
import com.example.wherekiddo.util.extensions.toLatLng
import com.example.wherekiddo.util.service.LocationService
import com.example.wherekiddo.util.service.LocationService.Companion.ACTION_START
import com.example.wherekiddo.util.service.LocationService.Companion.ACTION_STOP
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ORANGE
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun TrackingScreen(
    navController: NavHostController
) {

    val viewModel = hiltViewModel<TrackingScreenViewModel>()

    TrackingScreenView(viewModel)

    IsLoadingState(viewModel)

    EventsHandler(navController, viewModel)
}

@Composable
private fun TrackingScreenView(
    viewModel: TrackingScreenViewModel
) {

    val userData by viewModel.userData.collectAsState()

    BoxWithBackgroundPattern {

        if (userData?.vehiclePlates.isNullOrBlank()) {

            VehiclePlatesNotSetView(
                isParent = userData?.userType == PARENT,
                onSetPlatesClicked = viewModel::onSetPlatesClicked
            )

        } else {

            userData?.let { TrackingMapView(viewModel,it) }
        }
    }
}

@Composable
private fun TrackingMapView(
    viewModel: TrackingScreenViewModel,
    userData: UserData
) {

    if (userData.userType == PARENT) {

        TrackingMapViewParent(viewModel)

    } else {

        TrackingMapViewDriver(viewModel, userData.vehiclePlates)
    }
}

@Composable
private fun TrackingMapViewParent(
    viewModel: TrackingScreenViewModel
) {

    val trackedLocation by remember { viewModel.trackedLocation }

    LaunchedEffect(key1 = Unit) {

        viewModel.startTracking()
    }

    Box(
        modifier = Modifier.fillMaxSize().padding(bottom = ComponentSizes.bottomNavBarHeight.dp),
        contentAlignment = Alignment.Center
    ) {

        trackedLocation?.let { location ->

            val mapProperties = MapProperties(isMyLocationEnabled = false)
            val cameraPositionState = rememberCameraPositionState()

            LaunchedEffect(key1 = location) {

                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLngZoom(
                        location.toLatLng(),
                        15f
                    )
                )
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                properties = mapProperties,
                cameraPositionState = cameraPositionState
            ) {

                Marker(
                    state = MarkerState(position = location.toLatLng()),
                    title = "Bus",
                    icon = BitmapDescriptorFactory.defaultMarker(HUE_ORANGE)
                )
            }

        } ?: run {

            Text(
                text = "Vehicle Location Unknown!"
            )
        }
    }
}

@Composable
private fun TrackingMapViewDriver(
    viewModel: TrackingScreenViewModel,
    vehiclePlates: String
) {

    val isDriving by remember { viewModel.isDriving }

    val trackedLocation by remember { viewModel.trackedLocation }

    LaunchedEffect(key1 = isDriving) {

        if (isDriving) viewModel.startTracking()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.medium)
            .padding(bottom = ComponentSizes.bottomNavBarHeight.dp)
    ) {

        trackedLocation?.let {

            val mapProperties = MapProperties(isMyLocationEnabled = true)
            val cameraPositionState = rememberCameraPositionState()

            AnimatedVisibility(visible = isDriving) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    ) {

                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            properties = mapProperties,
                            cameraPositionState = cameraPositionState
                        )

                        LaunchedEffect(key1 = it) {

                            cameraPositionState.animate(
                                update = CameraUpdateFactory.newLatLngZoom(
                                    it.toLatLng(),
                                    15f
                                )
                            )
                        }
                    }


                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    StopDrivingButton(viewModel::onStopDrivingClicked)

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                }
            }
        } ?: run {

            AnimatedVisibility(visible = isDriving) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {

                    Spacer(modifier = Modifier.weight(0.5f))

                    StopDrivingButton(viewModel::onStopDrivingClicked)

                    Spacer(modifier = Modifier.weight(0.5f))
                }
            }
        }

        AnimatedVisibility(visible = !isDriving) {

            Column(
                modifier = Modifier.fillMaxSize(),
            ) {

                Spacer(modifier = Modifier.weight(0.5f))

                StartDrivingButton(vehiclePlates, viewModel::onStartDrivingClicked)

                Spacer(modifier = Modifier.weight(0.5f))
            }

        }
    }
}

@Composable
private fun StopDrivingButton(
    onClick: () -> Unit
) {

    val context = LocalContext.current

    PrimaryButton(
        modifier = Modifier.fillMaxWidth(),
        text = "Stop Driving",
        onClick = {

            onClick()

            Intent(context, LocationService::class.java).apply {

                action = ACTION_STOP
                context.startService(this)
            }
        }
    )
}

@Composable
private fun StartDrivingButton(
    vehiclePlates: String,
    onClick: () -> Unit
) {

    val context = LocalContext.current

    PrimaryButton(
        modifier = Modifier.fillMaxWidth(),
        text = "Start Driving",
        onClick = {

            onClick()

            Intent(context, LocationService::class.java).apply {

                putExtra(VEHICLE_PLATES, vehiclePlates)
                action = ACTION_START
                context.startService(this)
            }
        }
    )
}

@Composable
private fun VehiclePlatesNotSetView(
    isParent: Boolean,
    onSetPlatesClicked: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = if (isParent) {

                stringResource(id = R.string.tracking_screen_no_plates_set_parent)

            } else {

                stringResource(id = R.string.tracking_screen_no_plates_driver)
            },
            style = MaterialTheme.typography.caption
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.tracking_screen_set_plates_button),
            onClick = onSetPlatesClicked
        )
    }
}

@Composable
private fun IsLoadingState(
    viewModel: TrackingScreenViewModel
) {
    val isLoading by remember { viewModel.isLoading }

    if (isLoading) {

        BoxWithBackgroundPattern(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {

            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}

@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: TrackingScreenViewModel
) {

    val context = LocalContext.current

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {

        when (event.value) {

            is NavigateToHome -> {

                navController.popBackStack()
                navController.navigate(HOME_SCREEN)
            }
            is MakeErrorToast -> {
                Toast.makeText(context, (event.value as MakeErrorToast).error, Toast.LENGTH_SHORT).show()
                viewModel.clearEventChannel()
            }
            MakeGenericErrorToast -> {
                Toast.makeText(context, context.getText(R.string.error_generic), Toast.LENGTH_SHORT).show()
                viewModel.clearEventChannel()
            }
            else -> {}
        }
    }
}