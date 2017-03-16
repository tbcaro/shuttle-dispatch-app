package com.polaris.app.dispatch.controller.api

import com.polaris.app.dispatch.DisplayDateFormatter
import com.polaris.app.dispatch.controller.adapter.*
import com.polaris.app.dispatch.controller.adapter.enums.AssignmentState
import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

@RestController
@RequestMapping("/test/api")
class TestApiController {

    @RequestMapping("/fetchAllShuttleActivity")
    fun fetchAllShuttleActivity(
            serviceCode: String?
    ) : ResponseEntity<List<ShuttleActivityDetailsAdapter>> {
        val stops = arrayListOf<AssignmentStopAdapter>()
        val stop1 = AssignmentStopAdapter()
        stop1.name = "Stop 1"
        stop1.order = 0
        stop1.address = "123 Baseball Stadium Road"
        stop1.lat = BigDecimal("41.192382")
        stop1.long = BigDecimal("-79.391694")
        stop1.estArriveTime = LocalTime.of(11, 30)
        stop1.estDepartTime = LocalTime.of(12, 0)

        val stop2 = AssignmentStopAdapter()
        stop2.name = "Stop 2"
        stop2.order = 1
        stop2.address = "123 Baseball Stadium Road"
        stop2.lat = BigDecimal("41.188791")
        stop2.long = BigDecimal("-79.394937")
        stop2.estArriveTime = LocalTime.of(12+1, 30)
        stop2.estDepartTime = LocalTime.of(12+2, 30)

        val stop3 = AssignmentStopAdapter()
        stop3.name = "Stop 3"
        stop3.order = 2
        stop3.address = "123 Baseball Stadium Road"
        stop3.lat = BigDecimal("41.207504")
        stop3.long = BigDecimal("-79.397200")
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
        activity1.shuttleLatitude = BigDecimal("41.211460")
        activity1.shuttleLongitude = BigDecimal("-79.380963")
        activity1.shuttleHeading = BigDecimal("30")
        activity1.assignmentReport = null

        val activity2 = ShuttleActivityDetailsAdapter()
        activity2.activityId = 2
        activity2.driverName = "Tyler Holben"
        activity2.shuttleName = "Shuttle 2"
        activity2.shuttleColorHex = "#0000ff"
        activity2.shuttleStatus = ShuttleState.DRIVING
        activity2.shuttleLatitude = BigDecimal("41.214120")
        activity2.shuttleLongitude = BigDecimal("-79.384094")
        activity2.shuttleHeading = BigDecimal("275")
        activity2.assignmentReport?.assignmentStops = stops
        activity2.assignmentReport?.currentStop = 0
        activity2.assignmentReport?.assignmentStatus = AssignmentState.IN_PROGRESS

        val activity3 = ShuttleActivityDetailsAdapter()
        activity3.activityId = 3
        activity3.driverName = "Zach Kruise"
        activity3.shuttleName = "Shuttle 3"
        activity3.shuttleColorHex = "#ff0000"
        activity3.shuttleStatus = ShuttleState.AT_STOP
        activity3.shuttleLatitude = BigDecimal("41.194253")
        activity3.shuttleLongitude = BigDecimal("-79.392443")
        activity3.shuttleHeading = BigDecimal("10")
        activity3.assignmentReport?.assignmentStops = stops
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

    @RequestMapping("/fetchAllAssignments")
    fun fetchAllAssignments(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) date: LocalDate?
    ) : ResponseEntity<AssignmentListAdapter> {
        val stops = arrayListOf<AssignmentStopAdapter>()
        val stop1 = AssignmentStopAdapter()
        stop1.name = "Stop 1"
        stop1.order = 0
        stop1.address = "123 Baseball Stadium Road"
        stop1.lat = BigDecimal("41.192382")
        stop1.long = BigDecimal("-79.391694")
        stop1.estArriveTime = LocalTime.of(11, 30)
        stop1.estDepartTime = LocalTime.of(12, 0)

        val stop2 = AssignmentStopAdapter()
        stop2.name = "Stop 2"
        stop2.order = 1
        stop2.address = "123 Baseball Stadium Road"
        stop2.lat = BigDecimal("41.188791")
        stop2.long = BigDecimal("-79.394937")
        stop2.estArriveTime = LocalTime.of(12+1, 30)
        stop2.estDepartTime = LocalTime.of(12+2, 30)

        val stop3 = AssignmentStopAdapter()
        stop3.name = "Stop 3"
        stop3.order = 2
        stop3.address = "123 Baseball Stadium Road"
        stop3.lat = BigDecimal("41.207504")
        stop3.long = BigDecimal("-79.397200")
        stop3.estArriveTime = LocalTime.of(12+3, 0)
        stop3.estDepartTime = null

        stops.add(stop1)
        stops.add(stop2)
        stops.add(stop3)

        val assignment1 = AssignmentDetailsAdapter()
        assignment1.assignmentReport?.assignmentId = 1
        assignment1.assignmentReport?.assignmentStops = stops
        assignment1.assignmentReport?.currentStop = 0
        assignment1.assignmentReport?.assignmentStatus = AssignmentState.SCHEDULED
        assignment1.shuttleId = 1
        assignment1.shuttleName = "Shuttle 1A"
        assignment1.driverId = 1
        assignment1.driverName = "Travis Caro"
        assignment1.routeId = 1
        assignment1.routeName = "Downtown Loop"
        assignment1.startTime = LocalTime.now()

        val assignment2 = AssignmentDetailsAdapter()
        assignment2.assignmentReport?.assignmentId = 2
        assignment2.assignmentReport?.assignmentStops = stops
        assignment2.assignmentReport?.currentStop = 0
        assignment2.assignmentReport?.assignmentStatus = AssignmentState.SCHEDULED
        assignment2.shuttleId = 2
        assignment2.shuttleName = "Shuttle 2A"
        assignment2.driverId = 2
        assignment2.driverName = "Tyler Holben"
        assignment2.routeId = 2
        assignment2.routeName = "Downtown Loop"
        assignment2.startTime = LocalTime.now()

        val assignment3 = AssignmentDetailsAdapter()
        assignment3.assignmentReport?.assignmentId = 3
        assignment3.assignmentReport?.assignmentStops = stops
        assignment3.assignmentReport?.currentStop = 0
        assignment3.assignmentReport?.assignmentStatus = AssignmentState.SCHEDULED
        assignment3.shuttleId = 3
        assignment3.shuttleName = "Shuttle 3A"
        assignment3.driverId = 3
        assignment3.driverName = "Zach Kruise"
        assignment3.routeId = 3
        assignment3.routeName = "Downtown Loop"
        assignment3.startTime = LocalTime.now()

        val listAdapter = AssignmentListAdapter()
        listAdapter.selectedDate = date

        if(date != null) {
            // TBC : If day before today, add some more
            val yesterday = LocalDate.now().minusDays(1)
            val tomorrow = LocalDate.now().plusDays(1)
            if (date == yesterday) {
                listAdapter.assignmentDetailAdapters.add(assignment1)
            } else if (date == tomorrow) {
                listAdapter.assignmentDetailAdapters.add(assignment1)
                listAdapter.assignmentDetailAdapters.add(assignment2)
            } else {
                listAdapter.assignmentDetailAdapters.add(assignment1)
                listAdapter.assignmentDetailAdapters.add(assignment2)
                listAdapter.assignmentDetailAdapters.add(assignment3)
            }
        }

        return ResponseEntity(listAdapter, HttpStatus.OK)
    }

    @RequestMapping("/assignment/form")
    fun assignmentForm() : ResponseEntity<AssignmentFormAdapter> {
        val form = AssignmentFormAdapter()

        // TBC : Populate options for form and return

        return ResponseEntity(form, HttpStatus.OK)
    }

    @RequestMapping("/assignment/create")
    fun createAssignment(
            form: AssignmentFormAdapter
    ) : ResponseEntity<Int> {
        val id = 0

        // TBC : Throw exception if invalid

        return ResponseEntity(id, HttpStatus.OK)
    }

    @RequestMapping("/assignment/update")
    fun updateAssignment(
            form: AssignmentFormAdapter
    ) : ResponseEntity<Int> {
        val id = 0

        // TBC : Throw exception if invalid

        return ResponseEntity(id, HttpStatus.OK)
    }
}