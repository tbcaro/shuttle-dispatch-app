package com.polaris.app.dispatch.repository

import com.polaris.app.dispatch.repository.entity.RouteEntity
import com.polaris.app.dispatch.repository.entity.RouteStopEntity
import com.polaris.app.dispatch.service.bo.NewRoute
import com.polaris.app.dispatch.service.bo.Route

interface RouteRepository{
    fun findRoutes(serviceID: Int): List<RouteEntity>
    fun findRouteStops(route: RouteEntity): List<RouteStopEntity>
    fun insertRoute(route: NewRoute): Int
    fun insertRouteStop(routeID: Int, stopID: Int, index: Int)
    fun updateRoute(route: NewRoute)
    fun deleteRouteStops(routeID: Int)
    fun updateAssignments(routeID: Int)
    fun archiveRoute(routeID: Int)
}