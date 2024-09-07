package com.example.wherekiddo.repository.implementations

import android.net.Uri
import com.example.wherekiddo.domain.enums.UserTypeEnum
import com.example.wherekiddo.domain.models.UserData
import com.example.wherekiddo.domain.models.UserDataDTO
import com.example.wherekiddo.domain.models.toUserData
import com.example.wherekiddo.domain.models.toUserDataList
import com.example.wherekiddo.repository.constants.USERS_DATA_COLLECTION_REF
import com.example.wherekiddo.repository.constants.USER_IMAGES_STORAGE_REF
import com.example.wherekiddo.repository.interactors.UsersDataInteractor
import com.example.wherekiddo.util.Resource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UsersDataRepository: UsersDataInteractor {

    private val usersDataRef: CollectionReference =
        Firebase.firestore.collection(USERS_DATA_COLLECTION_REF)

    private val usersImagesRef: StorageReference =
        Firebase.storage.reference.child(USER_IMAGES_STORAGE_REF)

    override fun getUserData(
        userId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (UserData?) -> Unit
    ) {

        usersDataRef
            .document(userId)
            .get()
            .addOnSuccessListener { onSuccess.invoke(it.toObject(UserDataDTO::class.java).toUserData()) }
            .addOnFailureListener { onError.invoke(it.cause) }
    }

    override fun getDriverData(vehiclePlates: String): Flow<Resource<UserData>> = callbackFlow {

        val snapshotStateListener: ListenerRegistration? = null

        try {

            usersDataRef
                .whereEqualTo("vehiclePlates", vehiclePlates)
                .whereEqualTo("userType", UserTypeEnum.DRIVER.name)
                .addSnapshotListener { snapshot, e ->

                    trySend(Resource.Loading())

                    val response = if (snapshot != null) {

                        val userData = snapshot.toObjects(UserDataDTO::class.java)
                            .toUserDataList()
                            .firstOrNull()

                        userData?.let {

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

    override fun addUserData(
        userId: String,
        name: String,
        surname: String,
        email: String,
        userType: UserTypeEnum,
        onComplete: (Boolean) -> Unit
    ) {

        val userData = UserData(
            id = userId,
            name = name,
            surname = surname,
            email = email,
            userType = userType
        )

        usersDataRef
            .document(userId)
            .set(userData)
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }
    }

    override fun editUserData(
        userData: UserData,
        onComplete: (Boolean) -> Unit
    ) {

        usersDataRef
            .document(userData.id)
            .set(userData)
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }
    }

    override fun setUserVehiclePlates(
        userId: String,
        vehiclePlates: String,
        onComplete: (Boolean) -> Unit
    ) {

        usersDataRef
            .document(userId)
            .update(mapOf(Pair("vehiclePlates", vehiclePlates)))
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }
    }

    override fun uploadUserPhoto(
        userId: String,
        photoUri: Uri,
        onSuccess: () -> Unit,
        onError: (Throwable?) -> Unit
    ) {

        val imageRef = usersImagesRef.child(userId)

        imageRef.putFile(photoUri).addOnCompleteListener { result ->

            if (result.isSuccessful) {

                imageRef.downloadUrl.addOnSuccessListener { uri ->

                    usersDataRef.document(userId)
                        .update("photoUrl", uri.toString())
                        .addOnSuccessListener {
                            onSuccess.invoke()
                        }
                        .addOnFailureListener {
                            onError.invoke(it.cause)
                        }
                }

            } else {

                onError.invoke(result.exception?.cause)
            }
        }
    }
}