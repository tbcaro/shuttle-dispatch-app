package com.polaris.app.dispatch.service.bo

data class NewRoute(
        val serviceID: Int,
        val routeName: String?,
        val stops: List<NewRouteStop>
)