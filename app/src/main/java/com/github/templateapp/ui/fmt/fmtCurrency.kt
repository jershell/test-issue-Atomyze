package com.github.templateapp.ui.fmt

import java.text.NumberFormat
import java.util.*

fun fmtCurrency(value: Double?): String {
    val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("ru", "RU"))
    return formatter.format(value)
}