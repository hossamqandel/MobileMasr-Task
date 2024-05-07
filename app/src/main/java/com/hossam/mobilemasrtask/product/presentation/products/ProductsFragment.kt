package com.hossam.mobilemasrtask.product.presentation.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.hossam.mobilemasrtask.R
import com.hossam.mobilemasrtask.databinding.FragmentProductsBinding
import com.hossam.mobilemasrtask.product.domain.model.Product
import com.hossam.mobilemasrtask.product.presentation.products.util.ProductsState
import com.hossam.mobilemasrtask.util.extension.addDivider
import com.hossam.mobilemasrtask.util.extension.hide
import com.hossam.mobilemasrtask.util.extension.onLazyPagination
import com.hossam.mobilemasrtask.util.extension.setProgressColor
import com.hossam.mobilemasrtask.util.extension.setVisibleOrGone
import com.hossam.mobilemasrtask.util.extension.show
import com.hossam.mobilemasrtask.util.extension.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductsVM by viewModels()

    @Inject
    lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressed()
    }

    /**
     * Handles back button press event. When back button is pressed, it finishes the associated activity.
     */
    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        bindRecycler()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeToRefresh()
        observeProductsState()
        onProductClickEvent()
        setupRecyclerPagination()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Sets up swipe-to-refresh functionality for the RecyclerView.
     */
    private fun swipeToRefresh() {
        binding.swipeRefresh.setProgressColor(R.color.main_color_orange)
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            viewModel.refreshProducts()
        }
    }


    /**
     * Binds the RecyclerView with its adapter and other configurations.
     */
    private fun bindRecycler() {
        with(binding) {
            rvProducts.show()
            rvProducts.addDivider()
            rvProducts.adapter = productAdapter
        }
    }

    /**
     * Observes the state of products data and updates UI accordingly.
     */
    private fun observeProductsState() {
        // Observes products state and updates UI based on different states.
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.state.collectLatest { state ->
                    when (state) {

                        is ProductsState.Loading -> setProgressBarState(state.isLoading)

                        is ProductsState.Success -> {
                            binding.ivNoData.hide()
                            binding.rvProducts.show()
                            productAdapter.setProducts(state.products)
                            productAdapter.notifyDataSetChanged()
                        }

                        is ProductsState.NoProducts -> {
                            binding.rvProducts.hide()
                            binding.ivNoData.show()
                            showSnackbar(R.string.it_seems_there_are_no_content_available)
                        }

                        is ProductsState.Error -> showSnackbar(state.message)

                    }
                }
            }
        }
    }

    /**
     * Sets the visibility state of the progress bar.
     * @param isLoading Indicates whether the progress bar should be visible or not.
     */
    private fun setProgressBarState(isLoading: Boolean) {
        binding.progressBar.setVisibleOrGone(isLoading)
    }

    private fun onProductClickEvent() {
        productAdapter.setOnProductClickEvent { product: Product ->
            // Navigates to the product detail fragment when a product item is clicked.
            val action = ProductsFragmentDirections.actionProductsFragmentToProductDetailFragment3(product.id)
            findNavController().navigate(action)
        }
    }

    /**
     * Sets up lazy pagination for the RecyclerView.
     */
    private fun setupRecyclerPagination() {
        with(binding) {
            rvProducts.onLazyPagination {
                viewModel.upgradePage()
                viewModel.refreshProducts()
            }
        }
    }

}