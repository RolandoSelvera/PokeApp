package com.rolandoselvera.pokeapp.data.remote

import com.rolandoselvera.pokeapp.data.model.Pokemon
import com.rolandoselvera.pokeapp.data.model.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("pokemon/")
    suspend fun getAllPokemons(
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?
    ): Response<PokemonResponse>

    @GET("pokemon/{id}/")
    suspend fun getPokemon(
        @Path("id") id: Int
    ): Response<Pokemon>
}
