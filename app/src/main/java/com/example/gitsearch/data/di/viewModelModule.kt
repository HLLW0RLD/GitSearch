package com.example.gitsearch.data.di

import com.example.gitsearch.ui.screen.viewModel.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel {
        MainViewModel(get())
    }
}