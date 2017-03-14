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
@RequestMapping("/test/api")
class TestApiController {

    @RequestMapping("/fetchAllShuttleActivity")
    fun fetchAllShuttleActivity(
            serviceCode: String?
    ) : ResponseEntity<List<ShuttleActivityDetailsAdapter>> {
        val stops = arrayListOf<StopAdapter>()
        val stop1 = StopAdapter()
        stop1.name = "Stop 1"
        stop1.order = 0
        stop1.address = "123 Baseball Stadium Road"
        stop1.lat = BigDecimal("39.8282")
        stop1.long = BigDecimal("-98.5795")
        stop1.estArriveTime = LocalTime.of(11, 30)
        stop1.estDepartTime = LocalTime.of(12, 0)

        val stop2 = StopAdapter()
        stop2.name = "Stop 2"
        stop2.order = 1
        stop2.address = "123 Baseball Stadium Road"
        stop2.lat = BigDecimal("39.8282")
        stop2.long = BigDecimal("-98.5795")
        stop2.estArriveTime = LocalTime.of(12+1, 30)
        stop2.estDepartTime = LocalTime.of(12+2, 30)

        val stop3 = StopAdapter()
        stop3.name = "Stop 3"
        stop3.order = 2
        stop3.address = "123 Baseball Stadium Road"
        stop3.lat = BigDecimal("39.8282")
        stop3.long = BigDecimal("-98.5795")
        stop3.estArriveTime = LocalTime.of(12+3, 0)
        stop3.estDepartTime = null

        stops.add(stop1)
        stops.add(stop2)
        stops.add(stop3)

        val listOfDetails = arrayListOf<ShuttleActivityDetailsAdapter>()

        val activity1 = ShuttleActivityDetailsAdapter()
        activity1.activityId = 1
        activity1.driverName = "Travis Caro"
        activity1.shuttleName = "Shuttle 1"
        activity1.shuttleColorHex = "#00ff00"
        activity1.shuttleStatus = ShuttleState.ACTIVE
        activity1.shuttleLatitude = BigDecimal("39.8282")
        activity1.shuttleLongitude = BigDecimal("-98.5795")
        activity1.shuttleHeading = BigDecimal("0")
        activity1.assignmentReport = null

        val activity2 = ShuttleActivityDetailsAdapter()
        activity2.activityId = 2
        activity2.driverName = "Tyler Holben"
        activity2.shuttleName = "Shuttle 2"
        activity2.shuttleColorHex = "#0000ff"
        activity2.shuttleStatus = ShuttleState.DRIVING
        activity2.shuttleLatitude = BigDecimal("39.8282")
        activity2.shuttleLongitude = BigDecimal("-98.5795")
        activity2.shuttleHeading = BigDecimal("0")
        activity2.assignmentReport?.stops = stops
        activity2.assignmentReport?.currentStop = 0
        activity2.assignmentReport?.assignmentStatus = AssignmentState.IN_PROGRESS

        val activity3 = ShuttleActivityDetailsAdapter()
        activity3.activityId = 3
        activity3.driverName = "Zach Kruise"
        activity3.shuttleName = "Shuttle 3"
        activity3.shuttleColorHex = "#ff0000"
        activity3.shuttleStatus = ShuttleState.AT_STOP
        activity3.shuttleLatitude = BigDecimal("39.8282")
        activity3.shuttleLongitude = BigDecimal("-98.5795")
        activity3.shuttleHeading = BigDecimal("0")
        activity3.assignmentReport?.stops = stops
        activity3.assignmentReport?.currentStop = 1
        activity3.assignmentReport?.assignmentStatus = AssignmentState.IN_PROGRESS

        listOfDetails.add(activity1)
        listOfDetails.add(activity2)
        listOfDetails.add(activity3)

        if (serviceCode.isNullOrBlank()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(listOfDetails, HttpStatus.OK)
    }
}