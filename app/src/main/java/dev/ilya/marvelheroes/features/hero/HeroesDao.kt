package dev.ilya.marvelheroes.features.hero

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HeroesDao {

    @Query("SELECT * FROM heroes")
    fun listen() : Flow<List<Hero>>

    @Insert
    suspend fun create(hero :Hero)

    @Delete
    suspend fun delete(hero :Hero)
}