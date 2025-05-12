package dev.ilya.marvelheroes

import android.app.Application
import androidx.room.Room
import dev.ilya.marvelheroes.features.hero.HeroesDao
import dev.ilya.marvelheroes.services.AppDatabase

class App :Application() {

    companion object {
        lateinit var heroesDb :HeroesDao
            private set
    }

    override fun onCreate() {
        super.onCreate()
        heroesDb = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "database.db"
        )
            .build()
            .heroes()
    }
}