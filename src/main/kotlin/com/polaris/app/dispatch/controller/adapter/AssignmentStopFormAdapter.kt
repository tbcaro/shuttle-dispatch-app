package com.polaris.app.dispatch.controller.adapter

import com.polaris.app.dispatch.controller.adapter.form.*
import java.math.BigDecimal
import java.time.LocalTime


class AssignmentStopFormAdapter : FormAdapter {
    override var hasErrors: Boolean

    var stopId: FormInt
    var index: FormInt
    var name: FormString
    var address: FormString
    var latitude: FormBigDecimal
    var longitude: FormBigDecimal
    var estArriveTime: FormDateTime
    var estDepartTime: FormDateTime

    constructor() {
        this.hasErrors = false

        this.stopId = FormInt(null)
        this.index = FormInt(0)
        this.name = FormString("")
        this.address = FormString("")
        this.latitude = FormBigDecimal(BigDecimal("0"))
        this.longitude = FormBigDecimal(BigDecimal("0"))
        this.estArriveTime = FormDateTime(null)
        this.estDepartTime = FormDateTime(null)
    }
}