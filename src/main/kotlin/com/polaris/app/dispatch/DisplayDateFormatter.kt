package com.polaris.app.dispatch

import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*


class DisplayDateFormatter {

    fun format(d: LocalDate): String {
        // TBC : Build string 'DayOfWeek, Month dayNum, Year' -> 'Saturday, March 18, 2017
        var builder = StringBuilder()

        builder.append(d.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US))
        builder.append(", ")
        builder.append(d.month.getDisplayName(TextStyle.SHORT, Locale.US))
        builder.append(" ")
        builder.append(d.dayOfMonth)
        when (d.dayOfMonth.toString().get(d.dayOfMonth.toString().lastIndex)) {
            "1".toCharArray().first() -> builder.append("st")
            "2".toCharArray().first() -> builder.append("nd")
            "3".toCharArray().first() -> builder.append("rd")
            else -> {
                builder.append("th")
            }
        }
        builder.append(", ")
        builder.append(d.year)

        return builder.toString();
    }
}