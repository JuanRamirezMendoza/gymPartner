package com.peakDevCol.gympartner.di

import com.peakDevCol.gympartner.data.repository.LoginRepository
import com.peakDevCol.gympartner.data.repository.LoginRepositoryImpl
import com.peakDevCol.gympartner.data.repository.SignInRepository
import com.peakDevCol.gympartner.data.repository.SignInRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @Binds
    fun bindsMainRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository

    @Binds
    fun bindsDetailRepository(signInRepositoryImpl: SignInRepositoryImpl): SignInRepository

}