package com.example.wherekiddo.repository.interactors

import com.example.wherekiddo.util.Resource
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.flow.Flow

interface TrackingInteractor {

    fun getVehicleLocation(vehiclePlates: String): Flow<Resource<GeoPoint>>

    fun updateVehicleLocation(
        vehiclePlates: String,
        latitude: Double,
        longitude: Double,
        onComplete: (Boolean) -> Unit
    )
}