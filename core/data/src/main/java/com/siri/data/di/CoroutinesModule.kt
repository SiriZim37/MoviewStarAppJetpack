package com.siri.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier


/*
Dieses Modul stellt verschiedene Coroutine Dispatcher bereit:
@IoDispatcher: Für IO-intensive Operationen (z. B. Datenbank, Netzwerk)
    -  สำหรับงาน IO (โหลดข้อมูล, เขียนฐานข้อมูล)
@DefaultDispatcher: Für Standard-Hintergrundarbeiten
    - @DefaultDispatcher: สำหรับงานเบื้องหลังทั่วไป
@ApplicationScope: Für eine globale CoroutineScope
    - @ApplicationScope: Scope ที่มีอายุการใช้งานเท่ากับแอป
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IoDispatcher

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

@Module
@InstallIn(SingletonComponent::class)
object CoroutinesModule {

    @Provides
    @IoDispatcher
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @DefaultDispatcher
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @ApplicationScope
    fun providesCoroutineScope(
        @DefaultDispatcher dispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)
}