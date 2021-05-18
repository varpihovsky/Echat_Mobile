package com.example.echatmobile.di.modules

import android.content.Context
import androidx.room.Room
import com.example.echatmobile.api.EchatRestAPI
import com.example.echatmobile.di.scopes.ModelScope
import com.example.echatmobile.model.EchatAuthorizationSaver
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.model.db.EchatDatabase
import com.example.echatmobile.model.db.EchatRoomDatabase
import com.example.echatmobile.model.db.RoomDAO
import com.example.echatmobile.model.remote.EchatRemote
import com.example.echatmobile.system.ConnectionManager
import dagger.Module
import dagger.Provides
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class EchatModelModule {
    @ModelScope
    @Provides
    fun getEchatModel(
        echatRemote: EchatRemote,
        echatDatabase: EchatDatabase,
        echatAuthorizationSaver: EchatAuthorizationSaver
    ) = EchatModel(echatRemote, echatDatabase, echatAuthorizationSaver)

    @ModelScope
    @Provides
    fun getEchatRemote(echatRestAPI: EchatRestAPI, connectionManager: ConnectionManager) =
        EchatRemote(echatRestAPI, connectionManager)

    @ModelScope
    @Provides
    fun getApi(retrofit: Retrofit): EchatRestAPI = retrofit.create(EchatRestAPI::class.java)

    @ModelScope
    @Provides
    fun getRetrofit(factory: Converter.Factory): Retrofit =
        Retrofit.Builder()
            .baseUrl(ECHAT_REST_URL)
            .addConverterFactory(factory)
            .build()

    @ModelScope
    @Provides
    fun getFactory(): Converter.Factory = GsonConverterFactory.create()

    @ModelScope
    @Provides
    fun getConnectionManager() = ConnectionManager()

    @ModelScope
    @Provides
    fun getEchatDatabase(roomDAO: RoomDAO) =
        EchatDatabase(roomDAO)

    @ModelScope
    @Provides
    fun getRoomDAO(echatRoomDatabase: EchatRoomDatabase) = echatRoomDatabase.roomDAO

    @ModelScope
    @Provides
    fun getEchatRoomDatabase(context: Context) =
        Room.databaseBuilder(context, EchatRoomDatabase::class.java, "echat_database")
            .build()

    @ModelScope
    @Provides
    fun getAuthorizationSaver(context: Context) = EchatAuthorizationSaver(context)

    companion object {
        const val ECHAT_REST_URL = "http://192.168.0.38:8080/"
    }
}