package com.natiqhaciyef.prodocument.common.helpers

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun ticketTimeCalculator(from: String, to: String): String? {
    return if (!from.any { it.isLetter() } && !to.any { it.isLetter() }) {
        val preFrom = from.substring(0..1).toDouble()
        val sufFrom = from.substring(3..4).toDouble()

        val preTo = to.substring(0..1).toDouble()
        val sufTo = to.substring(3..4).toDouble()

        val fromX = preFrom * 60 + sufFrom
        val toX = preTo * 60 + sufTo
        val res = (toX - fromX) / 60

        fromDoubleToTimeLine(res)
    } else {
        null
    }
}

fun fromDateToDay(day: String): Int = if (day.startsWith("0")) 1 else 0


fun fromDoubleToTimeLine(d: Double = 7.5): String? {
    return if (d > 0.0) {
        val hours = d.toInt()
        val minutes = ((d % hours) * 60).toInt()
        "$hours hours\n$minutes minutes"
    } else {
        null
    }
}


fun monthToString(month: String) = when (month) {
    "01" -> {
        "January"
    }

    "02" -> {
        "February"
    }

    "03" -> {
        "March"
    }

    "04" -> {
        "April"
    }

    "05" -> {
        "May"
    }

    "06" -> {
        "June"
    }

    "07" -> {
        "July"
    }

    "08" -> {
        "August"
    }

    "09" -> {
        "September"
    }

    "10" -> {
        "October"
    }

    "11" -> {
        "November"
    }

    "12" -> {
        "December"
    }

    else -> "Time left"
}


fun examTimer(time: Int): String? {
    return if (time > 0) {
        if (time % 60 < 10 && time / 60 < 10)
            "0${time / 60} : 0${time % 60}"
        else if (time % 60 < 10 && time / 60 > 10)
            "${time / 60} : 0${time % 60}"
        else if (time % 60 > 10 && time / 60 < 10)
            "0${time / 60} : ${time % 60}"
        else
            "${time / 60} : ${time % 60}"
    } else {
        null
    }
}


fun majorStringToDateChanger(s: String = "01.12.2001 15:59"): String? {
    if (s.length != 16)
        return null

    if (s.any { it.isLetter() })
        return null

    val subDay = s.substring(0..1)
    val subMonth = s.substring(3..4)
    val subYear = s.substring(6..9)
    val subTime = s.substring(11..15)

    if (subDay.toInt() > 31 || subMonth.toInt() > 12 || subTime.substring(0..1)
            .toInt() > 23 || subTime.substring(3..4).toInt() > 60
    )
        return null


    if (fromStringToMappedTime(subTime) == null)
        return null

    val day = if (subDay.startsWith("0")) subDay[1] else subDay
    val month = monthToString(subMonth)
    val time = fromStringToMappedTime(subTime)

    return "$time ($day $month, $subYear)"
}

fun fromStringToMappedTime(time: String): String? {
    if (time.length != 5)
        return null

    val start = time.substring(0..1)

    return if (start.toInt() > 12) {
        "${start.toInt() - 12}:${time.substring(3..4)} PM"
    } else {
        if (time.startsWith("0")) "${time.substring(1 until time.length)} AM" else "$time AM"
    }
}

fun fromStringToMappedDay(date: String): String? {
    if (date.length != 5)
        return null

    val day = date.substring(0..1)
    val month = date.substring(3..4)

    if (day.toInt() > 31 || month.toInt() > 12)
        return null

    return "$day ${monthToString(month)}"
}


fun getNow(dateTime: LocalDateTime = LocalDateTime.now()): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    return formatter.format(dateTime)
}

fun String.stringToFormattedLocalDateTime(): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    return LocalDateTime.parse(this, formatter)
}