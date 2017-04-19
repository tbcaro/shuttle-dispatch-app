package com.polaris.app.dispatch.service.bo

import java.math.BigDecimal
import java.time.LocalTime

data class NewAssignmentStop(
        val stopArriveEst: LocalTime?,
        val stopDepartEst: LocalTime?,
        val stopID: Int?,
        val stopAddress: String?,
        val stopIndex: Int?,
        val stopLat: BigDecimal?,
        val stopLong: BigDecimal?
)