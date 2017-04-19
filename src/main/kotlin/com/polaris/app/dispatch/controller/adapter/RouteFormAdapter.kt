package com.polaris.app.dispatch.controller.adapter

import com.polaris.app.dispatch.controller.adapter.form.FormAdapter
import com.polaris.app.dispatch.controller.adapter.form.FormInt
import com.polaris.app.dispatch.controller.adapter.form.FormString
import com.polaris.app.dispatch.service.bo.NewRoute
import com.polaris.app.dispatch.service.bo.NewRouteStop
import com.polaris.app.dispatch.service.bo.RouteStop
import com.polaris.app.dispatch.service.bo.UpdateRoute
import sun.security.jca.ServiceId


class RouteFormAdapter : FormAdapter {
    override var hasErrors: Boolean
    var routeId: FormInt
    var routeName: FormString
    var routeStopForms: List<RouteStopFormAdapter>

    constructor() {
        this.hasErrors = false
        this.routeId = FormInt(0)
        this.routeName = FormString("")
        this.routeStopForms = arrayListOf()
    }

    fun toNewRoute(serviceId: Int): NewRoute {
        val newRouteStops = arrayListOf<NewRouteStop>()

        this.routeStopForms.forEach {
            newRouteStops.add(NewRouteStop(
                    stopID = it.stopId.value,
                    index = it.index.value
            ))
        }

        return NewRoute(
                serviceID = serviceId,
                routeName = this.routeName.value,
                stops = newRouteStops
        )
    }

    fun toUpdateRoute(serviceId: Int): UpdateRoute{
        val newRouteStops = arrayListOf<NewRouteStop>()

        this.routeStopForms.forEach {
            newRouteStops.add(NewRouteStop(
                    stopID = it.stopId.value,
                    index = it.index.value
            ))
        }

        return UpdateRoute(
                serviceID = serviceId,
                routeID = this.routeId.value,
                routeName = this.routeName.value,
                stops = newRouteStops
        )
    }
}