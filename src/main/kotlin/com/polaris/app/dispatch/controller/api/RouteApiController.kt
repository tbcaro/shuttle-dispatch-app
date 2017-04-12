package com.polaris.app.dispatch.controller.api

import com.polaris.app.dispatch.controller.adapter.RouteFormAdapter
import com.polaris.app.dispatch.controller.adapter.*
import com.polaris.app.dispatch.service.AssignmentService
import com.polaris.app.dispatch.service.AuthenticationService
import com.polaris.app.dispatch.service.exception.AssignmentValidationException
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api")
class RouteApiController(private val authService: AuthenticationService) {

    @RequestMapping("/fetchAllRoutes")
    fun fetchAllRoutes(
            http: HttpServletRequest
    ) : ResponseEntity<List<RouteDetailsAdapter>> {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)
//            val assignmentListAdapter = AssignmentListAdapter()
//            val detailsAdapters = arrayListOf<AssignmentDetailsAdapter>()
//            assignmentListAdapter.selectedDate = date
//
//            val assignments = assignmentService.retrieveAssignments(userContext.serviceId, date ?: LocalDate.now())
//            assignments.forEach {
//                val assignmentDetails = AssignmentDetailsAdapter()
//
//                assignmentDetails.driverId = it.driverID
//                assignmentDetails.driverName = "${it.driverFName} ${it.driverLName}"
//                assignmentDetails.routeId = it.routeID
//                assignmentDetails.routeName = it.routeName
//                assignmentDetails.shuttleId = it.shuttleID
//                assignmentDetails.shuttleName = it.shuttleName
//                assignmentDetails.startTime = it.startTime
//
//                val report = AssignmentReport()
//                val stops = arrayListOf<AssignmentStopAdapter>()
//                report.assignmentId = it.assignmentID
//                // TODO : Possibly return assignmentStatus as well
//
//                it.stops.forEach {
//                    val stopDetails = AssignmentStopAdapter()
//
//                    stopDetails.stopId = it.stopId
//                    stopDetails.address = it.stopAddress
//                    stopDetails.name = it.stopName
//                    stopDetails.lat = it.stopLat
//                    stopDetails.long = it.stopLong
//                    stopDetails.estArriveTime = it.stopArriveEst.toLocalTime()
//                    stopDetails.estDepartTime = it.stopDepartEst.toLocalTime()
//                    stopDetails.actualArriveTime = it.stopArrive.toLocalTime()
//                    stopDetails.actualDepartTime = it.stopDepart.toLocalTime()
//
//                    stops.add(stopDetails)
//                }
//
//                report.assignmentStops = stops
//                assignmentDetails.assignmentReport = report
//                detailsAdapters.add(assignmentDetails)
//            }
//
//            assignmentListAdapter.assignmentDetailAdapters = detailsAdapters
            return ResponseEntity(arrayListOf<RouteDetailsAdapter>(), HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }

    @RequestMapping("/route/formOptions")
    fun routeForm(http: HttpServletRequest) : ResponseEntity<RouteFormOptionsAdapter> {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)
            val options = RouteFormOptionsAdapter()
//
//            val shuttles = assignmentService.shuttleDrop(userContext.serviceId)
//            val drivers = assignmentService.driverDrop(userContext.serviceId)
//            val routes = assignmentService.routeDrop(userContext.serviceId)
//            val stops = assignmentService.stopDrop(userContext.serviceId)
//
//            shuttles.forEach {
//                options.shuttleOptions.put(it.shuttleID, it.shuttleName)
//            }
//
//            drivers.forEach {
//                options.driverOptions.put(it.driverID, "${it.driverFName} ${it.driverLName}")
//            }
//
//            routes.forEach {
//                val routeStops = assignmentService.retrieveRouteStops(it.routeID)
//                val routeDetails = RouteDetailsAdapter()
//
//                routeDetails.name = it.routeName
//                routeDetails.routeId = it.routeID
//
//                val stopDetailsAdapters = arrayListOf<StopDetailsAdapter>()
//                routeStops.forEach {
//                    val stopDetails = StopDetailsAdapter()
//
//                    stopDetails.name = it.stopName
//                    stopDetails.address = it.address
//                    stopDetails.stopId = it.stopID
//                    stopDetails.lat = it.latitude
//                    stopDetails.long = it.longitude
//
//                    stopDetailsAdapters.add(stopDetails)
//                }
//
//                routeDetails.stops = stopDetailsAdapters
//                options.routeOptions.put(it.routeID, routeDetails)
//            }
//
//            stops.forEach {
//                val stopDetails = StopDetailsAdapter()
//
//                stopDetails.name = it.stopName
//                stopDetails.address = it.address
//                stopDetails.stopId = it.stopID
//                stopDetails.lat = it.latitude
//                stopDetails.long = it.longitude
//
//                options.stopOptions.put(it.stopID, stopDetails)
//            }

            return ResponseEntity(options, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }

    @RequestMapping("/route/save", method = arrayOf(RequestMethod.POST))
    fun saveRoute(
            http: HttpServletRequest,
            @RequestBody form: RouteFormAdapter
    ) : ResponseEntity<Int> {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)
            var routeId = 0

//            if (form.assignmentId.value == null) {
//                // TBC : Do create
//                try {
//                    assignmentId = assignmentService.addAssignment(form.toNewAssignment(userContext.serviceId))
//                } catch (ex: AssignmentValidationException) {
//
//                }
//            } else {
//                // TBC : Do update
//                assignmentId = 1
//            }

            return ResponseEntity(routeId, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }

    @RequestMapping("/route/archive", method = arrayOf(RequestMethod.POST))
    fun archiveRoute(
            @RequestBody archiveAdapter: RouteArchiveAdapter
    ) : ResponseEntity<Int> {
        return ResponseEntity(0, HttpStatus.OK)
    }
}