package com.polaris.app.dispatch.controller.adapter

import java.math.BigDecimal


class RouteDetailsAdapter {
    var routeId: Int = 0
    var name: String? = ""
    var stops: List<RouteStopDetailsAdapter> = arrayListOf()
}