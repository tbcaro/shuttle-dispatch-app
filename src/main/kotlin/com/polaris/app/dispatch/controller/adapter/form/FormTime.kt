package com.polaris.app.dispatch.controller.adapter.form

import java.io.Serializable
import java.time.LocalTime


class FormTime(value: LocalTime? = null) : FormField<LocalTime?>(value), Serializable