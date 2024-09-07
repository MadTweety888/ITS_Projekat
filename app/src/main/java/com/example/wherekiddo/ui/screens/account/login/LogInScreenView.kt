package com.example.wherekiddo.ui.screens.account.login

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.wherekiddo.R
import com.example.wherekiddo.ui.activities.MainActivity
import com.example.wherekiddo.ui.navigation.Routes.SIGN_UP_SCREEN
import com.example.wherekiddo.ui.screens.account.login.LogInScreenViewModel.Events.MakeLoginErrorToast
import com.example.wherekiddo.ui.screens.account.login.LogInScreenViewModel.Events.NavigateToHomeScreen
import com.example.wherekiddo.ui.screens.account.login.LogInScreenViewModel.Events.NavigateToSignUp
import com.example.wherekiddo.ui.theme.GreenValid
import com.example.wherekiddo.ui.theme.LinkColor
import com.example.wherekiddo.ui.theme.RedError
import com.example.wherekiddo.ui.theme.spacing
import com.example.wherekiddo.ui.util.BoxWithBackgroundPattern
import com.example.wherekiddo.ui.util.PrimaryButton
import com.example.wherekiddo.ui.util.PrimaryOutlinedTextField
import com.example.wherekiddo.util.extensions.findActivity
import com.example.wherekiddo.util.validateNotEmpty

@Composable
fun LogInScreen(
    navController: NavHostController
) {

    val viewModel = hiltViewModel<LogInScreenViewModel>()

    LogInScreenView(viewModel)

    IsLoadingState(viewModel)

    EventsHandler(navController, viewModel)
}

@Composable
private fun LogInScreenView(
    viewModel: LogInScreenViewModel
) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val emailFocusRequester = remember { FocusRequester() }
    val emailTextState by rememberSaveable { viewModel.emailTextState}
    val isErrorMessagePairEmail = validateNotEmpty(emailTextState, context)

    val passwordFocusRequester = remember { FocusRequester() }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val passwordTextState by rememberSaveable { viewModel.passwordTextState }
    val isErrorMessagePairPassword = validateNotEmpty(passwordTextState, context)

    val logInButtonEnabled = !isErrorMessagePairEmail.first && !isErrorMessagePairPassword.first

    BoxWithBackgroundPattern(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = MaterialTheme.spacing.extraLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.large),
                text = stringResource(id = R.string.log_in_screen_title),
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.onBackground
            )

            //region Email Text Field

            Column {

                PrimaryOutlinedTextField(
                    modifier = Modifier
                        .focusRequester(emailFocusRequester)
                        .padding(top = MaterialTheme.spacing.large),
                    textStateValue = emailTextState,
                    onValueChange = viewModel::onEmailTextChanged,
                    label = stringResource(id = R.string.log_in_screen_email_label),
                    isError = isErrorMessagePairEmail.first,
                    onNext = {
                        focusManager.clearFocus()
                        passwordFocusRequester.requestFocus()
                    },
                    onTrailingIconClick = { viewModel.onEmailTextChanged("") }
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.extraSmall),
                    text = isErrorMessagePairEmail.second,
                    color = if (isErrorMessagePairEmail.first) RedError else GreenValid
                )
            }

            //endregion

            //region Password Text Field

            Column {

                PrimaryOutlinedTextField(
                    modifier = Modifier
                        .focusRequester(passwordFocusRequester)
                        .padding(top = MaterialTheme.spacing.large),
                    textStateValue = passwordTextState,
                    onValueChange = viewModel::onPasswordTextChanged,
                    label = stringResource(id = R.string.log_in_screen_password_label),
                    isError = isErrorMessagePairPassword.first,
                    imeAction = ImeAction.Done,
                    onDone = {
                        focusManager.clearFocus()
                    },
                    visualTransformation =
                    if (isPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIconVector =
                    if (!isPasswordVisible)
                        Icons.Filled.Visibility
                    else
                        Icons.Filled.VisibilityOff,
                    onTrailingIconClick = { isPasswordVisible = !isPasswordVisible }
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.extraSmall),
                    text = isErrorMessagePairPassword.second,
                    color = if (isErrorMessagePairPassword.first) RedError else GreenValid
                )
            }

            //endregion

            //region Log In Button

            PrimaryButton(
                modifier = Modifier
                    .padding(top = MaterialTheme.spacing.extraLarge)
                    .width(150.dp),
                enabled = logInButtonEnabled,
                text = stringResource(id = R.string.log_in_screen_log_in_button),
                onClick = viewModel::onLogInClick
            )

            //endregion

            //region Switch To Sign Up

            Row(
                Modifier
                    .padding(top = MaterialTheme.spacing.large),
            ) {

                Text(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.extraSmall),
                    style = MaterialTheme.typography.body1,
                    text = stringResource(id = R.string.log_in_screen_dont_have_an_account),
                    color = MaterialTheme.colors.onBackground
                )

                Text(
                    modifier = Modifier
                        .clickable { viewModel.navigateToSignUp() }
                        .padding(horizontal = MaterialTheme.spacing.extraSmall),
                    style = MaterialTheme.typography.body1,
                    text = stringResource(id = R.string.log_in_screen_dont_have_an_account_press_here),
                    color = LinkColor
                )
            }

            //endregion
        }
    }
}

@Composable
private fun IsLoadingState(
    viewModel: LogInScreenViewModel
) {

    val isLoading by remember { viewModel.isLoading }

    if (isLoading) {

        BoxWithBackgroundPattern {

            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: LogInScreenViewModel
) {

    val context = LocalContext.current

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {

        when (event.value) {

            NavigateToHomeScreen -> {
                context.startActivity(Intent(context, MainActivity::class.java))
                context.findActivity()?.finish()
            }
            NavigateToSignUp -> {
                navController.popBackStack()
                navController.navigate(SIGN_UP_SCREEN)
            }
            MakeLoginErrorToast -> {
                Toast.makeText(context, context.getText(R.string.error_invalid_login), Toast.LENGTH_SHORT).show()
                viewModel.clearEventChannel()
            }
            else -> {}
        }
    }
}