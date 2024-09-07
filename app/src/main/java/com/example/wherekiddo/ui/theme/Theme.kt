package com.example.wherekiddo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColors(
    primary = PrimaryColor,
    primaryVariant = Purple700,
    secondary = Teal200,
    background = BackgroundColor,
    onBackground = OnBackgroundColor,
    surface = SurfaceColor,
    onSurface = OnSurfaceColor
)

private val LightColorScheme = lightColors(
    primary = PrimaryColor,
    primaryVariant = Purple700,
    secondary = Teal200,
    background = BackgroundColor,
    onBackground = OnBackgroundColor,
    surface = SurfaceColor,
    onSurface = OnSurfaceColor
)

@Composable
fun WhereKiddoTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {

    val colors = if (darkTheme) {

        DarkColorScheme

    } else {

        LightColorScheme
    }

    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = colors.background,
            darkIcons = false
        )
    }

    CompositionLocalProvider(
        LocalSpacing provides Spacing(),
    ) {

        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }

    /*MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )*/
}