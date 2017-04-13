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
import java.math.BigDecimal
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

            val routeDetails1 = RouteDetailsAdapter()
            routeDetails1.routeId = 1
            routeDetails1.name = "Downtown Loop"
            routeDetails1.stops = arrayListOf<RouteStopDetailsAdapter>(
                    RouteStopDetailsAdapter(stop1, 0),
                    RouteStopDetailsAdapter(stop3, 1)
            )

            val routeDetails2 = RouteDetailsAdapter()
            routeDetails2.routeId = 2
            routeDetails2.name = "Shuttle-to-Airport"
            routeDetails2.stops = arrayListOf(
                    RouteStopDetailsAdapter(stop2, 0),
                    RouteStopDetailsAdapter(stop1, 1),
                    RouteStopDetailsAdapter(stop3, 2)
            )

            val routeDetails3 = RouteDetailsAdapter()
            routeDetails3.routeId = 3
            routeDetails3.name = "Another Saved Route"
            routeDetails3.stops = arrayListOf(
                    RouteStopDetailsAdapter(stop3, 0),
                    RouteStopDetailsAdapter(stop2, 1)
            )
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
            return ResponseEntity(arrayListOf(routeDetails1, routeDetails2, routeDetails3), HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }

    @RequestMapping("/route/formOptions")
    fun routeForm(http: HttpServletRequest) : ResponseEntity<RouteFormOptionsAdapter> {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)
            val options = RouteFormOptionsAdapter()

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

            options.stopOptions.put(1, stop1)
            options.stopOptions.put(2, stop2)
            options.stopOptions.put(3, stop3)
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