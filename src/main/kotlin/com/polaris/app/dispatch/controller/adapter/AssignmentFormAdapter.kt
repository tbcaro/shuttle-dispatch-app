package com.polaris.app.dispatch.controller.adapter

import com.fasterxml.jackson.annotation.JsonFormat
import com.google.common.collect.Multimap
import com.polaris.app.dispatch.controller.adapter.enums.AssignmentFieldTags
import com.polaris.app.dispatch.controller.adapter.form.*
import com.polaris.app.dispatch.service.bo.AssignmentStop
import com.polaris.app.dispatch.service.bo.AssignmentUpdate
import com.polaris.app.dispatch.service.bo.NewAssignment
import com.polaris.app.dispatch.service.bo.NewAssignmentStop
import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime


class AssignmentFormAdapter : FormAdapter {
    override var hasErrors: Boolean

    var assignmentId: FormInt
    var shuttleId: FormInt
    var driverId: FormInt
    var routeId: FormInt
    var startTime: FormDateTime
    var assignmentStopForms: List<AssignmentStopFormAdapter>

    constructor() {
        this.hasErrors = false
        this.assignmentId = FormInt(0)
        this.shuttleId = FormInt(0)
        this.driverId = FormInt(0)
        this.routeId = FormInt(0)
        this.startTime = FormDateTime(null)
        this.assignmentStopForms = arrayListOf<AssignmentStopFormAdapter>()
    }

    fun mapErrors(errors: Multimap<AssignmentFieldTags, String>) {
        //errors[AssignmentFieldTags.START_TIME].forEach {  }
    }

    fun toNewAssignment(serviceId: Int): NewAssignment {
        val newAssignmentStops = arrayListOf<NewAssignmentStop>()

        this.assignmentStopForms.forEach {
            newAssignmentStops.add(NewAssignmentStop(
                    stopArriveEst = it.estArriveTime.value?.toLocalTime(),
                    stopDepartEst = it.estDepartTime.value?.toLocalTime(),
                    stopID = it.stopId.value,
                    stopAddress = it.address.value,
                    stopIndex = it.index.value,
                    stopLat = it.latitude.value,
                    stopLong = it.longitude.value
            ))
        }

        return NewAssignment(
                serviceID = serviceId,
                driverID = this.driverId.value,
                shuttleID = this.shuttleId.value,
                routeID = this.routeId.value,
                startDate = this.startTime.value?.toLocalDate(),
                startTime = this.startTime.value?.toLocalTime(),
                stops = newAssignmentStops
        )
    }

    fun toAssignmentUpdate(serviceId: Int): AssignmentUpdate {
        val newAssignmentStops = arrayListOf<NewAssignmentStop>()

        this.assignmentStopForms.forEach {
            newAssignmentStops.add(NewAssignmentStop(
                    stopArriveEst = it.estArriveTime.value?.toLocalTime(),
                    stopDepartEst = it.estDepartTime.value?.toLocalTime(),
                    stopID = it.stopId.value,
                    stopAddress = it.address.value,
                    stopIndex = it.index.value,
                    stopLat = it.latitude.value,
                    stopLong = it.longitude.value
            ))
        }

        return AssignmentUpdate(
                assignmentID = this.assignmentId.value,
                serviceID = serviceId,
                driverID = this.driverId.value,
                shuttleID = this.shuttleId.value,
                routeID = this.routeId.value,
                startDate = this.startTime.value?.toLocalDate(),
                startTime = this.startTime.value?.toLocalTime(),
                stops = newAssignmentStops
        )
    }
}