package com.polaris.app.dispatch.controller.adapter.form

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.datetime.joda.LocalDateTimeParser
import java.io.Serializable
import java.time.LocalDateTime
import java.time.LocalTime


class FormDateTime(
        @JsonDeserialize(using = LocalDateTimeDeserializer::class)
        value: LocalDateTime? = null
) : FormField<LocalDateTime?>(value), Serializable