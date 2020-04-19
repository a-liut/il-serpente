package it.aliut.ilserpente.di

import it.aliut.ilserpente.service.GoogleUserService
import it.aliut.ilserpente.service.UserService
import it.aliut.ilserpente.ui.history.HistoryViewModel
import it.aliut.ilserpente.ui.modeselection.ModeSelectionViewModel
import it.aliut.ilserpente.ui.settings.SettingsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<UserService> {
        GoogleUserService(
            get()
        )
    }

    viewModel { ModeSelectionViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { HistoryViewModel() }
}
