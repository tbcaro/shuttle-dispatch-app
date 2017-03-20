package com.polaris.app.dispatch.service.bo

import java.time.LocalTime

data class MapAssignmentStop(
        val stopName: String,
        val stopAddress: String,
        val stopLat: String,
        val stopLong: String,
        val stopArrive: LocalTime,
        val stopDepart: LocalTime,
        val stopArriveEst: LocalTime,
        val stopDepartEst: LocalTime
)