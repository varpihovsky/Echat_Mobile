package com.example.echatmobile.di.modules

import android.util.Log
import com.example.echatmobile.api.EchatRestAPI
import com.example.echatmobile.di.scopes.ApplicationScope
import com.example.echatmobile.di.scopes.ModelScope
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.system.EchatApplication.Companion.LOG_TAG
import dagger.Module
import dagger.Provides
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class EchatModelModule {
    @ModelScope
    @Provides
    fun getEchatModel(echatRestAPI: EchatRestAPI) =
        EchatModel(echatRestAPI).apply { Log.d(LOG_TAG, counter++.toString()) }

    @ModelScope
    @Provides
    fun getApi(retrofit: Retrofit) = retrofit.create(EchatRestAPI::class.java)

    @ModelScope
    @Provides
    fun getRetrofit(factory: Converter.Factory) =
        Retrofit.Builder()
            .baseUrl(ECHAT_REST_URL)
            .addConverterFactory(factory)
            .build()

    @ModelScope
    @Provides
    fun getFactory(): Converter.Factory = GsonConverterFactory.create()

    companion object {
        const val ECHAT_REST_URL = "http://192.168.0.38:8080/"
        private var counter = 0
    }
}