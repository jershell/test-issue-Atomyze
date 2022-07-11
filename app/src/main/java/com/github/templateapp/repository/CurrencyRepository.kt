@file:OptIn(ExperimentalTime::class)

package com.github.templateapp.repository

import android.util.Log
import com.github.templateapp.data.IRemote
import com.github.templateapp.serialization.json
import com.github.templateapp.serialization.models.Currencies
import com.github.templateapp.serialization.models.Currency
import io.ktor.client.request.*
import io.ktor.client.statement.*
import java.util.*
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime


class CurrencyRepository(private val ds: IRemote) {
    private val all = mutableMapOf<String, Currency>()
    private val _loadedAt: Date = Date(0)

    val loadedAt: Date
        get() = _loadedAt

    suspend fun getAll(): Result<Map<String, Currency>> {
        return if(_loadedAt.time < Date().time - 24.hours.inWholeMilliseconds) {
            populate()
        } else {
            Result.success(all)
        }
    }

    suspend fun populate(): Result<Map<String, Currency>> {
        return fetch().onSuccess {
            all.clear()
            all.putAll(it)
            _loadedAt.time = Date().time
        }
    }

    private suspend fun fetch(): Result<Map<String, Currency>> {
        return try {
            Log.d("NET", "FETCH CURRENCY RATE")
            val response = ds.http.get<HttpResponse>("https://www.cbr-xml-daily.ru/daily_json.js")
            val payload = response
                .readText()
                .let {
                    json.decodeFromString(Currencies.serializer(), it)
                }

            Result.success(payload.all)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}