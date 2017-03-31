package com.polaris.app.dispatch.repository

import com.polaris.app.dispatch.repository.entity.UserEntity

interface UserRepository {
    fun findUserByLogin(username: String, password: String, servicecode: String) : List<UserEntity>
}