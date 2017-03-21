package com.polaris.app.dispatch.service.exception

import com.google.common.collect.Multimap
import com.polaris.app.dispatch.controller.adapter.enums.RouteFieldTags


class RouteValidationException (val errors: Multimap<RouteFieldTags, String>) : Exception()