package com.polaris.app.dispatch.controller.adapter

import com.polaris.app.dispatch.controller.adapter.form.*
import java.math.BigDecimal


class RouteStopDetailsAdapter{

    private var stopDetailsAdapter: StopDetailsAdapter

    var index: Int
    var stopId: Int = 0
    var name: String? = ""
    var address: String? = ""
    var lat: BigDecimal = BigDecimal("0")
    var long: BigDecimal = BigDecimal("0")

    constructor(stopDetailsAdapter: StopDetailsAdapter, index: Int) {
        this.stopDetailsAdapter = stopDetailsAdapter
        this.index = index
        this.stopId = this.stopDetailsAdapter.stopId
        this.name = this.stopDetailsAdapter.name
        this.address = this.stopDetailsAdapter.address
        this.lat = this.stopDetailsAdapter.lat
        this.long = this.stopDetailsAdapter.long
    }
}