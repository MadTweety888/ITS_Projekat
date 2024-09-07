package com.example.wherekiddo.domain.models

import com.example.wherekiddo.domain.enums.UserTypeEnum

data class UserData(
    val id: String,
    val name: String,
    val surname: String,
    val email: String,
    val userType: UserTypeEnum,
    val photoUrl: String = "",
    val vehiclePlates: String = ""
)