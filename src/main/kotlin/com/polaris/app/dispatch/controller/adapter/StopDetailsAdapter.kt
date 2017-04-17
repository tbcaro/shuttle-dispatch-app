package com.polaris.app.dispatch.controller.adapter

import java.math.BigDecimal


class StopDetailsAdapter {
    var stopId: Int = 0
    var name: String? = ""
    var address: String? = ""
    var lat: BigDecimal = BigDecimal("0")
    var long: BigDecimal = BigDecimal("0")
}