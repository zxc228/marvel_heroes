package dev.ilya.marvelheroes.features.hero

import retrofit2.http.GET

interface HeroesApi {

    @GET("https://akabab.github.io/superhero-api/api/all.json")
    suspend fun fetch() :List<Hero>
}