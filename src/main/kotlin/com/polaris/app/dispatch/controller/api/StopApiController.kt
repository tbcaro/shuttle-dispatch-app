package com.polaris.app.dispatch.controller.api

import com.polaris.app.dispatch.controller.adapter.RouteFormAdapter
import com.polaris.app.dispatch.controller.adapter.*
import com.polaris.app.dispatch.service.AssignmentService
import com.polaris.app.dispatch.service.AuthenticationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api")
class StopApiController(private val authService: AuthenticationService) {

    @RequestMapping("/fetchAllStops")
    fun fetchAllStops(
            http: HttpServletRequest
    ) : ResponseEntity<List<StopDetailsAdapter>> {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)

            val stop1 = StopDetailsAdapter()
            stop1.stopId = 1
            stop1.name = "Stop 1"
            stop1.address = "123 Stop 1 Address"
            stop1.lat = BigDecimal("41.192382")
            stop1.long = BigDecimal("-79.391694")

            val stop2 = StopDetailsAdapter()
            stop2.stopId = 2
            stop2.name = "Stop 2"
            stop2.address = "123 Stop 2 Address"
            stop2.lat = BigDecimal("41.188791")
            stop2.long = BigDecimal("-79.394937")

            val stop3 = StopDetailsAdapter()
            stop3.stopId = 3
            stop3.name = "Stop 3"
            stop3.address = "123 Stop 3 Address"
            stop3.lat = BigDecimal("41.207504")
            stop3.long = BigDecimal("-79.397200")

            return ResponseEntity(arrayListOf(stop1, stop2, stop3), HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }

    @RequestMapping("/stop/save", method = arrayOf(RequestMethod.POST))
    fun saveStop(
            http: HttpServletRequest,
            @RequestBody form: StopFormAdapter
    ) : ResponseEntity<Int> {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)
            var stopId = 0

            return ResponseEntity(stopId, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }

    @RequestMapping("/stop/archive", method = arrayOf(RequestMethod.POST))
    fun archiveStop(
            @RequestBody archiveAdapter: StopArchiveAdapter
    ) : ResponseEntity<Int> {
        return ResponseEntity(0, HttpStatus.OK)
    }
}