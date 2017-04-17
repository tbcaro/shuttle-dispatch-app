package com.polaris.app.dispatch.controller.adapter

import com.google.common.collect.Multimap
import com.polaris.app.dispatch.controller.adapter.enums.StopFieldTags
import com.polaris.app.dispatch.controller.adapter.form.FormAdapter
import com.polaris.app.dispatch.controller.adapter.form.FormBigDecimal
import com.polaris.app.dispatch.controller.adapter.form.FormInt
import com.polaris.app.dispatch.controller.adapter.form.FormString
import java.math.BigDecimal


class StopFormAdapter : FormAdapter {
    override var hasErrors: Boolean

    var stopId: FormInt
    var name: FormString
    var address: FormString
    var latitude: FormBigDecimal
    var longitude: FormBigDecimal

    constructor() {
        this.hasErrors = false
        this.stopId = FormInt(0)
        this.name = FormString("")
        this.address = FormString("")
        this.latitude = FormBigDecimal(BigDecimal("0"))
        this.longitude = FormBigDecimal(BigDecimal("0"))
    }

    fun mapErrors(errors: Multimap<StopFieldTags, String>) {
        //errors[AssignmentFieldTags.START_TIME].forEach {  }
    }




}