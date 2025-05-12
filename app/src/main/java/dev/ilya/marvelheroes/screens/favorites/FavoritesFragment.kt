package dev.ilya.marvelheroes.screens.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.ilya.marvelheroes.databinding.FragmentFavoritesBinding
import dev.ilya.marvelheroes.features.hero.Hero
import dev.ilya.marvelheroes.features.hero.HeroesAdapter
import dev.ilya.marvelheroes.screens.details.HeroDetailsDialog

class FavoritesFragment() :Fragment() {
    private var _binding :FragmentFavoritesBinding? = null
    private val binding :FragmentFavoritesBinding get() = _binding!!
    private val viewModel :FavoritesViewModel by viewModels()
    private val adapter :HeroesAdapter by lazy {
        HeroesAdapter(::showDetails)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.empty.visibility = View.GONE
        binding.grid.visibility = View.GONE
        binding.grid.adapter = adapter
        viewModel.favorites.observe(viewLifecycleOwner){
            if(it == null){
                showEmpty()
            } else {
                showGrid(it)
            }
        }
    }

    private fun showEmpty(){
        binding.grid.visibility = View.GONE
        binding.empty.visibility = View.VISIBLE
    }

    private fun showGrid(heroes :List<Hero>){
        binding.grid.visibility = View.VISIBLE
        binding.empty.visibility = View.GONE
        adapter.setItems(heroes)
    }

    private fun showDetails(hero :Hero){
        HeroDetailsDialog(
            requireContext(),
            hero,
            true
        ).show()
    }
}