package com.gmail.orlandroyd.testepratico.presentation.my_products

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.gmail.orlandroyd.testepratico.R
import com.gmail.orlandroyd.testepratico.databinding.FragmentMyProductsBinding
import com.gmail.orlandroyd.testepratico.domain.model.Product
import com.gmail.orlandroyd.testepratico.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyProductsFragment : Fragment(R.layout.fragment_my_products),
    MyProductAdapter.OnItemClickListener {

    // BINDING
    private var _binding: FragmentMyProductsBinding? = null
    private val binding get() = _binding!!

    // VIEW MODEL
    private val viewModel by viewModels<MyProductsViewModel>()

    private lateinit var productAdapter: MyProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init binding
        _binding = FragmentMyProductsBinding.bind(view)

        binding.recycler.setHasFixedSize(true)
        productAdapter = MyProductAdapter(this)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.products.collect {
                    when (it.status) {

                        DataState.Status.SUCCESS -> {
                            displayProgressBar(false)
                            productAdapter.submitList(it.data)
                            binding.recycler.adapter = productAdapter
                        }

                        DataState.Status.ERROR -> {
                            displayProgressBar(false)
                            context.toast(getString(R.string.error))
                        }

                        DataState.Status.LOADING -> {
                            displayProgressBar(true)
                        }

                        DataState.Status.FAILURE -> {
                            displayProgressBar(false)
                            val msg = it.data ?: emptyList()
                            context.toast("$msg")
                        }
                    }
                }.exhaustive
            }
        }
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        if (isDisplayed) {
            binding.progress.setVisible()
            binding.recycler.setGone()
        } else {
            binding.progress.setGone()
            binding.recycler.setVisible()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(product: Product) {
        val action = MyProductsFragmentDirections.actionNavMyProductsToNavDetail(product)
        findNavController().navigate(action)
    }
}