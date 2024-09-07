package com.example.wherekiddo.repository.implementations

import com.example.wherekiddo.repository.constants.TRACKING_COLLECTION_REF
import com.example.wherekiddo.repository.interactors.TrackingInteractor
import com.example.wherekiddo.util.Resource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

class TrackingRepository: TrackingInteractor {

    private val trackingDataRef: CollectionReference =
        Firebase.firestore.collection(TRACKING_COLLECTION_REF)

    override fun getVehicleLocation(vehiclePlates: String): Flow<Resource<GeoPoint>> = callbackFlow {

        val snapshotStateListener: ListenerRegistration? = null

        try {

            trackingDataRef
                .document(vehiclePlates)
                .addSnapshotListener { snapshot, e ->

                    trySend(Resource.Loading())

                    val response = if (snapshot != null) {

                        val location = snapshot.get("location") as? GeoPoint

                        location?.let {

                            Resource.Success(data = it)

                        } ?: Resource.Error(message = e?.message ?: "Vehicle not found!")

                    } else {

                        Resource.Error(message = e?.message ?: "Vehicle not found!")
                    }

                    trySend(response)
                }
        } catch (e:Exception) {

            trySend(Resource.Error(e.message ?: "Something went wrong"))
            e.printStackTrace()
        }

        awaitClose { snapshotStateListener?.remove() }
    }

    override fun updateVehicleLocation(
        vehiclePlates: String,
        latitude: Double,
        longitude: Double,
        onComplete: (Boolean) -> Unit
    ) {

        Timber.i("TAGA - plates: $vehiclePlates")

        val data = mapOf("location" to GeoPoint(latitude, longitude))

        trackingDataRef
            .document(vehiclePlates)
            .set(data, SetOptions.merge())
            .addOnCompleteListener { result ->

                onComplete.invoke(result.isSuccessful)
            }
    }
}