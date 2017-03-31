package com.polaris.app.dispatch.repository.entity

import java.math.BigDecimal
import java.time.LocalDateTime

data class MapAssignStopEntity(
        val assignmentStopId: Int,
        val stopId: Int?,
        val index: Int,
        val stopName: String,//Displayed
        val stopAddress: String,//Displayed
        val stopLat: BigDecimal,//May not be necessary
        val stopLong: BigDecimal,//May not be necessary
        val stopArrive: LocalDateTime,//May not be necessary
        val stopDepart: LocalDateTime,//Displayed when applicable
        val stopArriveEst: LocalDateTime,//Displayed
        val stopDepartEst: LocalDateTime//Used to determine EWT
)