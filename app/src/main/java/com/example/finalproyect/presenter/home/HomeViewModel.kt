package com.example.finalproyect.presenter.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproyect.domain.model.Event
import com.example.finalproyect.domain.usecase.event.GetUserEventsUseCase
import com.example.finalproyect.domain.usecase.event.SearchPublicEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserEventsUseCase: GetUserEventsUseCase,
    private val searchPublicEventsUseCase: SearchPublicEventsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchExpanded = MutableStateFlow(false)
    val isSearchExpanded: StateFlow<Boolean> = _isSearchExpanded.asStateFlow()

    init {
        loadUserEvents()
    }

    fun loadUserEvents(page: Int = 1, size: Int = 10) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            getUserEventsUseCase(page, size)
                .onSuccess { paginatedEvents ->
                    _uiState.value = _uiState.value.copy(
                        userEvents = if (page == 1) paginatedEvents.events else _uiState.value.userEvents + paginatedEvents.events,
                        isLoading = false,
                        canLoadMore = paginatedEvents.events.size == size
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message
                    )
                }
        }
    }

    fun searchPublicEvents(query: String, page: Int = 1, size: Int = 10) {
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(searchResults = emptyList())
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSearching = true, searchError = null)

            searchPublicEventsUseCase(query, page, size)
                .onSuccess { paginatedEvents ->
                    _uiState.value = _uiState.value.copy(
                        searchResults = if (page == 1) paginatedEvents.events else _uiState.value.searchResults + paginatedEvents.events,
                        isSearching = false,
                        canLoadMoreSearch = paginatedEvents.events.size == size
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isSearching = false,
                        searchError = exception.message
                    )
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(searchResults = emptyList())
        }
    }

    fun onSearchSubmit() {
        val query = _searchQuery.value
        if (query.isNotBlank()) {
            searchPublicEvents(query)
        }
    }

    fun setSearchExpanded(expanded: Boolean) {
        _isSearchExpanded.value = expanded
        if (!expanded) {
            _searchQuery.value = ""
            _uiState.value = _uiState.value.copy(searchResults = emptyList())
        }
    }

    fun refreshUserEvents() {
        loadUserEvents()
    }

    fun loadMoreUserEvents() {
        if (_uiState.value.canLoadMore && !_uiState.value.isLoading) {
            val currentPage = (_uiState.value.userEvents.size / 10) + 1
            loadUserEvents(currentPage)
        }
    }

    fun loadMoreSearchResults() {
        if (_uiState.value.canLoadMoreSearch && !_uiState.value.isSearching) {
            val currentPage = (_uiState.value.searchResults.size / 10) + 1
            searchPublicEvents(_searchQuery.value, currentPage)
        }
    }
}

// HomeUiState.kt
data class HomeUiState(
    val userEvents: List<Event> = emptyList(),
    val searchResults: List<Event> = emptyList(),
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val error: String? = null,
    val searchError: String? = null,
    val canLoadMore: Boolean = false,
    val canLoadMoreSearch: Boolean = false
)
