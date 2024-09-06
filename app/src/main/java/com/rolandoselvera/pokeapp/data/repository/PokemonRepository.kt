package com.rolandoselvera.pokeapp.data.repository

import com.rolandoselvera.pokeapp.data.model.Pokemon
import com.rolandoselvera.pokeapp.data.model.PokemonResponse

interface PokemonRepository {
    suspend fun getAllPokemons(limit: Int?, offset: Int?): Result<PokemonResponse>
    suspend fun getPokemon(id: Int): Result<Pokemon>
}
