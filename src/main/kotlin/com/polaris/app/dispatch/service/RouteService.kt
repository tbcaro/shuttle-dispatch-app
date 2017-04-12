package com.polaris.app.dispatch.service

import com.polaris.app.dispatch.service.bo.NewRoute
import com.polaris.app.dispatch.service.bo.Route

interface RouteService{
    fun retrieveRoutes(serviceID: Int): List<Route>
    fun addRoute(route: NewRoute)
    fun updateRoute(route: NewRoute)
    fun archiveRoute(routeID: Int)
}