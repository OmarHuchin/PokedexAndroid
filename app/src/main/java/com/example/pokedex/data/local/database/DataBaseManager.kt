package com.example.pokedex.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokedex.data.local.database.dao.PokemonsDao
import com.example.pokedex.data.models.Pokemon

@Database(entities = [
    Pokemon::class
], version = DataBaseConstants.DATABASE_VERSION, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DataBaseManager : RoomDatabase(){

    abstract fun pokemonsDao(): PokemonsDao

    companion object {

        private var INSTANCE: DataBaseManager? = null

        fun getInstance(context: Context): DataBaseManager {
            if (INSTANCE == null) {
                synchronized(DataBaseManager::class) {
                    INSTANCE = androidx.room.Room.databaseBuilder(context.applicationContext,
                        DataBaseManager::class.java, DataBaseConstants.DATABASE_NAME)
                        .build()
                }
            }
            return INSTANCE!!
        }

    }
    fun destroyInstance() {
        INSTANCE = null
    }


    suspend fun readPokemons(): List<Pokemon>{
        return pokemonsDao().getAll()
    }
}