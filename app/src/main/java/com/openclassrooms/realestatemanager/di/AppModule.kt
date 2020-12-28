package com.openclassrooms.realestatemanager.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.db.RealEstateDatabase
import com.openclassrooms.realestatemanager.other.Constants.REAL_ESTATE_DATABASE_NAME
import com.openclassrooms.realestatemanager.repositories.CurrentPropertyIdRepository
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
    fun provideCurrentPropertyRepository() = CurrentPropertyIdRepository()

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

    @Singleton
    @Provides
    fun providePropertyPhotoDao(database: RealEstateDatabase) = database.getPropertyPhotoDao()
}