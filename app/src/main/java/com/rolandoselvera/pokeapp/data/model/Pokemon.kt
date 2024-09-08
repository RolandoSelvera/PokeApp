package com.rolandoselvera.pokeapp.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Pokemon(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("abilities")
    val abilities: List<Abilities>,
    @SerializedName("sprites")
    val sprites: Sprites,
    @SerializedName("stats")
    val stats: List<Stats>,
    @SerializedName("types")
    val types: List<Types>,
    @SerializedName("height")
    val height: Int,
    @SerializedName("weight")
    val weight: Int
) : Serializable

data class Abilities(
    @SerializedName("ability")
    val ability: Ability
)

data class Ability(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class Sprites(
    @SerializedName("back_default")
    val backDefault: String,
    @SerializedName("back_shiny")
    val backShiny: String,
    @SerializedName("front_default")
    val frontDefault: String,
    @SerializedName("front_shiny")
    val frontShiny: String,
    @SerializedName("other")
    val other: Other
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

data class Types(
    @SerializedName("slot")
    val slot: Int,
    @SerializedName("type")
    val type: Type
) : Serializable

data class Type(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
) : Serializable

data class Other(
    @SerializedName("dream_world")
    val dreamWorld: Sprites,
    @SerializedName("home")
    val home: Sprites,
    @SerializedName("showdown")
    val showdown: Sprites,
    @SerializedName("official-artwork")
    val officialArtwork: Sprites
) : Serializable
