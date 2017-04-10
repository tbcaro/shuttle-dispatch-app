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
    fun assignmentForm(http: HttpServletRequest) : ResponseEntity<AssignmentFormOptionsAdapter> {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)
            val options = AssignmentFormOptionsAdapter()

            val shuttles = assignmentService.shuttleDrop(userContext.serviceId)
            val drivers = assignmentService.driverDrop(userContext.serviceId)
            val routes = assignmentService.routeDrop(userContext.serviceId)
            val stops = assignmentService.stopDrop(userContext.serviceId)

            shuttles.forEach {
                options.shuttleOptions.put(it.shuttleID, it.shuttleName)
            }

            drivers.forEach {
                options.driverOptions.put(it.driverID, "${it.driverFName} ${it.driverLName}")
            }

            routes.forEach {
                val routeStops = assignmentService.retrieveRouteStops(it.routeID)
                val routeDetails = RouteDetailsAdapter()

                routeDetails.name = it.routeName
                routeDetails.routeId = it.routeID

                val stopDetailsAdapters = arrayListOf<StopDetailsAdapter>()
                routeStops.forEach {
                    val stopDetails = StopDetailsAdapter()

                    stopDetails.name = it.stopName
                    stopDetails.address = it.address
                    stopDetails.stopId = it.stopID
                    stopDetails.lat = it.latitude
                    stopDetails.long = it.longitude

                    stopDetailsAdapters.add(stopDetails)
                }

                routeDetails.stops = stopDetailsAdapters
                options.routeOptions.put(it.routeID, routeDetails)
            }

            stops.forEach {
                val stopDetails = StopDetailsAdapter()

                stopDetails.name = it.stopName
                stopDetails.address = it.address
                stopDetails.stopId = it.stopID
                stopDetails.lat = it.latitude
                stopDetails.long = it.longitude

                options.stopOptions.put(it.stopID, stopDetails)
            }

            return ResponseEntity(options, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
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