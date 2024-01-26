package com.newspeed.domain.auth.dto

import com.newspeed.global.exception.auth.DifferentUserException

data class OAuth2UnlinkDTO(
    val authorizationCode: String,
    val email: String
) {

    fun validateByEmail(
        userEmail: String
    ) {
        if (this.isNotSameEmail(userEmail)) {
            throw DifferentUserException()
        }
    }

    private fun isNotSameEmail(
        thatEmail: String
    ) = email != thatEmail
}
