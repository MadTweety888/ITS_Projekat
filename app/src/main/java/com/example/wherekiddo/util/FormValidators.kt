package com.example.wherekiddo.util

import android.content.Context
import android.util.Patterns
import com.example.wherekiddo.R

fun validateNotEmpty(textState: String, context: Context): Pair<Boolean, String> {
    return if (textState.isEmpty()) {
        Pair(true, context.getString(R.string.error_cant_be_empty))
    } else Pair(false, "")
}

fun validateSignUpEmailTextField(email: String, context: Context): Pair<Boolean, String> {
    return if (email.isEmpty()) {
        Pair(true, context.getString(R.string.error_cant_be_empty))
    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        Pair(true, context.getString(R.string.error_invalid_email))
    } else Pair(false, "")
}

fun validateSignUpPasswordTextField(password: String, context: Context): Pair<Boolean, String> {
    return if (password.isEmpty()) {
        Pair(true, context.getString(R.string.error_cant_be_empty))
    } else if (password.length < 6) {
        Pair(true, context.getString(R.string.error_password_must_have_6_chars))
    } else Pair(false, "")
}

fun validateRepeatPasswordTextField(
    repeatPasswordTextState: String,
    passwordToMatchTextState: String,
    context: Context
): Pair<Boolean, String> {
    return if (repeatPasswordTextState.isEmpty())
        Pair(true, context.getString(R.string.error_cant_be_empty))
    else if (repeatPasswordTextState != passwordToMatchTextState) {
        Pair(true, context.getString(R.string.error_passwords_dont_match))
    } else {
        Pair(false, "")
    }
}