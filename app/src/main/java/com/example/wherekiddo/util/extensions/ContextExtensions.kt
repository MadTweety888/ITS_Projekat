package com.example.wherekiddo.util.extensions

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.content.ContextCompat

fun Context.findActivity(): Activity? {

    var context = this

    while (context is ContextWrapper) {

        if (context is Activity) return context

        context = context.baseContext
    }

    return null
}

fun Context.hasLocationPermission(): Boolean {

    return ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED &&
           ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
}