package com.rolandoselvera.pokeapp.presentation.views.fragments.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rolandoselvera.pokeapp.R
import com.rolandoselvera.pokeapp.common.internet.ConnectivityUtil
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

    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun initializeViews() {
        binding.swipeRefresh.setOnRefreshListener {
            fetchAllPokemons()
            binding.swipeRefresh.isRefreshing = true
            showProgress()
        }
        fetchAllPokemons()
        setupAdapter()
    }

    override fun initializeViewModel() {
        showProgress()

        homeViewModel.pokemonList.observe(viewLifecycleOwner) { pokemonList ->
            hideProgress()
            if (ConnectivityUtil(context).checkConnectivity()) {
                adapter.submitList(pokemonList.getOrNull()?.results)
                binding.swipeRefresh.isRefreshing = false
            } else {
                binding.swipeRefresh.isRefreshing = false
                showAlert(getString(R.string.app_name), getString(R.string.no_internet)) {
                    finishApp()
                }
            }
        }

        homeViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            hideProgress()
            binding.swipeRefresh.isRefreshing = false
            showAlert(getString(R.string.app_name), errorMessage) {

            }
        }
    }

    private fun fetchAllPokemons() {
        homeViewModel.fetchAllPokemons(limit = 20, offset = 0)
    }

    private fun setupAdapter() {
        adapter = PokemonListAdapter(requireContext()) {
            toast(it.name)
        }

        binding.recyclerView.adapter = adapter
    }
}