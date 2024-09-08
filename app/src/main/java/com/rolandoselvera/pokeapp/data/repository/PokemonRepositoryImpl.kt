package com.rolandoselvera.pokeapp.data.repository

import com.rolandoselvera.pokeapp.data.model.Pokemon
import com.rolandoselvera.pokeapp.data.model.PokemonResponse
import com.rolandoselvera.pokeapp.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : PokemonRepository {

    override suspend fun getAllPokemons(limit: Int?, offset: Int?): Result<PokemonResponse> {
        return try {
            val response = apiService.getAllPokemons(limit, offset)
            if (response.isSuccessful) {
                response.body()?.let { pokemonResponse ->

                    val pokemonList = pokemonResponse.results.map { pokemon ->
                        withContext(Dispatchers.IO) {
                            val pokemonId = pokemon.url
                                .split("/")
                                .filter {
                                    it.isNotEmpty()
                                }
                                .last()
                                .toInt()
                            val pokemonDetailResponse = apiService.getPokemonById(pokemonId)
                            if (pokemonDetailResponse.isSuccessful) {
                                pokemon.copy(
                                    id = pokemonId,
                                    imageUrl = pokemonDetailResponse.body()?.sprites?.other?.officialArtwork?.frontDefault
                                )
                            } else {
                                pokemon
                            }
                        }
                    }

                    Result.success(pokemonResponse.copy(results = pokemonList))
                } ?: Result.failure(Exception("Error: Response body is null"))
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPokemonById(id: Int): Result<Pokemon> {
        return try {
            val response = apiService.getPokemonById(id)
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

    override suspend fun getPokemonByName(name: String): Result<Pokemon> {
        return try {
            val response = apiService.getPokemonByName(name)
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
