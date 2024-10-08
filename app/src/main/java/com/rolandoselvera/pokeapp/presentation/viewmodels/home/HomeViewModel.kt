package com.rolandoselvera.pokeapp.presentation.viewmodels.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rolandoselvera.pokeapp.common.Constants.Companion.POKEMON_TO_SHOW
import com.rolandoselvera.pokeapp.data.model.Pokemon
import com.rolandoselvera.pokeapp.data.model.PokemonResponse
import com.rolandoselvera.pokeapp.domain.GetPokemonByNameUseCase
import com.rolandoselvera.pokeapp.domain.GetPokemonListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val getPokemonByNameUseCase: GetPokemonByNameUseCase,
) : ViewModel() {

    private val _pokemonList = MutableLiveData<Result<PokemonResponse>?>()
    val pokemonList: LiveData<Result<PokemonResponse>?> get() = _pokemonList

    private val _pokemon = MutableLiveData<Result<Pokemon>>()
    val pokemon: LiveData<Result<Pokemon>> get() = _pokemon

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private var offset = 0
    private val limit = POKEMON_TO_SHOW

    var isSearching: Boolean = false
    var isLoading = false

    fun fetchAllPokemons() {
        if (isLoading) return

        viewModelScope.launch {
            isLoading = true
            try {
                val result = getPokemonListUseCase(limit, offset)
                _pokemonList.postValue(result)
                offset += limit
            } catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                isLoading = false
            }
        }
    }

    fun resetPagination() {
        offset = 0
        _pokemonList.value = null
    }

    fun fetchSinglePokemon(name: String) {
        viewModelScope.launch {
            try {
                val result = getPokemonByNameUseCase(name)
                _pokemon.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
}