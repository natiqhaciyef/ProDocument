package com.natiqhaciyef.common.helpers

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.natiqhaciyef.common.constants.ELEVEN
import com.natiqhaciyef.common.constants.FIFTEEN
import com.natiqhaciyef.common.constants.FIVE
import com.natiqhaciyef.common.constants.FORMATTED_DATE_TIME
import com.natiqhaciyef.common.constants.FOUR
import com.natiqhaciyef.common.constants.NINE
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.SIX
import com.natiqhaciyef.common.constants.SIXTEEN
import com.natiqhaciyef.common.constants.SIXTY
import com.natiqhaciyef.common.constants.TEN
import com.natiqhaciyef.common.constants.THIRTY_ONE
import com.natiqhaciyef.common.constants.THREE
import com.natiqhaciyef.common.constants.TWELVE
import com.natiqhaciyef.common.constants.TWENTY_THREE
import com.natiqhaciyef.common.constants.ZERO
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun ticketTimeCalculator(from: String, to: String): String? {
    return if (!from.any { it.isLetter() } && !to.any { it.isLetter() }) {
        val preFrom = from.substring(ZERO..ONE).toDouble()
        val sufFrom = from.substring(THREE..FOUR).toDouble()

        val preTo = to.substring(ZERO..ONE).toDouble()
        val sufTo = to.substring(THREE..FOUR).toDouble()

        val fromX = preFrom * SIXTY + sufFrom
        val toX = preTo * SIXTY + sufTo
        val res = (toX - fromX) / SIXTY

        fromDoubleToTimeLine(res)
    } else {
        null
    }
}

fun fromDateToDay(day: String): Int = if (day.startsWith("$ZERO")) ONE else ZERO


fun fromDoubleToTimeLine(d: Double = 7.5): String? {
    return if (d > ZERO.toDouble()) {
        val hours = d.toInt()
        val minutes = ((d % hours) * SIXTY).toInt()
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
    return if (time > ZERO) {
        if (time % SIXTY < TEN && time / SIXTY < TEN)
            "0${time / SIXTY} : 0${time % SIXTY}"
        else if (time % SIXTY < TEN && time / SIXTY > TEN)
            "${time / SIXTY} : 0${time % SIXTY}"
        else if (time % SIXTY > TEN && time / SIXTY < TEN)
            "0${time / SIXTY} : ${time % SIXTY}"
        else
            "${time / SIXTY} : ${time % SIXTY}"
    } else {
        null
    }
}


fun majorStringToDateChanger(s: String = "01.12.2001 15:59"): String? {
    if (s.length != SIXTEEN)
        return null

    if (s.any { it.isLetter() })
        return null

    val subDay = s.substring(ZERO..ONE)
    val subMonth = s.substring(THREE..FOUR)
    val subYear = s.substring(SIX..NINE)
    val subTime = s.substring(ELEVEN..FIFTEEN)

    if (subDay.toInt() > THIRTY_ONE || subMonth.toInt() > TWELVE || subTime.substring(ZERO..ONE)
            .toInt() > TWENTY_THREE || subTime.substring(THREE..FOUR).toInt() > SIXTY
    )
        return null


    if (fromStringToMappedTime(subTime) == null)
        return null

    val day = if (subDay.startsWith("$ZERO")) subDay[ONE] else subDay
    val month = monthToString(subMonth)
    val time = fromStringToMappedTime(subTime)

    return "$time ($day $month, $subYear)"
}

fun fromStringToMappedTime(time: String): String? {
    if (time.length != FIVE)
        return null

    val start = time.substring(ZERO..ONE)

    return if (start.toInt() > TWELVE) {
        "${start.toInt() - TWELVE}:${time.substring(THREE..FOUR)} PM"
    } else {
        if (time.startsWith("$ZERO")) "${time.substring(ONE until time.length)} AM" else "$time AM"
    }
}

fun fromStringToMappedDay(date: String): String? {
    if (date.length != FIVE)
        return null

    val day = date.substring(ZERO..ONE)
    val month = date.substring(THREE..FOUR)

    if (day.toInt() > THIRTY_ONE || month.toInt() > TWELVE)
        return null

    return "$day ${monthToString(month)}"
}


@SuppressLint("NewApi")
fun getNow(dateTime: LocalDateTime = LocalDateTime.now()): String {
    val formatter = DateTimeFormatter.ofPattern(FORMATTED_DATE_TIME)
    return formatter.format(dateTime)
}

@SuppressLint("NewApi")
fun String.stringToFormattedLocalDateTime(): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern(FORMATTED_DATE_TIME)
    return LocalDateTime.parse(this, formatter)
}