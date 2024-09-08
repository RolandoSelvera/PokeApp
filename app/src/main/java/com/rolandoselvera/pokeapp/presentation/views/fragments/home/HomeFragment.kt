package com.rolandoselvera.pokeapp.presentation.views.fragments.home

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private var localPokemonList: MutableList<PokemonResult> = mutableListOf()
    private var pokemonByName: MutableList<PokemonResult> = mutableListOf()

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
                val newPokemonList = pokemonList?.getOrNull()?.results ?: emptyList()

                if (newPokemonList.isNotEmpty()) {
                    localPokemonList.addAll(newPokemonList)
                    adapter.submitList(localPokemonList)
                }

                if (localPokemonList.isEmpty()) {
                    hidePokemonList()
                }
                setLoadingToFalse()
            } else {
                setLoadingToFalse()
                hidePokemonList()
                showAlert(getString(R.string.app_name), getString(R.string.no_internet)) {
                    finishApp()
                }
            }
        }

        homeViewModel.pokemon.observe(viewLifecycleOwner) { pokemon ->
            hideProgress()
            showPokemonList()

            if (ConnectivityUtil(context).checkConnectivity()) {
                val singlePokemon = pokemon?.getOrNull()
                val result = PokemonResult().copy(
                    id = singlePokemon?.id ?: 0,
                    name = singlePokemon?.name ?: "",
                    imageUrl = pokemon.getOrNull()?.sprites?.other?.officialArtwork?.frontDefault
                )

                if (result.id != 0) {
                    pokemonByName.addAll(listOf(result))
                    adapter.submitList(pokemonByName)
                }

                if (pokemonByName.isEmpty()) {
                    hidePokemonList()
                }

                binding.swipeRefresh.isRefreshing = false
            } else {
                setLoadingToFalse()
                hidePokemonList()
                showAlert(getString(R.string.app_name), getString(R.string.no_internet)) {
                    finishApp()
                }
            }
        }

        homeViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            hideProgress()
            setLoadingToFalse()
            showAlert(getString(R.string.app_name), errorMessage) {
                hidePokemonList()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.fieldSearch.text = null
        setupAdapter()
        clearPokemonLists()
        fetchAllPokemons()
        setupSwipeRefresh()
        setupSearch()
        setupButton()
    }

    private fun setupButton() {
        binding.apply {
            fabShrink(recyclerView, fabTop)
            fabTop.setOnClickListener {
                recyclerView.smoothScrollToPosition(0)
            }
        }
    }

    private fun fetchAllPokemons() {
        showProgress()
        hidePokemonList()
        homeViewModel.fetchAllPokemons()
    }

    private fun fetchSinglePokemon(name: String) {
        homeViewModel.fetchSinglePokemon(name)
    }

    private fun setupAdapter() {
        adapter = PokemonListAdapter(requireContext()) { pokemon ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                pokemon.id
            )
            this.findNavController().navigate(action)
        }

        binding.recyclerView.adapter = adapter
        setupInfiniteScroll()
    }

    private fun setupInfiniteScroll() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!homeViewModel.isSearching && !homeViewModel.isLoading &&
                    firstVisibleItemPosition + visibleItemCount >= totalItemCount
                ) {
                    fetchAllPokemons()
                }
            }
        })
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            homeViewModel.resetPagination()
            localPokemonList.clear()
            fetchAllPokemons()
            binding.swipeRefresh.isRefreshing = true
            hideKeyboard()
            showProgress()
        }
    }

    private fun setupSearch() {
        binding.apply {
            search.setOnClickListener {
                val userSearch = fieldSearch.text.toString().trim().lowercase()

                if (userSearch.isNotBlank()) {
                    homeViewModel.isSearching = true
                    if (localPokemonList.any { it.name.lowercase() == userSearch }) {
                        searchPokemonLocally(userSearch)
                    } else {
                        fetchSinglePokemon(userSearch)
                    }
                    tilSearch.isErrorEnabled = false
                    tilSearch.error = null
                } else {
                    homeViewModel.isSearching = false
                    tilSearch.isErrorEnabled = true
                    tilSearch.error = getString(R.string.empty_field)
                    adapter.submitList(localPokemonList)
                    showPokemonList()
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
                    val searchQuery = searchText.toString().trim().lowercase()

                    if (searchQuery.length > 2) {
                        homeViewModel.isSearching = true
                        if (localPokemonList.any { it.name.lowercase().contains(searchQuery) }) {
                            searchPokemonLocally(searchQuery)
                        } else {
                            fetchSinglePokemon(searchQuery)
                        }
                        tilSearch.isErrorEnabled = false
                        tilSearch.error = null
                    } else if (searchQuery.isEmpty()) {
                        homeViewModel.isSearching = false
                        hideKeyboard()
                        adapter.submitList(localPokemonList)
                        showPokemonList()
                    }
                }
            })
        }
    }

    private fun searchPokemonLocally(query: String) {
        val filteredList = localPokemonList.filter { pokemon ->
            pokemon.name.lowercase().contains(query)
        }

        if (filteredList.isNotEmpty()) {
            adapter.submitList(filteredList)
            showPokemonList()
        } else {
            hidePokemonList()
        }
    }

    private fun clearPokemonLists() {
        localPokemonList = mutableListOf()
        pokemonByName = mutableListOf()
        adapter.submitList(emptyList())
        homeViewModel.resetPagination()
    }

    private fun setLoadingToFalse() {
        binding.swipeRefresh.isRefreshing = false
        homeViewModel.isLoading = false
        homeViewModel.isSearching = false
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