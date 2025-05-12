package dev.ilya.marvelheroes.screens.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dev.ilya.marvelheroes.App
import dev.ilya.marvelheroes.features.hero.Hero
import kotlinx.coroutines.flow.map

class FavoritesViewModel :ViewModel() {
    val favorites : LiveData<List<Hero>?> = App.heroesDb
        .listen()
        .map { it.takeIf { it.isNotEmpty() } }
        .asLiveData()
}