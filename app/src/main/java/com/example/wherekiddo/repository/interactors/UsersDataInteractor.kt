package com.example.wherekiddo.repository.interactors

import android.net.Uri
import com.example.wherekiddo.domain.enums.UserTypeEnum
import com.example.wherekiddo.domain.models.UserData
import com.example.wherekiddo.util.Resource
import kotlinx.coroutines.flow.Flow

interface UsersDataInteractor {

    fun getUserData(
        userId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (UserData?) -> Unit
    )

    fun getDriverData(vehiclePlates: String): Flow<Resource<UserData>>

    fun addUserData(
        userId: String,
        name: String,
        surname: String,
        email: String,
        userType: UserTypeEnum,
        onComplete: (Boolean) -> Unit
    )

    fun editUserData(
        userData: UserData,
        onComplete: (Boolean) -> Unit
    )

    fun setUserVehiclePlates(
        userId: String,
        vehiclePlates: String,
        onComplete: (Boolean) -> Unit
    )

    fun uploadUserPhoto(
        userId: String,
        photoUri: Uri,
        onSuccess: () -> Unit,
        onError: (Throwable?) -> Unit
    )
}