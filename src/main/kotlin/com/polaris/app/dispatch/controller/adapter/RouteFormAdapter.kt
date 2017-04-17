package com.polaris.app.dispatch.controller.adapter

import com.google.common.collect.Multimap
import com.polaris.app.dispatch.controller.adapter.enums.RouteFieldTags
import com.polaris.app.dispatch.controller.adapter.form.FormAdapter
import com.polaris.app.dispatch.controller.adapter.form.FormInt
import com.polaris.app.dispatch.controller.adapter.form.FormString


class RouteFormAdapter : FormAdapter {
    override var hasErrors: Boolean

    var routeID: FormInt
    var name: FormString
    var stopForms: List<StopFormAdapter>

    constructor() {
        this.hasErrors = false
        this.routeID = FormInt(0)
        this.name = FormString("")
        this.stopForms = arrayListOf<StopFormAdapter>()
    }

    fun mapErrors(errors: Multimap<RouteFieldTags, String>) {
        //errors[AssignmentFieldTags.START_TIME].forEach {  }
    }
}