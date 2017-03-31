package com.polaris.app.dispatch.repository.entity

import com.polaris.app.dispatch.repository.UserType


data class UserEntity(
        val id: Int,
        val serviceId: Int,
        val firstName: String,
        val lastName: String,
        val userName: String,
        val userType: UserType
)