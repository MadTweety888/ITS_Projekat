package com.example.wherekiddo.ui.screens.main.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.wherekiddo.R
import com.example.wherekiddo.domain.enums.UserTypeEnum.PARENT
import com.example.wherekiddo.domain.models.UserData
import com.example.wherekiddo.ui.navigation.Routes.TRACKING_SCREEN
import com.example.wherekiddo.ui.screens.main.home.HomeScreenViewModel.Events.MakeErrorToast
import com.example.wherekiddo.ui.screens.main.home.HomeScreenViewModel.Events.MakeGenericErrorToast
import com.example.wherekiddo.ui.screens.main.home.HomeScreenViewModel.Events.NavigateToTracking
import com.example.wherekiddo.ui.theme.spacing
import com.example.wherekiddo.ui.util.BoxWithBackgroundPattern
import com.example.wherekiddo.ui.util.PrimaryButton
import com.example.wherekiddo.ui.util.PrimaryOutlinedTextField
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun HomeScreen(
    navController: NavHostController
) {

    val viewModel = hiltViewModel<HomeScreenViewModel>()

    HomeScreenView(viewModel)

    IsLoadingState(viewModel)

    EventsHandler(navController, viewModel)
}

@Composable
private fun HomeScreenView(
    viewModel: HomeScreenViewModel
) {
    
    val focusManager = LocalFocusManager.current

    val userData by viewModel.userData.collectAsState()

    BoxWithBackgroundPattern(
        modifier = Modifier
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
    ) {

        userData?.let {

            if (it.userType == PARENT) {

                ParentHomeScreenView(viewModel)

            } else {

                DriverHomeScreenView(viewModel)
            }
        }
    }
}

//region Parent Screen Version

@Composable
private fun ParentHomeScreenView(
    viewModel: HomeScreenViewModel
) {

    val focusManager = LocalFocusManager.current

    val vehiclePlates by remember { viewModel.vehiclePlates }

    val driverData by remember { viewModel.driverData }

    val isTrackButtonEnabled by remember { viewModel.isButtonEnabled }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = stringResource(id = R.string.home_screen_tracked_vehicle_header),
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onBackground
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        PrimaryOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            textStateValue = vehiclePlates,
            onValueChange = { viewModel.onVehiclePlatesChanged(it) },
            label = stringResource(id = R.string.home_screen_vehicle_plates_label),
            imeAction = ImeAction.Done,
            onDone = {
                focusManager.clearFocus()
            },
            onTrailingIconClick = { viewModel.onVehiclePlatesChanged("") }
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.home_screen_track_button),
            enabled = isTrackButtonEnabled,
            onClick = {
                focusManager.clearFocus()
                viewModel.onTrackClicked()
            }
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        AnimatedVisibility(visible = driverData != null) {

            DriverDataCard(driverData, viewModel::onLocationClicked)
        }
    }
}

@Composable
private fun DriverDataCard(
    driverData: UserData?,
    onLocationClicked: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colors.surface)
            .padding(
                vertical = MaterialTheme.spacing.small,
                horizontal = MaterialTheme.spacing.medium
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        GlideImage(
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .size(120.dp),
            imageModel = { driverData?.photoUrl ?: "" },
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colors.primary
                )
            },
            failure = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.scale(2f),
                        imageVector = Icons.Outlined.ErrorOutline,
                        contentDescription = "Icon representing image fetch failure",
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
        )
        
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

        Column {

            Text(
                text = stringResource(id = R.string.home_screen_driver_name_label)
                        + " ${driverData?.name} ${driverData?.surname}",
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.onSurface
            )

            Text(
                text = stringResource(id = R.string.home_screen_driver_contact_label)
                        + " ${driverData?.email}",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                height = 42.dp,
                text = stringResource(id = R.string.home_screen_location_button),
                onClick = onLocationClicked
            )
        }
    }
}

//endregion

//region Driver Screen Version

@Composable
private fun DriverHomeScreenView(
    viewModel: HomeScreenViewModel
) {

    val focusManager = LocalFocusManager.current

    val vehiclePlates by remember { viewModel.vehiclePlates }

    val isDriveButtonEnabled by remember { viewModel.isButtonEnabled }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = stringResource(id = R.string.home_screen_driving_vehicle_header),
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onBackground
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        PrimaryOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            textStateValue = vehiclePlates,
            onValueChange = { viewModel.onVehiclePlatesChanged(it) },
            label = stringResource(id = R.string.home_screen_vehicle_plates_label),
            imeAction = ImeAction.Done,
            onDone = {
                focusManager.clearFocus()
            },
            onTrailingIconClick = { viewModel.onVehiclePlatesChanged("") }
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.home_screen_drive_button),
            enabled = isDriveButtonEnabled,
            onClick = {
                focusManager.clearFocus()
                viewModel.onDriveClicked()
            }
        )
    }
}

//endregion

@Composable
private fun IsLoadingState(
    viewModel: HomeScreenViewModel
) {
    val isLoading by remember { viewModel.isLoading }

    if (isLoading)
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

@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: HomeScreenViewModel
) {

    val context = LocalContext.current

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {

        when (event.value) {

            is NavigateToTracking -> {
                navController.popBackStack()
                navController.navigate(TRACKING_SCREEN)
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