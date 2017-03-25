package com.polaris.app.dispatch.controller.adapter

import java.time.LocalTime


class AssignmentStopFormAdapter : FormAdapter {
    override var hasErrors: Boolean

    var stopId: Field<Int?>
    var index: Field<Int>
    var name: Field<String>
    var address: Field<String>
    var estArriveTime: Field<LocalTime>?
    var estDepartTime: Field<LocalTime>?

    constructor() {
        this.hasErrors = false

        this.stopId = Field<Int?>(null)
        this.index = Field<Int>(0)
        this.name = Field<String>("")
        this.address = Field<String>("")
        this.estArriveTime = null
        this.estDepartTime = null
    }
}