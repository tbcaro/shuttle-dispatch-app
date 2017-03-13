package com.polaris.app.dispatch.controller.api

import com.polaris.app.dispatch.controller.adapter.AssignmentReport
import com.polaris.app.dispatch.controller.adapter.ShuttleActivityDetailsAdapter
import com.polaris.app.dispatch.controller.adapter.StopAdapter
import com.polaris.app.dispatch.controller.adapter.enums.AssignmentState
import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.LocalTime

@RestController
@RequestMapping("/api")
class MapApiController {

    @RequestMapping("/fetchAllShuttleActivity")
    fun fetchAllShuttleActivity(
            serviceCode: String?
    ) : ResponseEntity<List<ShuttleActivityDetailsAdapter>> {
        val testDetails = ShuttleActivityDetailsAdapter()

        testDetails.driverName = "Travis Caro"
        testDetails.shuttleName = "Shuttle 1"
        testDetails.shuttleColorHex = "#0000ff"
        testDetails.shuttleStatus = ShuttleState.DRIVING
        testDetails.shuttleLatitude = BigDecimal("39.8282")
        testDetails.shuttleLongitude = BigDecimal("-98.5795")
        testDetails.shuttleHeading = BigDecimal("0")

        val testStop = StopAdapter()
        testStop.name = "Baseball Stadium"
        testStop.order = 0
        testStop.address = "123 Baseball Stadium Road"
        testStop.lat = BigDecimal("39.8282")
        testStop.long = BigDecimal("-98.5795")
        testStop.estArriveTime = LocalTime.of(5 + 12, 30)
        testStop.estDepartTime = LocalTime.of(6 + 12, 0)

        val stops = arrayListOf<StopAdapter>()
        stops.add(testStop)

        testStop.order = 1
        stops.add(testStop)

        testStop.order = 2
        stops.add(testStop)

        testDetails.assignmentReport.stops = stops
        testDetails.assignmentReport.currentStop = 1
        testDetails.assignmentReport.assignmentStatus = AssignmentState.IN_PROGRESS

        val listOfDetails = arrayListOf<ShuttleActivityDetailsAdapter>()

        listOfDetails.add(testDetails)
        listOfDetails.add(testDetails)
        listOfDetails.add(testDetails)

        if (serviceCode.isNullOrBlank()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(listOfDetails, HttpStatus.OK)
    }
}