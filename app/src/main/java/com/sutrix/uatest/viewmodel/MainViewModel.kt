package com.sutrix.uatest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sutrix.uatest.model.business.CatalogBM
import com.sutrix.uatest.model.models.Burger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class MainViewModel(catalogBM: CatalogBM): ViewModel() {

    val burgers: Flow<List<Burger>?> = catalogBM.burgers

    init {
        viewModelScope.launch {
            catalogBM.updateBurgers()
        }
    }
}