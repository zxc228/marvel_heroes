package dev.ilya.marvelheroes.screens.heroes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dev.ilya.marvelheroes.R
import dev.ilya.marvelheroes.databinding.FragmentHeroesBinding
import dev.ilya.marvelheroes.features.hero.Hero
import dev.ilya.marvelheroes.features.hero.HeroesAdapter
import dev.ilya.marvelheroes.features.hero.HeroesFilter
import dev.ilya.marvelheroes.screens.details.HeroDetailsDialog
import dev.ilya.marvelheroes.screens.heroes.filter.FilterDialog
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class HeroesFragment : Fragment() {
    private var _binding: FragmentHeroesBinding? = null
    private val binding: FragmentHeroesBinding get() = _binding!!
    private lateinit var searchView: SearchView
    private val viewModel :HeroesViewModel by viewModels()
    private val adapter: HeroesAdapter by lazy {
        HeroesAdapter(::showDetails)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeroesBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState == null) {
            binding.refresh.isRefreshing = true
        }
        binding.grid.adapter = adapter
        binding.refresh.setOnRefreshListener(viewModel::refresh)
        setupSearch()
        setupFilter()
        viewModel.heroesData.observe(viewLifecycleOwner, ::renderList)
        viewModel.filterData.observe(viewLifecycleOwner, ::renderFilter)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.errorsChannel
                .receiveAsFlow()
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::showException)
        }
    }

    private fun setupSearch() {
        binding.toolbar.menu.findItem(R.id.search)
            .let { it.actionView as SearchView }
            .also { searchView = it }
            .apply {
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextChange(newText: String): Boolean {
                        viewModel.searchChange(newText)
                        return true
                    }

                    override fun onQueryTextSubmit(query: String): Boolean {
                        viewModel.search(query)
                        return true
                    }
                })
                setOnCloseListener {
                    viewModel.search(null)
                    false
                }
            }
    }

    private fun setupFilter(){
        binding.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.filter) {
                showFilter()
                return@setOnMenuItemClickListener true
            }
            false
        }
    }

    private fun renderList(heroes :List<Hero>){
        binding.refresh.isRefreshing = false
        adapter.setItems(heroes)
    }

    private fun renderFilter(filter :HeroesFilter){
        val newValue = filter.name == null
        if(searchView.isIconified != newValue){
            searchView.isIconified = newValue
        }
    }

    private fun showDetails(hero :Hero){
        HeroDetailsDialog(
            requireContext(),
            hero,
            viewModel.isFavorite(hero)
        ).show()
    }

    private fun showFilter() {
        FilterDialog(
            requireContext(),
            viewModel.filterData.value!!.intelligence,
            viewModel.range,
            viewModel::filter
        ).show()
    }

    private fun showException(exception: Exception){
        Toast.makeText(requireContext(), exception.message ?: "Неизвестная ошибка", Toast.LENGTH_LONG).show()
    }
}