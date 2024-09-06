package com.rolandoselvera.pokeapp.data.repository

import com.rolandoselvera.pokeapp.data.model.Pokemon
import com.rolandoselvera.pokeapp.data.model.PokemonResponse
import com.rolandoselvera.pokeapp.data.remote.ApiService
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : PokemonRepository {

    override suspend fun getAllPokemons(limit: Int?, offset: Int?): Result<PokemonResponse> {
        return try {
            val response = apiService.getAllPokemons(limit, offset)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Error: Response body is null"))
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPokemon(id: Int): Result<Pokemon> {
        return try {
            val response = apiService.getPokemon(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Error: Response body is null"))
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
