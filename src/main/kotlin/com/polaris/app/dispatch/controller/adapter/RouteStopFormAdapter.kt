package com.polaris.app.dispatch.controller.adapter

import com.polaris.app.dispatch.controller.adapter.form.*
import java.math.BigDecimal


class RouteStopFormAdapter : FormAdapter {
    override var hasErrors: Boolean

    var stopId: FormInt
    var index: FormInt

    constructor() {
        this.hasErrors = false

        this.stopId = FormInt(null)
        this.index = FormInt(0)
    }
}