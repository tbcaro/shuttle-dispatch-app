package com.polaris.app.dispatch.repository

import com.polaris.app.dispatch.repository.entity.HelloWorldEntity


interface HelloWorldRepository {
    fun findAll(): List<HelloWorldEntity>
}