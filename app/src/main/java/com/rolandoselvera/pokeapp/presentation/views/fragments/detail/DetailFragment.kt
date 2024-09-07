package com.rolandoselvera.pokeapp.presentation.views.fragments.detail

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.material.chip.Chip
import com.rolandoselvera.pokeapp.R
import com.rolandoselvera.pokeapp.common.internet.ConnectivityUtil
import com.rolandoselvera.pokeapp.data.model.Pokemon
import com.rolandoselvera.pokeapp.databinding.FragmentDetailBinding
import com.rolandoselvera.pokeapp.presentation.adapters.PokemonSpritesAdapter
import com.rolandoselvera.pokeapp.presentation.viewmodels.detail.DetailViewModel
import com.rolandoselvera.pokeapp.presentation.views.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    private val args: DetailFragmentArgs by navArgs()
    private val detailViewModel: DetailViewModel by viewModels()
    private var pokemonId = 0

    override fun getViewBinding() = FragmentDetailBinding.inflate(layoutInflater)

    override fun initializeViews() {
        pokemonId = args.pokemonId
        showProgress()
        setupToolbar()
        hidePokemonDetail()
    }

    override fun initializeViewModel() {
        detailViewModel.pokemon.observe(viewLifecycleOwner) { pokemon ->
            hideProgress()
            showPokemonDetail()

            if (ConnectivityUtil(context).checkConnectivity()) {
                setPokemonStats(pokemon)
            } else {
                hidePokemonDetail()
                showAlert(getString(R.string.app_name), getString(R.string.no_internet)) {
                    goToHome()
                }
            }
        }

        detailViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            hideProgress()
            showAlert(getString(R.string.app_name), errorMessage) {
                goToHome()
            }
            hidePokemonDetail()
        }
    }

    override fun onResume() {
        super.onResume()
        fetchSinglePokemon(pokemonId)
    }

    private fun fetchSinglePokemon(pokemonId: Int) {
        hidePokemonDetail()
        detailViewModel.fetchSinglePokemon(pokemonId)
    }

    private fun setPokemonStats(pokemon: Result<Pokemon>) {
        val pokemonImg = pokemon.getOrNull()?.sprites?.other?.officialArtwork?.frontDefault
        val pokemonName = pokemon.getOrNull()?.name ?: ""
        val pokemonHeight = pokemon.getOrNull()?.height ?: 0
        val pokemonWeight = pokemon.getOrNull()?.weight ?: 0
        val pokemonTypes = pokemon.getOrNull()?.types ?: emptyList()
        val pokemonAbilities = pokemon.getOrNull()?.abilities ?: emptyList()
        val pokemonSprites = pokemon.getOrNull()?.sprites

        binding.apply {
            imgPokemon.load(pokemonImg) {
                crossfade(true)
                placeholder(R.drawable.pokemon_icon)
            }

            txtName.text = pokemonName.replaceFirstChar {
                it.uppercase()
            }

            chipGroup1.removeAllViews()
            chipGroup2.removeAllViews()
            chipGroup3.removeAllViews()

            val chipHeight = Chip(requireContext()).apply {
                text = getString(R.string.pokemon_height, pokemonHeight.toString())
                isClickable = false
                isCheckable = false
                chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.propertiesColor
                    )
                )
            }

            val chipWeight = Chip(requireContext()).apply {
                text = getString(R.string.pokemon_weight, pokemonWeight.toString())
                isClickable = false
                isCheckable = false
                chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.propertiesColor
                    )
                )
            }

            chipGroup1.addView(chipHeight)
            chipGroup1.addView(chipWeight)

            pokemonTypes.forEach { type ->
                val chip = Chip(requireContext()).apply {
                    text = getString(
                        R.string.pokemon_type,
                        type.type.name.replaceFirstChar { it.uppercase() })
                    isClickable = false
                    isCheckable = false
                    chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                }

                chipGroup2.addView(chip)
            }

            pokemonAbilities.forEach { abilities ->
                val chip = Chip(requireContext()).apply {
                    text = getString(
                        R.string.pokemon_type,
                        abilities.ability.name.replaceFirstChar { it.uppercase() })
                    isClickable = false
                    isCheckable = false
                    chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.abilitiesColor
                        )
                    )
                }

                chipGroup3.addView(chip)
            }

            txtId.text = getString(R.string.pokemon_id, pokemonId.toString())
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = pokemonSprites?.let { PokemonSpritesAdapter(it) }
            }

        }
    }

    private fun goToHome() {
        this.findNavController().popBackStack()
    }

    private fun showPokemonDetail() {
        binding.containerState.root.visibility = View.GONE
        binding.imgPokemon.visibility = View.VISIBLE
        binding.txtName.visibility = View.VISIBLE
        binding.txtId.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun hidePokemonDetail() {
        binding.imgPokemon.visibility = View.GONE
        binding.txtName.visibility = View.GONE
        binding.txtId.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.containerState.root.visibility = View.VISIBLE
    }
}