package com.alpha.vision.pro.gallery.settings.di

import com.alpha.vision.pro.gallery.settings.datastore.DataStorePrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

// DataStorePrefs is already @Singleton + @Inject constructor, so Hilt auto-provides it.
// This module is a placeholder for any future DataStore-related bindings.
@Module
@InstallIn(SingletonComponent::class)
object SettingsModule
