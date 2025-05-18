package com.siri.upcoming


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.siri.data.model.Movie
import androidx.hilt.navigation.compose.hiltViewModel
import com.siri.upcoming.R
import com.siri.ui.component.AppBar
import com.siri.ui.component.LazyPagingVerticalGrid
import kotlinx.coroutines.launch


@Composable
fun UpcomingMoviesScreen(
viewModel: UpcomingViewModel = hiltViewModel()
) {
//    val items = viewModel.upcomingmoviePagingDataFlow.collectAsLazyPagingItems()

    // แทนที่จะเก็บ selectedCategory ใน composable ให้ใช้จาก viewModel เลย
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val items = viewModel.pagingDataFlow.collectAsLazyPagingItems()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val categories = listOf("All","Popular" ,"Nowplaying"  ,"Action", "Drama", "Comedy", "Horror")
//    var selectedCategory by remember { mutableStateOf("All") }
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            AppBar(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    viewModel.selectCategory(category)  // แจ้งให้ ViewModel โหลดข้อมูลใหม่
                },
                isSearchActive = isSearchActive,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onSearchClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            "Search clicked",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            )
        },
        containerColor = Color.Black
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = Color.Black
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = selectedCategory,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyPagingVerticalGrid(
                    items = items,
                    columns = GridCells.Adaptive(minSize = 120.dp),
                    itemContent = { movie ->
                        val showMovie =
                            selectedCategory == "All" || movie?.category == selectedCategory
                        if (movie != null && showMovie) {
                            MoviePosterCard(movie) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "select: ${movie.title}",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }

}
@Preview(showBackground = true)
@Composable
fun PreviewMovieItem() {
    val dummyMovie = Movie(
        id = 1,
        title = "Immaculate",
        category = "Action",
        cover = "/5Eip60UDiPLASyKjmHPMruggTc4.jpg"
    )

    MoviePosterCard(movie = dummyMovie) {}
}