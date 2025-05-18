package com.siri.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.siri.data.model.Movie
import com.siri.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class UpcomingViewModel @Inject constructor(
    repository: MovieRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory

//    val upcomingmoviePagingDataFlow: Flow<PagingData<Movie>> =
//        repository.getUpcomingMovies()
//            .cachedIn(viewModelScope)

    // ให้ ViewModel expose flow ของ PagingData ที่กรองตาม searchQuery และ selectedCategory
    val upcomingmoviePagingDataFlow: Flow<PagingData<Movie>> =
        _searchQuery.flatMapLatest { query ->
            _selectedCategory.flatMapLatest { category ->
                repository.getUpcomingMovies(query, category)
            }
        }.cachedIn(viewModelScope)

    val pagingDataFlow: Flow<PagingData<Movie>> = _selectedCategory.flatMapLatest { category ->
        when(category) {
            "Nowplaying" -> repository.getNowplayingMovies()
            "Popular" -> repository.getPopularMovies()
            else -> repository.getUpcomingMovies()
        }
    }.cachedIn(viewModelScope)

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

}