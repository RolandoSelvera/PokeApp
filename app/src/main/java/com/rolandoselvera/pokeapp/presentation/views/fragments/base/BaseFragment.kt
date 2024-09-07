package com.rolandoselvera.pokeapp.presentation.views.fragments.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.rolandoselvera.pokeapp.R
import com.rolandoselvera.pokeapp.presentation.views.activities.main.MainActivity

/**
 * A simple [Fragment] subclass.
 * Use the [BaseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null
    val binding get() = _binding!!

    private lateinit var dialog: MaterialAlertDialogBuilder

    abstract fun getViewBinding(): VB

    abstract fun initializeViews()

    abstract fun initializeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = MaterialAlertDialogBuilder(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViewModel()
        initializeViews()
        hideKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard()
        _binding = null
    }

    fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }

    fun setupToolbar() {
        setHasOptionsMenu(true)
        val toolbar = (activity as AppCompatActivity?)?.supportActionBar
        toolbar?.show()
        toolbar?.setDisplayHomeAsUpEnabled(true)
    }

    fun hideToolbar() {
        val toolbar = (activity as AppCompatActivity?)?.supportActionBar
        toolbar?.hide()
    }

    fun fabShrink(recyclerView: RecyclerView, fab: ExtendedFloatingActionButton) {
        binding.apply {
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        fab.shrink()
                    } else {
                        fab.extend()
                    }
                }
            })
        }
    }

    fun toast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun showAlert(title: String?, message: String?, onAccept: () -> Unit) {
        if (::dialog.isInitialized) {
            dialog
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(requireContext().getString(R.string.accept)) { _, _ ->
                    onAccept()
                }
                .setOnDismissListener {
                    it.dismiss()
                }
                .show()
        }
    }

    fun showAlert(title: String?, message: String?, onAccept: () -> Unit, onCancel: () -> Unit) {
        if (::dialog.isInitialized) {
            dialog
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.accept)) { _, _ ->
                    onAccept()
                }
                .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                    onCancel()
                }
                .setOnDismissListener {
                    it.dismiss()
                }
                .show()
        }
    }

    fun showProgress() {
        (activity as MainActivity).showProgress()
    }

    fun hideProgress() {
        (activity as MainActivity).hideProgress()
    }

    fun finishApp() {
        requireActivity().finish()
    }
}