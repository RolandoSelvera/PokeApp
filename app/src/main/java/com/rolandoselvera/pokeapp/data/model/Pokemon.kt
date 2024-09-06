package com.rolandoselvera.pokeapp.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Pokemon(
    @SerializedName("sprites")
    val sprites: Sprites,
    @SerializedName("stats")
    val stats: List<Stats>,
    @SerializedName("height")
    val height: Int,
    @SerializedName("weight")
    val weight: Int
) : Serializable

data class Sprites(
    @SerializedName("back_default")
    val backDefault: String,
    @SerializedName("back_shiny")
    val backShiny: String,
    @SerializedName("front_default")
    val frontDefault: String,
    @SerializedName("front_shiny")
    val frontShiny: String
) : Serializable

data class Stats(
    @SerializedName("base_stat")
    val baseStat: Int,
    @SerializedName("effort")
    val effort: Int,
    @SerializedName("stat")
    val stat: Stat
) : Serializable

data class Stat(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
) : Serializable
