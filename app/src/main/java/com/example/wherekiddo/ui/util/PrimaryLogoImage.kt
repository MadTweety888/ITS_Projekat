package com.example.wherekiddo.ui.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.wherekiddo.R
import com.example.wherekiddo.ui.theme.BackgroundPatternColor
import com.example.wherekiddo.ui.theme.WhereKiddoTheme

@Composable
fun PrimaryLogoImage(
    modifier: Modifier = Modifier,
    width: Dp = 230.dp,
    height: Dp = 200.dp
) {

    Image(
        modifier = modifier
            .width(width)
            .height(height)
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    listOf(
                        BackgroundPatternColor,
                        Color.Transparent,
                        BackgroundPatternColor
                    )
                ),
                shape = MaterialTheme.shapes.small
            )
            .clip(MaterialTheme.shapes.small),
        contentScale = ContentScale.FillBounds,
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Splash screen logo"
    )
}

@Preview
@Composable
private fun LogoImagePreview() {

    WhereKiddoTheme {

        PrimaryLogoImage()
    }
}