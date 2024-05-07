package com.hossam.mobilemasrtask.util.extension

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


fun RecyclerView.addDivider() {
    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
}

/**
 * Sets up lazy pagination for the RecyclerView by adding a scroll listener.
 * When the last item in the list becomes visible, the [loadMore] lambda function is invoked.
 * @param loadMore Lambda function to be executed when more items need to be loaded.
 */
fun RecyclerView.onLazyPagination(loadMore: () -> Unit) {

    val layoutManager = this.layoutManager as LinearLayoutManager
    // Pagination listener
    val paginationListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val totalItemCount = layoutManager.itemCount
            val lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()

            if (lastVisibleItem == totalItemCount - 1) {
                // Load more items when the last item becomes visible
                loadMore()
            }
        }
    }

    // Attach pagination listener to RecyclerView
    this.addOnScrollListener(paginationListener)
}