package com.mirz.request.di

import com.mirz.navigation.base.BaseNavGraph
import com.mirz.request.navigation.RequestNavGraphImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class RequestNavModule {

    @Binds
    @IntoSet
    abstract fun bindRequestNavGraph(navGraph: RequestNavGraphImpl): BaseNavGraph
}