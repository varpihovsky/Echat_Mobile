package com.example.echatmobile.di.modules

import com.example.echatmobile.api.EchatRestAPI
import com.example.echatmobile.model.EchatModel
import dagger.Module
import dagger.Provides
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class EchatModelModule {
    @Provides
    fun getEchatModel(echatRestAPI: EchatRestAPI) = EchatModel(echatRestAPI)

    @Provides
    fun getApi(retrofit: Retrofit) = retrofit.create(EchatRestAPI::class.java)

    @Provides
    fun getRetrofit(factory: Converter.Factory) =
        Retrofit.Builder()
            .baseUrl(ECHAT_REST_URL)
            .addConverterFactory(factory)
            .build()

    @Provides
    fun getFactory(): Converter.Factory = GsonConverterFactory.create()

    companion object{
        const val ECHAT_REST_URL = "http://192.168.0.38:8080/"
    }
}