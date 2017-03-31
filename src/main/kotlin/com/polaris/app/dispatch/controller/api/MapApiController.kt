package com.polaris.app.dispatch.controller.api

import com.polaris.app.dispatch.controller.adapter.AssignmentReport
import com.polaris.app.dispatch.controller.adapter.ShuttleActivityDetailsAdapter
import com.polaris.app.dispatch.controller.adapter.AssignmentStopAdapter
import com.polaris.app.dispatch.controller.adapter.enums.AssignmentState
import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import com.polaris.app.dispatch.service.MapService
import com.polaris.app.dispatch.service.AuthenticationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.LocalTime
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api")
class MapApiController(private val authService: AuthenticationService, private val mapService: MapService) {

    @RequestMapping("/fetchAllShuttleActivity")
    fun fetchAllShuttleActivity(http: HttpServletRequest) : ResponseEntity<List<ShuttleActivityDetailsAdapter>> {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)
            val listOfDetails = arrayListOf<ShuttleActivityDetailsAdapter>()

            val shuttleActivities = mapService.retrieveShuttle(userContext.serviceId)

            return ResponseEntity(listOfDetails, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }
}