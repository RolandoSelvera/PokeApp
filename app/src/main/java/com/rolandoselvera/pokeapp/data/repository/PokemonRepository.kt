package com.rolandoselvera.pokeapp.data.repository

import com.rolandoselvera.pokeapp.data.model.Pokemon
import com.rolandoselvera.pokeapp.data.model.PokemonResponse

interface PokemonRepository {
    suspend fun getAllPokemons(limit: Int?, offset: Int?): Result<PokemonResponse>
    suspend fun getPokemonById(id: Int): Result<Pokemon>
    suspend fun getPokemonByName(name: String): Result<Pokemon>
}
