@file:OptIn(ExperimentalTime::class)

package com.github.templateapp.screenmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.github.templateapp.repository.CurrencyRepository
import com.github.templateapp.serialization.models.Currency
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

class HomeScreenModel(
    private val repository: CurrencyRepository
) : ScreenModel {

    companion object {
        data class Item(
            val id: String,
            val name: String,
            val code: String,
            val value: Double? = null,
            val previousValue: Double? = null,
            val faceValue: Long = 1,
            val recap: Double? = null,
        )

        fun Currency.toItem(quantity: Double): Item {
            return Item(
                id = ID,
                name = Name ?: CharCode ?: ID,
                code = CharCode ?: "",
                faceValue = Nominal ?: 1L,
                value = Value,
                previousValue = Value,
                recap = calculateRecap(quantity, Nominal ?: 1L, Value ?: Previous ?: 0.0),
            )
        }

        fun calculateRecap(quantity: Double, faceValue: Long, basePrice: Double): Double? {
            val recap = if (quantity > 0 && basePrice > 0) {
                (quantity / basePrice) * faceValue
            } else null

            return recap
        }
    }

    val all = MutableStateFlow<List<Item>>(emptyList())
    val timer = MutableStateFlow(0.0f)
    val isLoading = MutableStateFlow(false)
    val errors = MutableStateFlow(emptyList<Throwable>())
    val quantity = MutableStateFlow(0.0)

    private val refreshInterval = 10.seconds
    private var fetchDataJob: Job = Job().apply {
        complete()
    }

    fun tick() {
        if (timer.value > 1.0f && !fetchDataJob.isActive) {
            fetchDataJob = coroutineScope.launch {
                val result = repository.populate()
                if (result.isSuccess) {
                    result
                        .getOrNull()
                        ?.let {
                            all.value = it.values.toList().map { cur -> cur.toItem(quantity.value) }
                        }
                }
            }
        } else {
            val x = Date().time - repository.loadedAt.time
            timer.value = if (fetchDataJob.isActive) {
                0.0f
            } else {
                x.toFloat() / refreshInterval.inWholeMilliseconds
            }
        }
    }

    fun refresh(silent: Boolean = false) {
        fetchDataJob = coroutineScope.launch {
            isLoading.value = !silent
            repository.populate().getOrNull()
                ?.let { all.value = it.values.toList().map { cur -> cur.toItem(quantity.value) } }
            isLoading.value = false
        }
    }

    fun getCurrencies() {
        fetchDataJob = coroutineScope.launch {
            isLoading.value = true
            timer.value = 0.0f

            val result = repository.getAll()

            if (result.isSuccess) {
                result.getOrNull()?.let {
                    all.value = it.values.toList().map { cur -> cur.toItem(quantity.value) }
                }
            }

            if (result.isFailure) {
                result.exceptionOrNull()?.let { errors.value += listOf(it) }
            }

            isLoading.value = false
        }
    }

    fun setQuantity(value: Double) {
        quantity.value = value
        all.value = all.value.map {
            it.copy(
                recap = calculateRecap(
                    quantity = value,
                    faceValue = it.faceValue,
                    basePrice = it.value ?: it.previousValue ?: 0.0
                )
            )
        }
    }
}