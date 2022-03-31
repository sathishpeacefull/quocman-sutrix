package com.sutrix.uatest.model.business

import com.sutrix.uatest.model.models.Burger
import com.sutrix.uatest.model.network.UAClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

interface CatalogBM {
    val burgers: Flow<List<Burger>?>

    suspend fun updateBurgers()
}

class CatalogBMImpl(private val uaClient: UAClient) : CatalogBM {

    override val burgers: MutableStateFlow<List<Burger>?> = MutableStateFlow(null)
    var burgersList: List<Burger>? = null
    override suspend fun updateBurgers() {

        if (burgersList == null) {
            withContext(Dispatchers.IO) {
                burgersList = uaClient.getBurgers().map {
                    Burger(
                        ref = it.ref,
                        title = it.title,
                        description = it.description,
                        thumbnail = it.thumbnail,
                        price = it.price.toDouble() / 100
                    )
                }
            }

        }
        burgers.value = burgersList
    }
}
