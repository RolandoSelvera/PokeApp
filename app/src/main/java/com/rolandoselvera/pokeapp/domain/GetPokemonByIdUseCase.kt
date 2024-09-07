package com.rolandoselvera.pokeapp.domain

import com.rolandoselvera.pokeapp.data.model.Pokemon
import com.rolandoselvera.pokeapp.data.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonByIdUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(id: Int): Result<Pokemon> {
        return try {
            val result = repository.getPokemonById(id)
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
