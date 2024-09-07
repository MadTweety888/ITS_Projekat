package com.example.wherekiddo.domain.models

import com.example.wherekiddo.domain.enums.toUserTypeEnum

data class UserDataDTO(
    val id: String = "",
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val userType: String = "",
    val photoUrl: String = "",
    val vehiclePlates: String = ""
)

fun UserDataDTO?.toUserData(): UserData? {

    this?.let {

        return UserData(
            id = id.ifBlank { return null },
            name = name,
            surname = surname,
            email = email,
            photoUrl = photoUrl,
            vehiclePlates = vehiclePlates,
            userType = userType.toUserTypeEnum() ?: return null
        )

    } ?: return null
}

fun List<UserDataDTO>?.toUserDataList(): List<UserData> {

    return this?.mapNotNull { it.toUserData() } ?: emptyList()
}
