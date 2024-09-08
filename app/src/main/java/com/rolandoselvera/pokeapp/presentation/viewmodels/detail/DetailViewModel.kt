package com.rolandoselvera.pokeapp.presentation.viewmodels.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rolandoselvera.pokeapp.data.model.Pokemon
import com.rolandoselvera.pokeapp.domain.GetPokemonByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getPokemonByIdUseCase: GetPokemonByIdUseCase,
) : ViewModel() {

    private val _pokemon = MutableLiveData<Result<Pokemon>>()
    val pokemon: LiveData<Result<Pokemon>> get() = _pokemon

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchSinglePokemon(id: Int) {
        viewModelScope.launch {
            try {
                val result = getPokemonByIdUseCase(id)
                _pokemon.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
}