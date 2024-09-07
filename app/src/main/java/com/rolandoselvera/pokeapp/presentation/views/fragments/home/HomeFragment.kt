package com.rolandoselvera.pokeapp.presentation.views.fragments.home

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rolandoselvera.pokeapp.R
import com.rolandoselvera.pokeapp.common.internet.ConnectivityUtil
import com.rolandoselvera.pokeapp.data.model.PokemonResult
import com.rolandoselvera.pokeapp.databinding.FragmentHomeBinding
import com.rolandoselvera.pokeapp.presentation.adapters.PokemonListAdapter
import com.rolandoselvera.pokeapp.presentation.viewmodels.home.HomeViewModel
import com.rolandoselvera.pokeapp.presentation.views.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var adapter: PokemonListAdapter
    private var localPokemonList: List<PokemonResult>? = listOf()

    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun initializeViews() {
        initToolbar()
        hidePokemonList()
    }

    override fun initializeViewModel() {
        homeViewModel.pokemonList.observe(viewLifecycleOwner) { pokemonList ->
            hideProgress()
            showPokemonList()

            if (ConnectivityUtil(context).checkConnectivity()) {
                localPokemonList = pokemonList.getOrNull()?.results
                adapter.submitList(localPokemonList)

                if (localPokemonList.isNullOrEmpty()) {
                    hidePokemonList()
                }
                binding.swipeRefresh.isRefreshing = false
            } else {
                binding.swipeRefresh.isRefreshing = false
                hidePokemonList()
                showAlert(getString(R.string.app_name), getString(R.string.no_internet)) {
                    finishApp()
                }
            }
        }

        homeViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            hideProgress()
            binding.swipeRefresh.isRefreshing = false
            showAlert(getString(R.string.app_name), errorMessage) {
                hidePokemonList()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.fieldSearch.text = null
        binding.swipeRefresh.setOnRefreshListener {
            fetchAllPokemons()
            binding.swipeRefresh.isRefreshing = true
            hideKeyboard()
            showProgress()
        }
        fetchAllPokemons()
        setupSearch()
        setupAdapter()
    }

    private fun fetchAllPokemons() {
        showProgress()
        hidePokemonList()
        homeViewModel.fetchAllPokemons(limit = 20, offset = 0)
    }

    private fun setupAdapter() {
        adapter = PokemonListAdapter(requireContext()) { pokemon ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                pokemon.id
            )
            this.findNavController().navigate(action)
        }

        binding.recyclerView.adapter = adapter
    }

    private fun setupSearch() {
        binding.apply {
            search.setOnClickListener {
                val userSearch = fieldSearch.text.toString()

                if (userSearch.trim().isNotBlank()) {
                    searchPokemonLocally(userSearch.lowercase())
                    tilSearch.isErrorEnabled = false
                    tilSearch.error = null
                } else {
                    tilSearch.isErrorEnabled = true
                    tilSearch.error = getString(R.string.empty_field)
                }

                hideKeyboard()
            }

            fieldSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(
                    searchText: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    searchText: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    if (searchText.length > 2) {
                        searchPokemonLocally(searchText.toString().lowercase())
                        tilSearch.isErrorEnabled = false
                        tilSearch.error = null
                    } else if (searchText.isEmpty()) {
                        hideKeyboard()
                        adapter.submitList(localPokemonList)
                        showPokemonList()
                    }
                }
            })
        }
    }

    private fun searchPokemonLocally(query: String) {
        val filteredList = localPokemonList?.filter { pokemon ->
            pokemon.name.lowercase().contains(query)
        }

        if (!filteredList.isNullOrEmpty() && filteredList.isNotEmpty()) {
            adapter.submitList(filteredList)
            showPokemonList()
        } else {
            hidePokemonList()
        }
    }

    private fun showPokemonList() {
        binding.containerState.root.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun hidePokemonList() {
        binding.recyclerView.visibility = View.GONE
        binding.containerState.root.visibility = View.VISIBLE
    }
}