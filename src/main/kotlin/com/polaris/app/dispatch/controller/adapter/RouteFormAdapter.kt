package com.polaris.app.dispatch.controller.adapter

import com.polaris.app.dispatch.controller.adapter.form.FormAdapter
import com.polaris.app.dispatch.controller.adapter.form.FormInt
import com.polaris.app.dispatch.controller.adapter.form.FormString


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
}