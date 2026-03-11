package com.alpha.vision.pro.gallery.data.di

import com.alpha.vision.pro.gallery.data.nativelib.NativeEditServiceImpl
import com.alpha.vision.pro.gallery.domain.service.NativeEditService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NativeModule {
    @Binds @Singleton
    abstract fun bindNativeEditService(impl: NativeEditServiceImpl): NativeEditService
}
