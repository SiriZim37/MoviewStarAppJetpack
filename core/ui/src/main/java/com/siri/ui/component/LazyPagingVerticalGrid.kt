package com.siri.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.siri.ui.R

@Composable
fun <T : Any> LazyPagingVerticalGrid(
    items: LazyPagingItems<T>,
    columns: GridCells,
    itemContent: @Composable (T?) -> Unit,
    loadingContent: @Composable () -> Unit = { DefaultLoadingView() },
    errorContent: @Composable (String, () -> Unit) -> Unit = { message, retry ->
        DefaultErrorView(message, retry)
    },
    loadingItemContent: @Composable () -> Unit = { DefaultLoadingView() },
    errorItemContent: @Composable (String, () -> Unit) -> Unit = { message, retry ->
        DefaultErrorView(message, retry)
    }
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = items.loadState.refresh) {
            is LoadState.Loading -> loadingContent()
            is LoadState.Error -> {
                errorContent(
                    state.error.message ?: LocalContext.current.getString(R.string.error_unknown)
                ) {
                    items.refresh()
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = columns,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        count = items.itemCount,
                        key = { index -> items.peek(index)?.hashCode() ?: index }
                    ) { index ->
                        itemContent(items[index])
                    }

                    when (val appendState = items.loadState.append) {
                        is LoadState.Loading -> item(span = { GridItemSpan(maxLineSpan) }) {
                            loadingItemContent()
                        }
                        is LoadState.Error -> item(span = { GridItemSpan(maxLineSpan) }) {
                            errorItemContent(
                                appendState.error.message
                                    ?: LocalContext.current.getString(R.string.error_unknown)
                            ) {
                                items.retry()
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}
