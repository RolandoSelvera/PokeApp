package com.rolandoselvera.pokeapp.domain

import com.rolandoselvera.pokeapp.data.model.PokemonResponse
import com.rolandoselvera.pokeapp.data.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(limit: Int?, offset: Int?): Result<PokemonResponse> {
        return try {
            val result = repository.getAllPokemons(limit, offset)
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
