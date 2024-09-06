package com.rolandoselvera.pokeapp.presentation.views.fragments.home

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rolandoselvera.pokeapp.databinding.FragmentHomeBinding
import com.rolandoselvera.pokeapp.presentation.viewmodels.home.HomeViewModel
import com.rolandoselvera.pokeapp.presentation.views.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun initializeViews() {
        homeViewModel.fetchAllPokemons(limit = 20, offset = 0)
        homeViewModel.fetchSinglePokemon(25)
    }

    override fun initializeViewModel() {
        homeViewModel.pokemonList.observe(viewLifecycleOwner) { pokemonList ->
            Log.d("POKEMON_LIST", pokemonList.toString())
        }

        homeViewModel.pokemon.observe(viewLifecycleOwner) { pokemon ->
            Log.d("POKEMON_INFO", pokemon.toString())
        }

        homeViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Log.e("POKEMON_ERROR", errorMessage)
        }
    }
}