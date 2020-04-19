package it.aliut.ilserpente.di

import it.aliut.ilserpente.ui.history.HistoryViewModel
import it.aliut.ilserpente.ui.modeselection.ModeSelectionViewModel
import it.aliut.ilserpente.ui.settings.SettingsViewModel
import it.aliut.ilserpente.user.GoogleUserService
import it.aliut.ilserpente.user.UserService
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<UserService> { GoogleUserService(get()) }

    viewModel { ModeSelectionViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { HistoryViewModel() }
}
