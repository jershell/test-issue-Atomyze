package com.github.templateapp.serialization.models

import kotlinx.serialization.Serializable

@Serializable
data class Currency(
    val ID: String,
    val NumCode: String? = null,
    val CharCode: String? = null,
    val Nominal: Long? = 1,
    val Name: String? = null,
    val Value: Double? = null,
    val Previous: Double? = null
)
