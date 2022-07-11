package com.github.templateapp.data

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*

object DataSource: IRemote {
    private fun initHttpClient() = HttpClient() {
        defaultRequest {

        }
    }

    override val http = initHttpClient()
}
