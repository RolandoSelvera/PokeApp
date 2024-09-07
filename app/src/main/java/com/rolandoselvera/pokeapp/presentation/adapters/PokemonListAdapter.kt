package com.rolandoselvera.pokeapp.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rolandoselvera.pokeapp.R
import com.rolandoselvera.pokeapp.data.model.PokemonResult
import com.rolandoselvera.pokeapp.databinding.ItemListPokemonBinding

class PokemonListAdapter(context: Context, private val onItemClicked: (PokemonResult) -> Unit) :
    ListAdapter<PokemonResult, PokemonListAdapter.SearchViewHolder>(DiffCallback) {

    private var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemListPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val current = getItem(position)

        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }

        holder.bind(current)
    }

    inner class SearchViewHolder(private var binding: ItemListPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: PokemonResult) {
            binding.apply {
                txtName.text = result.name.replaceFirstChar {
                    it.uppercase()
                }

                txtId.text = mContext.getString(R.string.pokemon_id, result.id.toString())

                image.load(result.imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_img_preview)
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<PokemonResult>() {
            override fun areItemsTheSame(oldItem: PokemonResult, newItem: PokemonResult): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: PokemonResult,
                newItem: PokemonResult
            ): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }
}