package com.rolandoselvera.pokeapp.presentation.viewmodels.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rolandoselvera.pokeapp.data.model.Pokemon
import com.rolandoselvera.pokeapp.data.model.PokemonResponse
import com.rolandoselvera.pokeapp.domain.GetPokemonListUseCase
import com.rolandoselvera.pokeapp.domain.GetPokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val getPokemonUseCase: GetPokemonUseCase,
) : ViewModel() {

    private val _pokemonList = MutableLiveData<Result<PokemonResponse>>()
    val pokemonList: LiveData<Result<PokemonResponse>> get() = _pokemonList

    private val _pokemon = MutableLiveData<Result<Pokemon>>()
    val pokemon: LiveData<Result<Pokemon>> get() = _pokemon

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchAllPokemons(limit: Int, offset: Int) {
        viewModelScope.launch {
            try {
                val result = getPokemonListUseCase(limit, offset)
                _pokemonList.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }

    fun fetchSinglePokemon(id: Int) {
        viewModelScope.launch {
            try {
                val result = getPokemonUseCase(id)
                _pokemon.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
}

