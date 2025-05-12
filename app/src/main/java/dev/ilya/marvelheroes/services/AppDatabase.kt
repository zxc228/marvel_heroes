package dev.ilya.marvelheroes.services

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.ilya.marvelheroes.features.hero.Hero
import dev.ilya.marvelheroes.features.hero.HeroesDao

@Database(entities = [Hero::class], version = 1)
abstract class AppDatabase :RoomDatabase(){
    abstract fun heroes() :HeroesDao
}