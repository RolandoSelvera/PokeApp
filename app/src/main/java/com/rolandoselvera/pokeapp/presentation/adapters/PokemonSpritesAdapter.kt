package com.rolandoselvera.pokeapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rolandoselvera.pokeapp.R
import com.rolandoselvera.pokeapp.data.model.Sprites
import com.rolandoselvera.pokeapp.databinding.ItemSpriteBinding

class PokemonSpritesAdapter(sprites: Sprites) :
    RecyclerView.Adapter<PokemonSpritesAdapter.SpriteViewHolder>() {

    private val spriteList = listOf(
        sprites.frontDefault,
        sprites.backDefault,
        sprites.frontShiny,
        sprites.backShiny
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpriteViewHolder {
        val binding = ItemSpriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SpriteViewHolder, position: Int) {
        val spriteUrl = spriteList[position]
        holder.bind(spriteUrl)
    }

    override fun getItemCount(): Int = spriteList.size

    inner class SpriteViewHolder(private val binding: ItemSpriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(spriteUrl: String) {
            binding.imageSprite.load(spriteUrl) {
                crossfade(true)
                placeholder(R.drawable.pokemon_icon)
            }
        }
    }
}
