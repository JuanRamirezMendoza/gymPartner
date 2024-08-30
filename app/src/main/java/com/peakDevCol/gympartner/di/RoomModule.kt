package com.peakDevCol.gympartner.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.peakDevCol.gympartner.data.LocalDataSource
import com.peakDevCol.gympartner.data.local.RoomDataSource
import com.peakDevCol.gympartner.data.local.database.GymPartnerDataBase
import com.peakDevCol.gympartner.data.local.database.TypeResponseConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val GYM_PARTNER_DATABASE_NAME = "gym_partner_database"


    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideAppDatabase(
        application: Application,
        typeResponseConverter: TypeResponseConverter,
    ): GymPartnerDataBase {
        return Room
            .databaseBuilder(application, GymPartnerDataBase::class.java, GYM_PARTNER_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .addTypeConverter(typeResponseConverter)
            .build()
    }


    @Provides
    @Singleton
    fun providePokemonDao(appDatabase: GymPartnerDataBase): LocalDataSource =
        RoomDataSource(database = appDatabase)


}