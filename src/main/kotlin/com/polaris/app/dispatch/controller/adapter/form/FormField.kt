package com.polaris.app.dispatch.controller.adapter.form

import java.io.Serializable
import java.util.*

abstract class FormField<T>(t: T) : Serializable{
    var value: T = t
    var errors: ArrayList<String> = arrayListOf()
}