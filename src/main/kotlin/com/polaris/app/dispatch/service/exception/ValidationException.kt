package com.polaris.app.dispatch.service.exception

import com.google.common.collect.Multimap

class ValidationException<FIELD> (val errors: Multimap<FIELD, String>) : Exception {
}