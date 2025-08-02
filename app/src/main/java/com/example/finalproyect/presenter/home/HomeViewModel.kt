package com.example.finalproyect.presenter.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproyect.domain.model.EventDetail
import com.example.finalproyect.domain.usecase.event.GetUserOrganizedEventsUseCase
import com.example.finalproyect.domain.usecase.event.GetUserPurchasedEventsUseCase
import com.example.finalproyect.domain.usecase.event.SearchPublicEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserEventsUseCase: GetUserOrganizedEventsUseCase,
    private val getUserPurchasedEventsUseCase: GetUserPurchasedEventsUseCase,
    private val searchPublicEventsUseCase: SearchPublicEventsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchExpanded = MutableStateFlow(false)
    val isSearchExpanded: StateFlow<Boolean> = _isSearchExpanded.asStateFlow()

    init {
        loadUserOrganizedEvents()
        loadUserPurchasedEvents()
    }

    private fun loadUserPurchasedEvents(page: Int = 1, size: Int = 10) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingPurchasedEvents =  true, error = null)

            getUserPurchasedEventsUseCase(page, size)
                .onSuccess { paginatedEvents ->
                    _uiState.value = _uiState.value.copy(
                        userOrganizedEvents = if (page == 1) paginatedEvents.events else _uiState.value.userPurchasedEvents + paginatedEvents.events,
                        isLoadingPurchasedEvents = false,
                        canLoadMorePurchasedEvents = paginatedEvents.events.size == size
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingPurchasedEvents = false,
                        error = exception.message
                    )
                }
        }
    }

    private fun loadUserOrganizedEvents(page: Int = 1, size: Int = 10) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingOrganizedEvents = true, error = null)

            getUserEventsUseCase(page, size)
                .onSuccess { paginatedEvents ->
                    _uiState.value = _uiState.value.copy(
                        userOrganizedEvents = if (page == 1) paginatedEvents.events else _uiState.value.userOrganizedEvents + paginatedEvents.events,
                        isLoadingOrganizedEvents = false,
                        canLoadMoreOrganizedEvents = paginatedEvents.events.size == size
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingOrganizedEvents = false,
                        error = exception.message
                    )
                }
        }
    }

    private fun searchPublicEvents(query: String, page: Int = 1, size: Int = 10) {
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

    fun refreshUserOrganizedEvents() {
        loadUserOrganizedEvents()
    }
    fun refreshUserPurchasedEvents(){
        loadUserPurchasedEvents()
    }

    fun loadMoreUserOrganizedEvents() {
        if (_uiState.value.canLoadMoreOrganizedEvents && !_uiState.value.isLoadingOrganizedEvents) {
            val currentPage = (_uiState.value.userOrganizedEvents.size / 10) + 1
            loadUserOrganizedEvents(page = currentPage)
        }
    }

    fun loadMoreUserPurchasedEvents() {
        if (_uiState.value.canLoadMorePurchasedEvents && !_uiState.value.isLoadingPurchasedEvents) {
            val currentPage = (_uiState.value.userPurchasedEvents.size / 10) + 1
            loadUserPurchasedEvents(page = currentPage)
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
    val userOrganizedEvents: List<EventDetail> = emptyList(),
    val userPurchasedEvents: List<EventDetail> = emptyList(),
    val searchResults: List<EventDetail> = emptyList(),
    val isLoadingOrganizedEvents: Boolean = false,
    val isLoadingPurchasedEvents: Boolean = false,
    val isSearching: Boolean = false,
    val error: String? = null,
    val searchError: String? = null,
    val canLoadMoreOrganizedEvents: Boolean = false,
    val canLoadMorePurchasedEvents: Boolean = false,
    val canLoadMoreSearch: Boolean = false
)
