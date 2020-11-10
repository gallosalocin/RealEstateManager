package com.openclassrooms.realestatemanager.di

import android.content.Context
import androidx.room.Room
import com.openclassrooms.realestatemanager.db.RealEstateDatabase
import com.openclassrooms.realestatemanager.other.Constants.REAL_ESTATE_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRealEstateDatabase(@ApplicationContext context: Context) =
            Room.databaseBuilder(context, RealEstateDatabase::class.java, REAL_ESTATE_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideAgentDao(database: RealEstateDatabase) = database.getAgentDao()

    @Singleton
    @Provides
    fun providePropertyDao(database: RealEstateDatabase) = database.getPropertyDao()
}