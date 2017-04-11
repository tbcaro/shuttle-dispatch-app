package com.polaris.app.dispatch.service.impl

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import com.polaris.app.dispatch.controller.adapter.enums.AssignmentFieldTags
import com.polaris.app.dispatch.controller.adapter.enums.AssignmentState
import com.polaris.app.dispatch.controller.adapter.enums.AssignmentStopFieldTags
import com.polaris.app.dispatch.repository.AssignmentRepository
import com.polaris.app.dispatch.repository.entity.AssignmentStopEntity
import com.polaris.app.dispatch.service.AssignmentService
import com.polaris.app.dispatch.service.bo.*
import com.polaris.app.dispatch.service.exception.AssignmentValidationException
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.*

class AssignmentServiceImpl(val AssignmentRepository: AssignmentRepository): AssignmentService{
    override fun retrieveAssignments(service: Int, startDate: LocalDate): List<Assignment> {
        val assignments = arrayListOf<Assignment>()
        val assignmentEntities = this.AssignmentRepository.findAssignments(service, startDate)

        assignmentEntities.forEach{
            val assignmentStops = arrayListOf<AssignmentStop>()
            val assignmentStopEntities = this.AssignmentRepository.findAssignmentStops(it.assignmentID)
            assignmentStopEntities.forEach{
                val assignmentStop = AssignmentStop(
                        stopId = it.stopID,
                        stopName = it.stopName,
                        stopAddress = it.stopAddress,
                        stopLat = it.stopLat,
                        stopLong = it.stopLong,
                        stopArrive = it.stopArrive,
                        stopDepart = it.stopDepart,
                        stopArriveEst = it.stopArriveEst,
                        stopDepartEst = it.stopDepartEst
                )
                assignmentStops.add(assignmentStop)
            }
            val assignment = Assignment(
                    assignmentID = it.assignmentID,
                    startTime = it.startTime,
                    routeID = it.routeID,
                    routeName = it.routeName,
                    driverID = it.driverID,
                    driverFName = it.driverFName,
                    driverLName = it.driverLName,
                    shuttleID = it.shuttleID,
                    shuttleName = it.shuttleName,
                    stops = assignmentStops
            )
            assignments.add(assignment)
        }
        return assignments
    }


    override fun shuttleDrop(service: Int): List<AssignmentShuttle> {
        val shuttleDrops = arrayListOf<AssignmentShuttle>()
        val shuttleDropEntities = this.AssignmentRepository.findDropShuttles(service)

        shuttleDropEntities.forEach{
            val shuttle = AssignmentShuttle(
                    shuttleID = it.shuttleID,
                    shuttleName = it.shuttleName
            )
            shuttleDrops.add(shuttle)
        }
        return shuttleDrops
    }

    override fun driverDrop(service: Int): List<AssignmentDriver> {
        val driverDrops = arrayListOf<AssignmentDriver>()
        val driverDropEntities = this.AssignmentRepository.findDropDrivers(service)

        driverDropEntities.forEach{
            val driver = AssignmentDriver(
                    driverID = it.driverID,
                    driverFName = it.driverFName,
                    driverLName = it.driverLName
            )
            driverDrops.add(driver)
        }
        return driverDrops
    }

    override fun routeDrop(service: Int): List<AssignmentRoute> {
        val routeDrops = arrayListOf<AssignmentRoute>()
        val routeDropEntities = this.AssignmentRepository.findDropRoutes(service)

        routeDropEntities.forEach {
            val route = AssignmentRoute(
                    routeID = it.routeID,
                    routeName = it.routeName
            )
            routeDrops.add(route)
        }
        return routeDrops
    }

    override fun stopDrop(service: Int): List<AssignmentStopDrop> {
        val stopDrops = arrayListOf<AssignmentStopDrop>()
        val stopDropEntities = this.AssignmentRepository.findDropStops(service)

        stopDropEntities.forEach{
            val stop = AssignmentStopDrop(
                    stopID = it.stopID,
                    stopName = it.stopName,
                    address = it.address,
                    latitude = it.latitude,
                    longitude = it.longitude
            )
            stopDrops.add(stop)
        }
        return stopDrops
    }

    override fun retrieveRouteStops(routeid: Int): List<AssignmentRouteStop> {
        val routeStops = arrayListOf<AssignmentRouteStop>()
        val routeStopEntities = this.AssignmentRepository.findDropRouteStops(routeid)

        routeStopEntities.forEach{
            val routeStop = AssignmentRouteStop(
                    stopID = it.stopID,
                    stopName = it.stopName,
                    index = it.index,
                    address = it.address,
                    latitude = it.latitude,
                    longitude = it.longitude
            )
            routeStops.add(routeStop)
        }
        return routeStops
    }

    override fun addAssignment(newAssignment: NewAssignment): Int {
        val errors: Multimap<AssignmentFieldTags, String> = HashMultimap.create()
        val stopErrors: MutableMap<Int, Multimap<AssignmentStopFieldTags,String>> = HashMap()

        this.AssignmentRepository.startTransaction()
        val assignmentid = this.AssignmentRepository.addAssignment(newAssignment)
        this.AssignmentRepository.addAssignmentStops(assignmentid, newAssignment.stops)
        this.AssignmentRepository.endTransaction()

        return assignmentid

        //Implement error checking

        /*val errors: Multimap<AssignmentFieldTags, String> = HashMultimap.create()
        val stopErrors: MutableMap<Int, Multimap<AssignmentStopFieldTags, String>> = HashMap()

        if(true) {
            errors[AssignmentFieldTags.START_TIME].add("This start time sucks!")
            stopErrors.put(0, HashMultimap.create<AssignmentStopFieldTags, String>())

            // TBC : Tyler, the '!!' means that I am asserting that stopErrors[0] is not null because in the statement above I
            // created a multimap at that index.
            stopErrors[0]!![AssignmentStopFieldTags.TEST_ASSIGNMENT_STOP_FIELD].add("example assignment stop error")

        }

        if(!errors.isEmpty || stopErrors.isNotEmpty()) {
            throw AssignmentValidationException(errors, stopErrors)
        }
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.*/
    }

    override fun archiveAssignment(assignmentid: Int) {
        this.AssignmentRepository.archiveAssignment(assignmentid)
    }

    override fun updateAssignment(updatedAssignment: AssignmentUpdate): Boolean {
        val currentAssignment = this.AssignmentRepository.checkAssignment(updatedAssignment.assignmentID)
        if (currentAssignment.status == AssignmentState.IN_PROGRESS){
            //INCOMPLETE



            return true
        }
        else if (currentAssignment.status == AssignmentState.COMPLETED){
            return false
        }
        else/*UNFINISHED OR SCHEDULED*/{
            this.AssignmentRepository.startTransaction()
            this.AssignmentRepository.updateAssignment(updatedAssignment)
            this.AssignmentRepository.removeAssignmentStops(updatedAssignment.assignmentID, 0)
            this.AssignmentRepository.addAssignmentStops(updatedAssignment.assignmentID, updatedAssignment.stops)
            this.AssignmentRepository.endTransaction()
            return true
        }
    }
}