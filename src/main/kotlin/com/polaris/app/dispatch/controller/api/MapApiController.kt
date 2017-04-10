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
import java.time.LocalDateTime
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

            shuttleActivities.forEach {
                val activityDetailsAdapter = ShuttleActivityDetailsAdapter()
                activityDetailsAdapter.activityId = it.shuttleID
                activityDetailsAdapter.shuttleName = it.shuttleName
                activityDetailsAdapter.shuttleColorHex = it.shuttleIconColor
                activityDetailsAdapter.shuttleLatitude = it.shuttleLat
                activityDetailsAdapter.shuttleLongitude = it.shuttleLong
                activityDetailsAdapter.shuttleHeading = it.heading
                activityDetailsAdapter.driverName = "${it.shuttleDriverFName} ${it.shuttleDriverLName}"
                activityDetailsAdapter.shuttleStatus = ShuttleState.valueOf(it.shuttleStatus)

                val activity = it
                val assignment = mapService.retrieveAssignmentData(it)
                assignment?.let {
                    val assignmentReport = AssignmentReport()

                    assignmentReport.assignmentId = it.assignmentID
                    assignmentReport.currentStop = activity.currentStopIndex

                    val stopAdapters = arrayListOf<AssignmentStopAdapter>()
                    it.stops.forEach {
                        val adapter = AssignmentStopAdapter()

                        adapter.assingmentStopId = it.assignmentStopId
                        adapter.stopId = it.stopId ?: 0
                        adapter.order = it.index
                        adapter.name = it.stopName
                        adapter.address = it.stopAddress
                        adapter.lat = it.stopLat
                        adapter.long = it.stopLong
                        adapter.estArriveTime = it.stopArriveEst.toLocalTime()
                        adapter.estDepartTime = it.stopDepartEst.toLocalTime()
                        adapter.actualArriveTime = it.stopArrive.toLocalTime()
                        adapter.actualDepartTime = it.stopDepart.toLocalTime()

                        stopAdapters.add(adapter)
                    }
                    assignmentReport.assignmentStops = stopAdapters

                    activityDetailsAdapter.assignmentReport = assignmentReport
                }

                listOfDetails.add(activityDetailsAdapter)
            }

//            println("\"/fetchAllShuttleActivity\" called at ${LocalDateTime.now()}")
//            println("${listOfDetails.size} activities found")
            return ResponseEntity(listOfDetails, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }
}