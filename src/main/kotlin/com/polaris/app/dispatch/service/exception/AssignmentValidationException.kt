package com.polaris.app.dispatch.service.exception

import com.google.common.collect.Multimap
import com.polaris.app.dispatch.controller.adapter.enums.AssignmentFieldTags
import com.polaris.app.dispatch.controller.adapter.enums.AssignmentStopFieldTags

class AssignmentValidationException (
        val errors: Multimap<AssignmentFieldTags, String>,
        val stopErrors: Map<Int, Multimap<AssignmentStopFieldTags, String>> // TBC : Map error list to index of assignment stop
        ) : Exception()