package com.polaris.app.dispatch.controller.exception


class AuthenticationException(private val msg : String = "UserEntity not logged in") : Exception(msg)