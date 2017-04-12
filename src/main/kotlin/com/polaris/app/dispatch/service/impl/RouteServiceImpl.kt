package com.polaris.app.dispatch.service.impl

import com.polaris.app.dispatch.repository.RouteRepository
import com.polaris.app.dispatch.service.RouteService
import com.polaris.app.dispatch.service.bo.NewRoute
import com.polaris.app.dispatch.service.bo.Route
import com.polaris.app.dispatch.service.bo.RouteStop
import com.polaris.app.dispatch.service.bo.Stop
import org.springframework.transaction.annotation.Transactional

class RouteServiceImpl(val routeRepository: RouteRepository): RouteService{
    override fun retrieveRoutes(serviceID: Int): List<Route> {
        val routes = arrayListOf<Route>()
        val routeEntities = this.routeRepository.findRoutes(serviceID)

        routeEntities.forEach{
            val routeStops = arrayListOf<RouteStop>()
            val routeStopEntities = this.routeRepository.findRouteStops(it)

            routeStopEntities.forEach {
                val routeStop = RouteStop(
                        stopID = it.stopID,
                        index = it.index,
                        name = it.name,
                        address = it.address,
                        latitude = it.latitude,
                        longitude = it.longitude
                )
                routeStops.add(routeStop)
            }
            val route = Route(
                    serviceID = it.serviceID,
                    routeID = it.routeID,
                    routeName = it.routeName,
                    stops = routeStops
            )
            routes.add(route)
        }
        return routes
    }

    @Transactional
    override fun addRoute(route: NewRoute) {
        val routeID = this.routeRepository.insertRoute(route)
        route.stops.forEach {
            this.routeRepository.insertRouteStop(routeID, it.stopID, it.index)
        }
    }

    @Transactional
    override fun updateRoute(route: NewRoute) {
        this.routeRepository.updateRoute(route)
        this.routeRepository.deleteRouteStops(route.routeID)
        route.stops.forEach {
            this.routeRepository.insertRouteStop(route.routeID, it.stopID, it.index)
        }
        this.routeRepository.updateAssignments(route.routeID)
    }

    override fun archiveRoute(routeID: Int) {
        this.routeRepository.archiveRoute(routeID)
    }
}