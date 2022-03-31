package com.sutrix.uatest.model.network

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

interface UAClient {
    suspend fun getBurgers(): List<UABurger>
}

@Serializable
data class UABurger(
    val ref: String,
    val title: String,
    val description: String,
    val thumbnail: String,
    val price: Int
)

val baseJson by lazy {
    Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
}

val baseSerializer by lazy {
    KotlinxSerializer(baseJson)
}


class UAClientImpl : UAClient {


    private val client = HttpClient {
        install(JsonFeature) {
            serializer = baseSerializer
        }

        defaultRequest {
            url {
                host = "uad.io"
            }
        }
    }


    override suspend fun getBurgers(): List<UABurger> {

        return client.get<List<UABurger>> {
            url {
                encodedPath = "bigburger"
            }
        }
    }
}