package com.polaris.app.dispatch.service.exception

import com.google.common.collect.Multimap
import com.polaris.app.dispatch.controller.adapter.enums.RouteFieldTags
import com.polaris.app.dispatch.controller.adapter.enums.StopFieldTags


class StopValidationException (val errors: Multimap<StopFieldTags, String>) : Exception()