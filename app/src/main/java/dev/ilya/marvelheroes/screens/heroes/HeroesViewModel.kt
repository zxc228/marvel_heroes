package dev.ilya.marvelheroes.screens.heroes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ilya.marvelheroes.App
import dev.ilya.marvelheroes.features.hero.Hero
import dev.ilya.marvelheroes.features.hero.HeroesFilter
import dev.ilya.marvelheroes.services.AppNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HeroesViewModel : ViewModel() {
    private val heroes: MutableList<Hero> = mutableListOf()
    val heroesData: MutableLiveData<List<Hero>> = MutableLiveData(heroes)
    val filterData: MutableLiveData<HeroesFilter> = MutableLiveData(HeroesFilter())
    private val favoritesState: StateFlow<List<Hero>> =
        App.heroesDb
            .listen()
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                heroes
            )
    private var searchJob: Job? = null
    var range: Pair<Int, Int> = 0 to 0
    val errorsChannel = Channel<Exception>()

    init {
        viewModelScope.launch {
            reloadList()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            heroes.clear()
            reloadList()
        }
    }

    fun searchChange(search: String) {
        searchJob?.cancel()
        if (search.isEmpty()) return
        searchJob = viewModelScope.launch {
            delay(500)
            search(search)
        }
    }

    fun search(text: String?) {
        searchJob?.cancel()
        filterData.value = filterData.value!!.copy(name = text)
        filterList()
    }

    fun filter(intelligence: Int?) {
        filterData.value = filterData.value!!.copy(intelligence = intelligence)
        filterList()
    }

    fun isFavorite(hero: Hero): Boolean =
        favoritesState.value.contains(hero)

    private fun filterList() {
        heroesData.value = heroes
            .filter { hero ->
                filterData.value!!.intelligence
                    ?.let { hero.powerstats.intelligence >= it }
                    ?: true
            }
            .filter { hero ->
                filterData.value!!.name
                    ?.let { hero.name.contains(it, ignoreCase = true) }
                    ?: true
            }
    }

    private suspend fun reloadList() {
        try {
            heroes.addAll(AppNetwork.fetch())
        } catch (ex: Exception) {
            errorsChannel.send(ex)
        }
        heroesData.value = heroes
        filterData.value = HeroesFilter()
        withContext(Dispatchers.Default) {
            range = heroes.map { it.powerstats.intelligence }.range
        }
    }

    private val Collection<Int>.range: Pair<Int, Int>
        get() = if (isEmpty()) 0 to 0
        else min() to max()
}