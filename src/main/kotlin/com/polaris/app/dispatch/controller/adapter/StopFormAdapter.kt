package com.polaris.app.dispatch.controller.adapter

import com.polaris.app.dispatch.controller.adapter.form.FormAdapter
import com.polaris.app.dispatch.controller.adapter.form.FormBigDecimal
import com.polaris.app.dispatch.controller.adapter.form.FormInt
import com.polaris.app.dispatch.controller.adapter.form.FormString
import java.math.BigDecimal


class StopFormAdapter : FormAdapter {
    override var hasErrors: Boolean

    var stopId: FormInt
    var name: FormString
    var address: FormString
    var lat: FormBigDecimal
    var long: FormBigDecimal

    constructor() {
        this.hasErrors = false
        this.stopId = FormInt(0)
        this.name = FormString("")
        this.address = FormString("")
        this.lat = FormBigDecimal(BigDecimal("0"))
        this.long = FormBigDecimal(BigDecimal("0"))
    }
}