package com.polaris.app.dispatch.controller.api

import com.polaris.app.dispatch.controller.adapter.*
import com.polaris.app.dispatch.controller.adapter.enums.AssignmentState
import com.polaris.app.dispatch.service.AssignmentService
import com.polaris.app.dispatch.service.AuthenticationService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api")
class AssignmentApiController(private val authService: AuthenticationService, private val assignmentService: AssignmentService) {

    @RequestMapping("/fetchAllAssignments")
    fun fetchAllAssignments(
            http: HttpServletRequest,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) date: LocalDate?
    ) : ResponseEntity<AssignmentListAdapter> {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)
            val assignmentListAdapter = AssignmentListAdapter()
            val detailsAdapters = arrayListOf<AssignmentDetailsAdapter>()
            assignmentListAdapter.selectedDate = date

            val assignments = assignmentService.retrieveAssignments(userContext.serviceId, date ?: LocalDate.now())
            assignments.forEach {
                val assignmentDetails = AssignmentDetailsAdapter()

                assignmentDetails.driverId = it.driverID
                assignmentDetails.driverName = "${it.driverFName} ${it.driverLName}"
                assignmentDetails.routeId = it.routeID
                assignmentDetails.routeName = it.routeName
                assignmentDetails.shuttleId = it.shuttleID
                assignmentDetails.shuttleName = it.shuttleName
                assignmentDetails.startTime = it.startTime

                val report = AssignmentReport()
                val stops = arrayListOf<AssignmentStopAdapter>()
                report.assignmentId = it.assignmentID
                // TODO : Possibly return assignmentStatus as well

                it.stops.forEach {
                    val stopDetails = AssignmentStopAdapter()

                    stopDetails.stopId = it.stopId
                    stopDetails.address = it.stopAddress
                    stopDetails.name = it.stopName
                    stopDetails.lat = it.stopLat
                    stopDetails.long = it.stopLong
                    stopDetails.estArriveTime = it.stopArriveEst.toLocalTime()
                    stopDetails.estDepartTime = it.stopDepartEst.toLocalTime()
                    stopDetails.actualArriveTime = it.stopArrive.toLocalTime()
                    stopDetails.actualDepartTime = it.stopDepart.toLocalTime()

                    stops.add(stopDetails)
                }

                report.assignmentStops = stops
                assignmentDetails.assignmentReport = report
                detailsAdapters.add(assignmentDetails)
            }

            assignmentListAdapter.assignmentDetailAdapters = detailsAdapters
            return ResponseEntity(assignmentListAdapter, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }

    @RequestMapping("/assignment/formOptions")
    fun assignmentForm() : ResponseEntity<AssignmentFormOptionsAdapter> {
        val options = AssignmentFormOptionsAdapter()

        // TBC : Populate options for form and return
        options.shuttleOptions.put(1, "Shuttle 1A")
        options.shuttleOptions.put(2, "Shuttle 2A")
        options.shuttleOptions.put(3, "Shuttle 3A")

        options.driverOptions.put(1, "Travis Caro")
        options.driverOptions.put(2, "Tyler Holben")
        options.driverOptions.put(3, "Zach Kruise")

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

        options.stopOptions.put(stop1.stopId, stop1)
        options.stopOptions.put(stop2.stopId, stop2)
        options.stopOptions.put(stop3.stopId, stop3)

        val route1 = RouteDetailsAdapter()
        route1.routeId = 1
        route1.name = "Trav's Route"
        route1.stops = arrayListOf(stop1, stop2)

        val route2 = RouteDetailsAdapter()
        route2.routeId = 2
        route2.name = "Tyler's Route"
        route2.stops = arrayListOf(stop2, stop3)

        val route3 = RouteDetailsAdapter()
        route3.routeId = 3
        route3.name = "Zach's Route"
        route3.stops = arrayListOf(stop2, stop3, stop1)

        options.routeOptions.put(route1.routeId, route1)
        options.routeOptions.put(route2.routeId, route2)
        options.routeOptions.put(route3.routeId, route3)

        return ResponseEntity(options, HttpStatus.OK)
    }

    @RequestMapping("/assignment/save", method = arrayOf(RequestMethod.POST))
    fun saveAssignment(
            @RequestBody form: AssignmentFormAdapter
    ) : ResponseEntity<Int> {
        var assignmentId = 0

        if (form.assignmentId.value == null) {
            // TBC : Do create
            assignmentId = 2
        } else {
            // TBC : Do update
            assignmentId = 1
        }

        return ResponseEntity(assignmentId, HttpStatus.OK)
    }

    @RequestMapping("/assignment/archive", method = arrayOf(RequestMethod.POST))
    fun archiveAssignment(
            @RequestBody archiveAdapter: AssignmentArchiveAdapter
    ) : ResponseEntity<Int> {
        return ResponseEntity(archiveAdapter.assignmentId, HttpStatus.OK)
    }
}