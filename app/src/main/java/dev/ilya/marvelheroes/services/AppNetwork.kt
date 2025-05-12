package dev.ilya.marvelheroes.services

import dev.ilya.marvelheroes.features.hero.Hero
import dev.ilya.marvelheroes.features.hero.HeroesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object AppNetwork {
    private val api : HeroesApi = Retrofit.Builder()
        .baseUrl("https://akabab.github.io")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create<HeroesApi>()

    suspend fun fetch() :List<Hero> =
        api.fetch()
}