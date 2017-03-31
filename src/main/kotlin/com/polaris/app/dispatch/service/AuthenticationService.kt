package com.polaris.app.dispatch.service

import com.polaris.app.dispatch.repository.UserRepository
import com.polaris.app.dispatch.service.bo.UserContext
import javax.servlet.http.HttpServletRequest


interface AuthenticationService {
    fun authenticate(http: HttpServletRequest, username: String, password: String, servicecode: String)
    fun isAuthenticated(http: HttpServletRequest) : Boolean
    fun generateSession(http: HttpServletRequest, attributes: Map<String, Any>)
    fun invalidateSession(http: HttpServletRequest)
    fun getUserContext(http: HttpServletRequest) : UserContext
}