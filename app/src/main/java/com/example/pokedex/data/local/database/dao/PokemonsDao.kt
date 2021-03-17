package com.example.pokedex.data.local.database.dao
import androidx.room.*
import com.example.pokedex.data.local.database.DataBaseConstants
import com.example.pokedex.data.local.database.entities.PokemonEntity
import com.example.pokedex.data.models.Pokemon

@Dao
interface PokemonsDao {
    @Query("SELECT * from ${DataBaseConstants.TABLE_NAME_POKEMONS} where  ${DataBaseConstants.COLUMN_ID} > 0 order by ${DataBaseConstants.COLUMN_ID} asc")
    suspend fun getAll(): List<PokemonEntity>
    @Query("SELECT * from ${DataBaseConstants.TABLE_NAME_POKEMONS} where  ${DataBaseConstants.COLUMN_ID} = :id")
    suspend fun getById(id: Long): PokemonEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemons: List<PokemonEntity>)
}