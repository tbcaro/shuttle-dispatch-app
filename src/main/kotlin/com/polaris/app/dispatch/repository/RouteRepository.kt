package com.polaris.app.dispatch.repository

import com.polaris.app.dispatch.repository.entity.RouteEntity
import com.polaris.app.dispatch.repository.entity.RouteStopEntity
import com.polaris.app.dispatch.repository.entity.StopEntity
import com.polaris.app.dispatch.service.bo.NewRoute
import com.polaris.app.dispatch.service.bo.Route
import com.polaris.app.dispatch.service.bo.UpdateRoute

interface RouteRepository{
    fun findRoutes(serviceID: Int): List<RouteEntity>
    fun findRouteStops(route: RouteEntity): List<RouteStopEntity>
    fun insertRoute(route: NewRoute): Int
    fun insertRouteStop(routeID: Int?, stopID: Int?, index: Int?)
    fun updateRoute(route: UpdateRoute): Int
    fun deleteRouteStops(routeID: Int?)
    fun updateAssignments(routeID: Int?)
    fun archiveRoute(routeID: Int)
    fun findStops(service: Int):List<StopEntity>
}