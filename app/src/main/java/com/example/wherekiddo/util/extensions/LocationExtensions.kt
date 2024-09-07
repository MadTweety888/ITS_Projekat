package com.example.wherekiddo.util.extensions

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint

fun Location.toFormattedString(): String {

    val lat = this.latitude.toString()
    val lng = this.longitude.toString()

    return "${lat}º N, ${lng}º E"
}

fun GeoPoint?.toFormattedString(): String {

    val lat = this?.latitude.toString()
    val lng = this?.longitude.toString()

    return "${lat}º N, ${lng}º E"
}

fun GeoPoint.toLatLng(): LatLng {

    return LatLng(this.latitude, this.longitude)
}