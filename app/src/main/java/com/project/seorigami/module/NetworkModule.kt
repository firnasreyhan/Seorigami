package com.project.seorigami.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideNetworkProvider(
        @ApplicationContext context: Context
    ): NetworkProvider = NetworkProvider(
        context = context,
        url = "https://mdthosting.com/penjahitapp/public/api/"
    )

    @Singleton
    @Provides
    fun provideRetrofit(networkProvider: NetworkProvider): Retrofit = networkProvider.setRetrofit()

    @Singleton
    @Provides
    fun provideNetworkInterface(retrofit: Retrofit): NetworkInterface = retrofit.create(NetworkInterface::class.java)

    @Singleton
    @Provides
    fun provideRepository(networkInterface: NetworkInterface) = Repository(networkInterface)
}