package com.polaris.app.dispatch.service.impl

import com.polaris.app.dispatch.repository.AssignmentRepository
import com.polaris.app.dispatch.service.AssignmentService
import com.polaris.app.dispatch.service.bo.Assignment
import com.polaris.app.dispatch.service.bo.AssignmentStop
import java.sql.Time

class AssignmentServiceImpl(val AssignmentRepository: AssignmentRepository): AssignmentService{
    override fun retrieveAssignments(windowStart: Time, windowEnd: Time): List<Assignment> {
        val assignments = arrayListOf<Assignment>()
        val assignmentEntities = this.AssignmentRepository.findAssignments(windowStart, windowEnd)

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}