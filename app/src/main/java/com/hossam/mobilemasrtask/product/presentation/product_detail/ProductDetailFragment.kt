package com.hossam.mobilemasrtask.product.presentation.product_detail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hossam.mobilemasrtask.R
import com.hossam.mobilemasrtask.databinding.FragmentProductDetailBinding
import com.hossam.mobilemasrtask.product.domain.model.Product
import com.hossam.mobilemasrtask.product.presentation.product_detail.util.ProductDetailState
import com.hossam.mobilemasrtask.util.extension.hide
import com.hossam.mobilemasrtask.util.extension.onClick
import com.hossam.mobilemasrtask.util.extension.setVisibleOrGone
import com.hossam.mobilemasrtask.util.extension.show
import com.hossam.mobilemasrtask.util.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductDetailVM by viewModels()
    @Inject lateinit var productImageAdapter: ProductImageAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val delayMillis = 2000L
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        linearLayoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBackEvent()
        swipeToRefresh()
        bindProductImagesRecyclerAdapter()
        observeProductState()
    }

    override fun onResume() {
        super.onResume()
        startAutoScroll()
    }

    override fun onPause() {
        super.onPause()
        stopAutoScroll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun swipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            viewModel.getProductDetail()
        }
    }

    private fun startAutoScroll() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                val lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                val itemCount = productImageAdapter.itemCount

                if (lastVisibleItemPosition < itemCount - 1) {
                    binding.rvProductImages.smoothScrollToPosition(lastVisibleItemPosition + 1)
                } else {
                    binding.rvProductImages.smoothScrollToPosition(0)
                }

                handler.postDelayed(this, delayMillis)
            }
        }, delayMillis)
    }

    private fun stopAutoScroll() {
        handler.removeCallbacksAndMessages(null)
    }

    private fun setProgressBarState(isLoading: Boolean) {
        binding.progressBar.setVisibleOrGone(isLoading)
    }

    private fun bindProductImagesRecyclerAdapter() {
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvProductImages.layoutManager = linearLayoutManager
        binding.rvProductImages.adapter = productImageAdapter
    }

    private fun bindProductImages(images: Set<String>) {
        productImageAdapter.setImages(images)
        productImageAdapter.notifyDataSetChanged()
    }



    private fun bindProductDetail(product: Product) {
        with(binding) {
            tvProductTitle.text = product.title
            tvProductDescription.text = product.description
            tvProductPrice.text = "${product.price} ${getString(R.string.egp)}"
        }
    }

    private fun observeProductState() {
        viewModel.getProductDetail()
        with(binding){
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    viewModel.state.collectLatest { state ->
                        when (state) {

                            is ProductDetailState.Loading -> setProgressBarState(state.isLoading)

                            is ProductDetailState.Success -> {
                                productNotFoundContainer.hide()
                                productDetailContainer.show()
                                bindProductDetail(state.product)
                                bindProductImages(state.product.images)
                            }

                            is ProductDetailState.Error -> {
                                productDetailContainer.hide()
                                tvErrorMessage.text = getString(state.message)
                                productNotFoundContainer.show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onBackEvent(){
        binding.btnBack.onClick { findNavController().popBackStack() }
    }
}