package com.rolandoselvera.pokeapp.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PokemonResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<PokemonResult>
) : Serializable

data class PokemonResult(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
) : Serializable