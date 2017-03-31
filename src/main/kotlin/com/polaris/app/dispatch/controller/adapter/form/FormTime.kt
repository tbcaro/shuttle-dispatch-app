package com.polaris.app.dispatch.controller.adapter.form

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer
import java.io.Serializable
import java.time.LocalTime


class FormTime(
        @JsonDeserialize(using = LocalTimeDeserializer::class)
        @JsonSerialize(using = LocalTimeSerializer::class)
        value: LocalTime? = null
) : FormField<LocalTime?>(value), Serializable