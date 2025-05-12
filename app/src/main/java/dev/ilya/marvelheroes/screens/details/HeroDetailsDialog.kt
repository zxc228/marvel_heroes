package dev.ilya.marvelheroes.screens.details

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import dev.ilya.marvelheroes.App
import dev.ilya.marvelheroes.R
import dev.ilya.marvelheroes.databinding.DialogHeroBinding
import dev.ilya.marvelheroes.features.hero.Hero
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class HeroDetailsDialog(
    context: Context,
    hero: Hero,
    var isFavorite: Boolean,
) : AlertDialog(context) {

    private val scope = CoroutineScope(Dispatchers.Main)
    private val binding = DialogHeroBinding.inflate(LayoutInflater.from(context))

    init {
        binding.title.text = hero.name
        setView(binding.root)
        Glide.with(context)
            .load(hero.images.lg)
            .placeholder(R.drawable.no_portrait)
            .into(binding.image)
        binding.description.text = hero.biography.toText()
        indicateFavorite(isFavorite)
        binding.favorite.setOnClickListener {
            scope.launch {
                if (isFavorite) App.heroesDb.delete(hero)
                else App.heroesDb.create(hero)
            }
        }
        binding.share.setOnClickListener {

        }
        scope.launch {
            App.heroesDb
                .listen()
                .collect {
                    isFavorite = it.contains(hero)
                    indicateFavorite(isFavorite)
                }
        }
        setButton(BUTTON_POSITIVE, "close") { _, _ -> }
        setOnCancelListener { scope.cancel() }
    }

    private fun indicateFavorite(isFavorite :Boolean){
        binding.favorite.setImageResource(
            if (isFavorite) R.drawable.ic_heart_fill
            else R.drawable.ic_heart_empty
        )
    }
}