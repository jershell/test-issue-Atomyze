package com.github.templateapp.data

import io.ktor.client.*

interface IRemote {
    val http: HttpClient
}