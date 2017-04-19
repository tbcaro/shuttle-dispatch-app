package com.polaris.app.dispatch.controller.exception


class AuthenticationException(private val msg : String = "User logged out") : Exception(msg)