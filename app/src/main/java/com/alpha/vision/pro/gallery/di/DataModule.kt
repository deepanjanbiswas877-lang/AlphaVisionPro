package com.alpha.vision.pro.gallery.di

import android.content.Context
import androidx.room.Room
import com.alpha.vision.pro.gallery.data.db.MediaDatabase
import com.alpha.vision.pro.gallery.data.db.dao.MediaDao
import com.alpha.vision.pro.gallery.data.native.NativeImageProcessor
import com.alpha.vision.pro.gallery.data.repository.MediaRepositoryImpl
import com.alpha.vision.pro.gallery.data.repository.VaultRepositoryImpl
import com.alpha.vision.pro.gallery.domain.repository.MediaRepository
import com.alpha.vision.pro.gallery.domain.repository.VaultRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds @Singleton
    abstract fun bindMediaRepository(impl: MediaRepositoryImpl): MediaRepository

    @Binds @Singleton
    abstract fun bindVaultRepository(impl: VaultRepositoryImpl): VaultRepository

    companion object {
        @Provides @Singleton
        fun provideMediaDatabase(@ApplicationContext context: Context): MediaDatabase =
            Room.databaseBuilder(context, MediaDatabase::class.java, "alpha_vision_media.db")
                .fallbackToDestructiveMigration(true)
                .build()

        @Provides @Singleton
        fun provideMediaDao(db: MediaDatabase): MediaDao = db.mediaDao()

        @Provides @Singleton
        fun provideNativeImageProcessor(): NativeImageProcessor = NativeImageProcessor()
    }
}
