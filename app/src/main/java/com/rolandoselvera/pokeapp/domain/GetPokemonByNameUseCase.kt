package com.rolandoselvera.pokeapp.domain

import com.rolandoselvera.pokeapp.data.model.Pokemon
import com.rolandoselvera.pokeapp.data.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonByNameUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(name: String): Result<Pokemon> {
        return try {
            val result = repository.getPokemonByName(name)
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}