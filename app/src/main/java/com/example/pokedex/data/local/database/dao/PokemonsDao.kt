package com.example.pokedex.data.local.database.dao
import androidx.room.*
import com.example.pokedex.data.local.database.DataBaseConstants
import com.example.pokedex.data.models.Pokemon

@Dao
interface PokemonsDao {
    @Query("SELECT * from ${DataBaseConstants.TABLE_NAME_POKEMONS}")
    suspend fun getAll(): List<Pokemon>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemons: List<Pokemon>)
}