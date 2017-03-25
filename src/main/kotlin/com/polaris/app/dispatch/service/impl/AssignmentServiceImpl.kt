package com.polaris.app.dispatch.service.impl

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import com.polaris.app.dispatch.controller.adapter.enums.AssignmentFieldTags
import com.polaris.app.dispatch.controller.adapter.enums.AssignmentStopFieldTags
import com.polaris.app.dispatch.repository.AssignmentRepository
import com.polaris.app.dispatch.service.AssignmentService
import com.polaris.app.dispatch.service.bo.Assignment
import com.polaris.app.dispatch.service.bo.AssignmentStop
import com.polaris.app.dispatch.service.exception.AssignmentValidationException
import java.sql.Time
import java.util.*

class AssignmentServiceImpl(val AssignmentRepository: AssignmentRepository): AssignmentService{
    override fun retrieveAssignments(windowStart: Time, windowEnd: Time): List<Assignment> {
        val assignments = arrayListOf<Assignment>()
        val assignmentEntities = this.AssignmentRepository.findAssignments(0, windowStart, windowEnd)

        assignmentEntities.forEach{
            val assignmentStops = arrayListOf<AssignmentStop>()
            val assignmentStopEntities = this.AssignmentRepository.findAssignmentStops(it.assignmentID)
            assignmentStopEntities.forEach{
                val assignmentStop = AssignmentStop(
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
                    routeName = it.routeName,
                    driverID = it.driverID,
                    shuttleID = it.shuttleID,
                    stops = assignmentStops
            )
            assignments.add(assignment)
        }
        return assignments
    }

    override fun addAssignment(newAssignment: Assignment) {
        val errors: Multimap<AssignmentFieldTags, String> = HashMultimap.create()
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}