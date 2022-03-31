package com.sutrix.uatest.di

import com.sutrix.uatest.model.business.CatalogBM
import com.sutrix.uatest.model.business.CatalogBMImpl
import com.sutrix.uatest.model.network.UAClient
import com.sutrix.uatest.model.network.UAClientImpl
import com.sutrix.uatest.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {
    single<UAClient> { UAClientImpl() }
    single<CatalogBM> { CatalogBMImpl(get()) }
    viewModel { MainViewModel(get()) }
}