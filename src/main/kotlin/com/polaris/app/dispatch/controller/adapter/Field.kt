package com.polaris.app.dispatch.controller.adapter

import java.util.*

class Field<T>(t: T) {
    var value: T = t
    var errors: ArrayList<String> = arrayListOf()
}