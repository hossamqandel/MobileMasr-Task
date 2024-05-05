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
import androidx.recyclerview.widget.LinearLayoutManager
import com.hossam.mobilemasrtask.databinding.FragmentProductsBinding
import com.hossam.mobilemasrtask.product.domain.model.Product
import com.hossam.mobilemasrtask.product.presentation.products.util.ProductsState
import com.hossam.mobilemasrtask.util.extension.addDivider
import com.hossam.mobilemasrtask.util.extension.hide
import com.hossam.mobilemasrtask.util.extension.setVisibleOrGone
import com.hossam.mobilemasrtask.util.extension.show
import com.hossam.mobilemasrtask.util.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductsVM by viewModels()
    @Inject lateinit var productAdapter: ProductAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressed()
    }

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
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun swipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            viewModel.getProducts()
        }
    }


    private fun bindRecycler() {
        with(binding) {
            layoutManager = LinearLayoutManager(requireContext()).apply { orientation = LinearLayoutManager.VERTICAL }
            rvProducts.show()
            rvProducts.layoutManager = layoutManager
            rvProducts.addDivider()
            rvProducts.adapter = productAdapter
        }
    }

    private fun observeProductsState() {
        viewModel.getProducts()
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
                        }

                        is ProductsState.Error -> showToast(getString(state.message))

                    }
                }
            }
        }
    }

    private fun setProgressBarState(isLoading: Boolean) {
        binding.progressBar.setVisibleOrGone(isLoading)
    }

    private fun onProductClickEvent() {
        productAdapter.setOnProductClickEvent { product: Product ->
            val action = ProductsFragmentDirections.actionProductsFragmentToProductDetailFragment3(product.id)
            findNavController().navigate(action)
        }
    }

//    private fun setupRecyclerPagination() {
//        with(binding) {
//            rvProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    val visibleItemCount = layoutManager.childCount
//                    val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
//                    val total = productAdapter.itemCount
//
//                    if ((visibleItemCount + pastVisibleItem) >= total) {
//                        viewModel.upgradePage()
//                        viewModel.getProducts()
//                    }
//                    super.onScrolled(recyclerView, dx, dy)
//                }
//            })
//        }
//    }

}