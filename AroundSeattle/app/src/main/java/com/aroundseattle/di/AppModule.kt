package com.aroundseattle.di

import android.app.Application
import android.arch.persistence.room.Room
import com.aroundseattle.R
import com.aroundseattle.api.FoursquareDataProvider
import com.aroundseattle.api.FoursquareService
import com.aroundseattle.data.Configuration
import com.aroundseattle.db.Database
import com.aroundseattle.db.FavoritesDao
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideFoursquareService(): FoursquareService {
        val logging = HttpLoggingInterceptor()
        logging.level =  HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        return Retrofit.Builder()
                .baseUrl("https://api.foursquare.com/v2/")
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FoursquareService::class.java)
    }

    @Singleton
    @Provides
    fun provideConfig(app: Application): Configuration {
        return Configuration(app.resources.getString(R.string.fs_clientId),
                app.resources.getString(R.string.fs_clientSecret))
    }

    @Singleton
    @Provides
    fun provideFoursquareDataProvider(config: Configuration, service: FoursquareService)
            : FoursquareDataProvider {
        return FoursquareDataProvider(config, service)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application) : Database {
        return Room.databaseBuilder(app, Database::class.java, "favorites.db")
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun provideFavoritesDao(db: Database) :  FavoritesDao {
        return db.favoritesDao()
    }
}
