package com.polaris.app.dispatch.service.bo

data class UpdateRoute(
        val serviceID: Int,
        val routeID: Int?,
        val routeName: String?,
        val stops: List<NewRouteStop>
)