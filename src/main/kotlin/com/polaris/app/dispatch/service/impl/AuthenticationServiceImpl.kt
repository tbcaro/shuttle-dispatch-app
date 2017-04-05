package com.polaris.app.dispatch.service.impl

import com.polaris.app.dispatch.repository.UserRepository
import com.polaris.app.dispatch.repository.UserType
import com.polaris.app.dispatch.service.bo.UserContext
import com.polaris.app.dispatch.controller.exception.AuthenticationException
import com.polaris.app.dispatch.service.AuthenticationService
import java.util.*
import javax.servlet.http.HttpServletRequest


class AuthenticationServiceImpl(val userRepository: UserRepository) : AuthenticationService {
    val SERVICE_ID = "serviceId"
    val SERVICE_CODE = "serviceCode"
    val USER_ID = "userId"
    val USERNAME = "username"

    override fun authenticate(http: HttpServletRequest, username: String, password: String, servicecode: String) {
        val users = userRepository.findUserByLogin(username, password, servicecode)
        if (users.isEmpty()) {
            throw AuthenticationException("Authentication failed: invalid username, password, or servicecode")
        } else if (users.size > 1) {
            throw AuthenticationException("Authentication failed: more than one user for this combo. Please contact your administrator.")
        } else {
            val user = users[0]
            if (user.userType == UserType.DISPATCHER) {
                val sessionAttributes = HashMap<String, Any>()
                sessionAttributes.put(SERVICE_ID, user.serviceId)
                sessionAttributes.put(SERVICE_CODE, servicecode)
                sessionAttributes.put(USER_ID, user.id)
                sessionAttributes.put(USERNAME, user.userName)

                this.generateSession(http, sessionAttributes)
            }
        }
    }

    override fun isAuthenticated(http: HttpServletRequest): Boolean {
        val session = http.getSession(false)

        session?.let {
            try {
                var user = this.getUserContext(http)
                if (user.username.isNotBlank() && user.servicecode.isNotBlank()) return true
            } catch (ex: Exception) { }
        }

        return false
    }

    override fun generateSession(http: HttpServletRequest, attributes: Map<String, Any>) {
        val session = http.getSession(true)

        attributes.entries.forEach {
            session.setAttribute(it.key, it.value)
        }

        // TBC : Max inactive session: 30 mins
        session.maxInactiveInterval = 30 * 60
    }

    override fun invalidateSession(http: HttpServletRequest) {
        val session = http.getSession(false)
        session?.let {
            it.invalidate()
        }
    }

    override fun getUserContext(http: HttpServletRequest) : UserContext {
        val session = http.getSession(false)
        session?.let {
            val serviceId = it.getAttribute(SERVICE_ID) as? Int
            val servicecode = it.getAttribute(SERVICE_CODE) as? String
            val userId = it.getAttribute(USER_ID) as? Int
            val username = it.getAttribute(USERNAME) as? String

            val user = UserContext()
            serviceId?.let { user.serviceId = it }
            servicecode?.let { user.servicecode = it }
            userId?.let { user.userId = it }
            username?.let { user.username = it }

            return user
        }

        throw Exception("Not authenticated")
    }
}