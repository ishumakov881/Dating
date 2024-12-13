package com.lds.quickdeal.android.di

import com.lds.quickdeal.repository.SettingsRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ExportRepositoryEntryPoint {
    fun getExportRepository(): SettingsRepository
}