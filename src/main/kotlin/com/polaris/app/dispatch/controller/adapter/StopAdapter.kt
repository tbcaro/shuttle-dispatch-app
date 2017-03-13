package com.polaris.app.dispatch.controller.adapter

import java.math.BigDecimal
import java.time.LocalTime
import java.time.temporal.ChronoUnit


class StopAdapter {
    var order: Int = 0
    var name: String = ""
    var address: String = ""
    var lat: BigDecimal = BigDecimal("0")
    var long: BigDecimal = BigDecimal("0")
    var estArriveTime: LocalTime = LocalTime.now()
    var estDepartTime: LocalTime = LocalTime.now()
    var actualArriveTime: LocalTime? = null
    var actualDepartTime: LocalTime? = null
    val estWaitTime: Long by lazy {
        ChronoUnit.MINUTES.between(this.estArriveTime, this.estDepartTime)
    }
}