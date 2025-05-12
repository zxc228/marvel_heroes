package dev.ilya.marvelheroes.screens

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import dev.ilya.marvelheroes.R
import dev.ilya.marvelheroes.databinding.ActivityMainBinding
import dev.ilya.marvelheroes.screens.favorites.FavoritesFragment
import dev.ilya.marvelheroes.screens.heroes.HeroesFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding :ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.navigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.heroes -> showPage(HeroesFragment(), it.title.toString())
                R.id.favorites -> showPage(FavoritesFragment(), it.title.toString())
                else -> return@setOnItemSelectedListener false
            }
            true
        }
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.main_content, HeroesFragment(), "heroes")
                .commitNow()
        }
    }

    private fun showPage(page :Fragment, tag :String? = null) {
        val manager = supportFragmentManager
        val fragment = manager.findFragmentByTag(tag) ?: page
        manager.beginTransaction()
            .replace(R.id.main_content, fragment, tag)
            .commit()
    }
}