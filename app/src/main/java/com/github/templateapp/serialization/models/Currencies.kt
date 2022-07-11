package com.github.templateapp.serialization.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Currencies(
    @SerialName("Valute")
    val all: Map<String, Currency> = emptyMap()
)
