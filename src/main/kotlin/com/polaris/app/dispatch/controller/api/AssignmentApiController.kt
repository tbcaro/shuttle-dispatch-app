package com.polaris.app.dispatch.controller.api

import com.polaris.app.dispatch.controller.adapter.*
import com.polaris.app.dispatch.controller.adapter.enums.AssignmentState
import com.polaris.app.dispatch.service.AssignmentService
import com.polaris.app.dispatch.service.AuthenticationService
import com.polaris.app.dispatch.service.exception.AssignmentValidationException
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

                if(it.routeName == null || it.routeName.isNullOrEmpty())
                    assignmentDetails.routeName = "Custom Route"
                else
                    assignmentDetails.routeName = it.routeName

                assignmentDetails.driverId = it.driverID
                assignmentDetails.driverName = "${it.driverFName} ${it.driverLName}"
                assignmentDetails.routeId = it.routeID
                assignmentDetails.shuttleId = it.shuttleID
                assignmentDetails.shuttleName = it.shuttleName
                assignmentDetails.startTime = it.startTime

                val report = AssignmentReport()
                val stops = arrayListOf<AssignmentStopAdapter>()
                report.assignmentId = it.assignmentID
                report.assignmentStatus = it.status

                for ((index, stop) in it.stops.withIndex()) {
                    val stopDetails = AssignmentStopAdapter()

                    if(stop.stopName == null || stop.stopName.isNullOrEmpty())
                        stopDetails.name = "Custom Stop"
                    else
                        stopDetails.name = stop.stopName

                    stopDetails.stopId = stop.stopId
                    stopDetails.address = stop.stopAddress
                    stopDetails.lat = stop.stopLat
                    stopDetails.long = stop.stopLong
                    stopDetails.order = index
                    stopDetails.estArriveTime = stop.stopArriveEst?.toLocalTime()
                    stopDetails.estDepartTime = stop.stopDepartEst?.toLocalTime()
                    stopDetails.actualArriveTime = stop.stopArrive?.toLocalTime()
                    stopDetails.actualDepartTime = stop.stopDepart?.toLocalTime()

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

                val stopDetailsAdapters = arrayListOf<RouteStopDetailsAdapter>()
                routeStops.forEach {
                    val stopDetails = StopDetailsAdapter()

                    stopDetails.name = it.stopName
                    stopDetails.address = it.address
                    stopDetails.stopId = it.stopID
                    stopDetails.lat = it.latitude
                    stopDetails.long = it.longitude

                    stopDetailsAdapters.add(RouteStopDetailsAdapter(stopDetails, it.index))
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
            http: HttpServletRequest,
            @RequestBody form: AssignmentFormAdapter
    ) : ResponseEntity<Int> {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)
            var assignmentId = 0

            if (form.assignmentId.value == null) {
                // TBC : Do create
                try {
                    assignmentId = assignmentService.addAssignment(form.toNewAssignment(userContext.serviceId))
                } catch (ex: AssignmentValidationException) {
                    // TODO : Map errors
                }
            } else {
                // TBC : Do update
                try {
                    assignmentId = assignmentService.updateAssignment(form.toAssignmentUpdate(userContext.serviceId))
                } catch (ex: AssignmentValidationException) {
                    // TODO : Map errors
                }
            }

            return ResponseEntity(assignmentId, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }

    @RequestMapping("/assignment/archive", method = arrayOf(RequestMethod.POST))
    fun archiveAssignment(
            @RequestBody archiveAdapter: AssignmentArchiveAdapter
    ) : ResponseEntity<Int> {
        try {
            assignmentService.archiveAssignment(archiveAdapter.assignmentId)
        } catch (ex: Exception) { }
        return ResponseEntity(archiveAdapter.assignmentId, HttpStatus.OK)
    }
}