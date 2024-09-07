package com.example.wherekiddo.domain.enums

import com.example.wherekiddo.domain.enums.UserTypeEnum.DRIVER
import com.example.wherekiddo.domain.enums.UserTypeEnum.PARENT

enum class UserTypeEnum {

    PARENT,
    DRIVER
}

fun UserTypeEnum.toUserTypeString(): String {

    return when (this) {

        PARENT -> "Parent"
        DRIVER -> "Driver"
    }
}

fun String.toUserTypeEnum(): UserTypeEnum? {

    return when (this) {

        "PARENT" -> PARENT
        "DRIVER" -> DRIVER
        else     -> null
    }
}