package com.polaris.app.dispatch.controller.adapter

import com.polaris.app.dispatch.controller.adapter.form.*
import java.time.LocalTime


class AssignmentStopFormAdapter : FormAdapter {
    override var hasErrors: Boolean

    var stopId: FormInt
    var index: FormInt
    var name: FormString
    var address: FormString
//    var estArriveTime: FormTime
//    var estDepartTime: FormTime

    constructor() {
        this.hasErrors = false

        this.stopId = FormInt(null)
        this.index = FormInt(0)
        this.name = FormString("")
        this.address = FormString("")
//        this.estArriveTime = FormTime(null)
//        this.estDepartTime = FormTime(null)
    }
}