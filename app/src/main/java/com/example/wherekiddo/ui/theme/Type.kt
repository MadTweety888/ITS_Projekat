package com.example.wherekiddo.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight.Companion.Black
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraLight
import androidx.compose.ui.text.font.FontWeight.Companion.Light
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.sp
import com.example.wherekiddo.R

val fontNunito = FontFamily(
    Font(R.font.nunito_black, Black),
    Font(R.font.nunito_blackitalic, Black, Italic),
    Font(R.font.nunito_bold, Bold),
    Font(R.font.nunito_bolditalic, Bold, Italic),
    Font(R.font.nunito_extrabold, ExtraBold),
    Font(R.font.nunito_extrabolditalic, ExtraBold, Italic),
    Font(R.font.nunito_extralight, ExtraLight),
    Font(R.font.nunito_extralightitalic, ExtraLight, Italic),
    Font(R.font.nunito_italic, Normal, Italic),
    Font(R.font.nunito_light, Light),
    Font(R.font.nunito_lightitalic, Light, Italic),
    Font(R.font.nunito_medium, Medium),
    Font(R.font.nunito_mediumitalic, Medium, Italic),
    Font(R.font.nunito_regular, Normal),
    Font(R.font.nunito_semibold, SemiBold),
    Font(R.font.nunito_semibolditalic, SemiBold, Italic)
)

val Typography = Typography(
    defaultFontFamily = fontNunito,
    h1 = TextStyle(
        fontFamily = fontNunito,
        fontWeight = Bold,
        fontSize = 32.sp,
        lineHeight = 32.sp.times(1.12),
        letterSpacing = 0.sp
    ),
    h2 = TextStyle(
        fontFamily = fontNunito,
        fontWeight = Bold,
        fontSize = 24.sp,
        lineHeight = 24.sp.times(1.12),
        letterSpacing = 0.sp
    ),
    h3 = TextStyle(
        fontFamily = fontNunito,
        fontWeight = Bold,
        fontSize = 22.sp,
        lineHeight = 22.sp.times(1.12),
        letterSpacing = 0.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = fontNunito,
        fontWeight = Bold,
        fontSize = 18.sp,
        lineHeight = 18.sp.times(1.12),
        letterSpacing = 0.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = fontNunito,
        fontWeight = Bold,
        fontSize = 16.sp,
        lineHeight = 16.sp.times(1.12),
        letterSpacing = 0.sp
    ),
    body1 = TextStyle(
        fontFamily = fontNunito,
        fontWeight = Medium,
        fontSize = 14.sp,
        lineHeight = 14.sp.times(1.44),
        letterSpacing = 0.sp
    ),
    body2 = TextStyle(
        fontFamily = fontNunito,
        fontWeight = Medium,
        fontSize = 12.sp,
        lineHeight = 12.sp.times(1.44),
        letterSpacing = 0.sp
    ),
    button = TextStyle(
        fontFamily = fontNunito,
        fontWeight = ExtraBold,
        fontSize = 16.sp,
        lineHeight = 16.sp.times(1.12),
        letterSpacing = 0.sp
    ),
    h4 = TextStyle(
        fontFamily = fontNunito,
        fontWeight = ExtraBold,
        fontSize = 14.sp,
        lineHeight = 14.sp.times(1.44),
        letterSpacing = 0.sp
    ),
    caption = TextStyle(
        fontFamily = fontNunito,
        fontWeight = SemiBold,
        fontSize = 14.sp,
        lineHeight = 14.sp.times(1.44),
        letterSpacing = 0.sp
    ),
    overline = TextStyle(
        fontFamily = fontNunito,
        fontWeight = Bold,
        fontSize = 12.sp,
        lineHeight = 12.sp.times(1.44),
        letterSpacing = 0.sp
    )
)