package com.polaris.app.dispatch.service

import com.polaris.app.dispatch.service.bo.NewRoute
import com.polaris.app.dispatch.service.bo.Route
import com.polaris.app.dispatch.service.bo.Stop
import com.polaris.app.dispatch.service.bo.UpdateRoute

interface RouteService{
    fun retrieveRoutes(serviceID: Int): List<Route>
    fun addRoute(route: NewRoute): Int
    fun updateRoute(route: UpdateRoute): Int
    fun archiveRoute(routeID: Int)
    fun retrieveStops(service: Int): List<Stop>
}