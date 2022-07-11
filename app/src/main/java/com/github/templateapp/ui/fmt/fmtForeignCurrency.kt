package com.github.templateapp.ui.fmt

import com.github.templateapp.screenmodel.HomeScreenModel

fun fmtForeignCurrency(value: Double?, currency: HomeScreenModel.Companion.Item): String {
    // TODO Тут хотелось болшего
    if(value == null) return  ""
    val f = (value*100).toInt() / 100.0
    return "$f"
}