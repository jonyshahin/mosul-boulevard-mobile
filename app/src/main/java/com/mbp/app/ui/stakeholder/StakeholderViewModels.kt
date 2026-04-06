package com.mbp.app.ui.stakeholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbp.app.data.model.CustomerDto
import com.mbp.app.data.model.DashboardStatsResponse
import com.mbp.app.data.model.SalesSummaryResponse
import com.mbp.app.data.model.StatusReportResponse
import com.mbp.app.data.model.TowerUnitDto
import com.mbp.app.data.model.VillaDto
import com.mbp.app.data.repository.AuthRepository
import com.mbp.app.data.repository.StakeholderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ---------- Dashboard ----------

data class DashboardState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val stats: DashboardStatsResponse? = null,
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repo: StakeholderRepository,
    private val authRepo: AuthRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init { load() }

    fun load() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val r = repo.getDashboardStats()
            _state.update {
                if (r.isSuccess) it.copy(isLoading = false, stats = r.getOrNull())
                else it.copy(isLoading = false, error = r.exceptionOrNull()?.message)
            }
        }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            authRepo.logout()
            onDone()
        }
    }
}

// ---------- Villas list ----------

data class VillasListState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val items: List<VillaDto> = emptyList(),
    val page: Int = 1,
    val lastPage: Int = 1,
    val search: String = "",
    val villaTypeId: Int? = null,
    val canLoadMore: Boolean = false,
)

@HiltViewModel
class VillasListViewModel @Inject constructor(
    private val repo: StakeholderRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(VillasListState())
    val state: StateFlow<VillasListState> = _state.asStateFlow()

    init { reload() }

    fun setSearch(q: String) {
        _state.update { it.copy(search = q) }
        reload()
    }

    fun setType(id: Int?) {
        _state.update { it.copy(villaTypeId = id) }
        reload()
    }

    fun reload() {
        _state.update { it.copy(isLoading = true, error = null, page = 1, items = emptyList()) }
        fetchPage(1, append = false)
    }

    fun loadMore() {
        val s = _state.value
        if (s.isLoading || !s.canLoadMore) return
        fetchPage(s.page + 1, append = true)
    }

    private fun fetchPage(page: Int, append: Boolean) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val s = _state.value
            val r = repo.getVillas(page, s.search.ifBlank { null }, s.villaTypeId)
            _state.update { cur ->
                if (r.isSuccess) {
                    val resp = r.getOrNull()!!
                    val items = if (append) cur.items + resp.data else resp.data
                    val last = resp.meta?.lastPage ?: page
                    cur.copy(
                        isLoading = false,
                        items = items,
                        page = page,
                        lastPage = last,
                        canLoadMore = page < last,
                    )
                } else {
                    cur.copy(isLoading = false, error = r.exceptionOrNull()?.message)
                }
            }
        }
    }
}

// ---------- Towers list ----------

data class TowersListState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val items: List<TowerUnitDto> = emptyList(),
    val page: Int = 1,
    val lastPage: Int = 1,
    val search: String = "",
    val towerDefId: Int? = null,
    val canLoadMore: Boolean = false,
)

@HiltViewModel
class TowersListViewModel @Inject constructor(
    private val repo: StakeholderRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(TowersListState())
    val state: StateFlow<TowersListState> = _state.asStateFlow()

    init { reload() }

    fun setSearch(q: String) {
        _state.update { it.copy(search = q) }
        reload()
    }

    fun setTower(id: Int?) {
        _state.update { it.copy(towerDefId = id) }
        reload()
    }

    fun reload() {
        _state.update { it.copy(isLoading = true, error = null, page = 1, items = emptyList()) }
        fetchPage(1, append = false)
    }

    fun loadMore() {
        val s = _state.value
        if (s.isLoading || !s.canLoadMore) return
        fetchPage(s.page + 1, append = true)
    }

    private fun fetchPage(page: Int, append: Boolean) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val s = _state.value
            val r = repo.getTowerUnits(page, s.search.ifBlank { null }, s.towerDefId)
            _state.update { cur ->
                if (r.isSuccess) {
                    val resp = r.getOrNull()!!
                    val items = if (append) cur.items + resp.data else resp.data
                    val last = resp.meta?.lastPage ?: page
                    cur.copy(
                        isLoading = false,
                        items = items,
                        page = page,
                        lastPage = last,
                        canLoadMore = page < last,
                    )
                } else {
                    cur.copy(isLoading = false, error = r.exceptionOrNull()?.message)
                }
            }
        }
    }
}

// ---------- Reports ----------

data class ReportsState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val sales: SalesSummaryResponse? = null,
    val structural: StatusReportResponse? = null,
    val finishing: StatusReportResponse? = null,
    val facade: StatusReportResponse? = null,
)

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repo: StakeholderRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ReportsState())
    val state: StateFlow<ReportsState> = _state.asStateFlow()

    init { load() }

    fun load() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val sales = repo.getSalesSummary()
            val struct = repo.getStructuralStatus()
            val fin = repo.getFinishingStatus()
            val fac = repo.getFacadeStatus()
            _state.update {
                it.copy(
                    isLoading = false,
                    sales = sales.getOrNull(),
                    structural = struct.getOrNull(),
                    finishing = fin.getOrNull(),
                    facade = fac.getOrNull(),
                    error = listOf(sales, struct, fin, fac)
                        .firstOrNull { r -> r.isFailure }
                        ?.exceptionOrNull()?.message
                )
            }
        }
    }
}

// ---------- Customers ----------

data class CustomersListState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val items: List<CustomerDto> = emptyList(),
    val page: Int = 1,
    val lastPage: Int = 1,
    val search: String = "",
    val canLoadMore: Boolean = false,
)

@HiltViewModel
class CustomersListViewModel @Inject constructor(
    private val repo: StakeholderRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(CustomersListState())
    val state: StateFlow<CustomersListState> = _state.asStateFlow()

    init { reload() }

    fun setSearch(q: String) {
        _state.update { it.copy(search = q) }
        reload()
    }

    fun reload() {
        _state.update { it.copy(isLoading = true, error = null, page = 1, items = emptyList()) }
        fetchPage(1, append = false)
    }

    fun loadMore() {
        val s = _state.value
        if (s.isLoading || !s.canLoadMore) return
        fetchPage(s.page + 1, append = true)
    }

    private fun fetchPage(page: Int, append: Boolean) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val s = _state.value
            val r = repo.getCustomers(page, s.search.ifBlank { null })
            _state.update { cur ->
                if (r.isSuccess) {
                    val resp = r.getOrNull()!!
                    val items = if (append) cur.items + resp.data else resp.data
                    val last = resp.meta?.lastPage ?: page
                    cur.copy(
                        isLoading = false,
                        items = items,
                        page = page,
                        lastPage = last,
                        canLoadMore = page < last,
                    )
                } else {
                    cur.copy(isLoading = false, error = r.exceptionOrNull()?.message)
                }
            }
        }
    }
}

data class CustomerDetailState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val customer: CustomerDto? = null,
)

@HiltViewModel
class StakeholderCustomerDetailViewModel @Inject constructor(
    private val repo: StakeholderRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(CustomerDetailState())
    val state: StateFlow<CustomerDetailState> = _state.asStateFlow()

    fun load(id: Int) {
        _state.update { CustomerDetailState(isLoading = true) }
        viewModelScope.launch {
            val r = repo.getCustomer(id)
            _state.update {
                if (r.isSuccess) it.copy(isLoading = false, customer = r.getOrNull())
                else it.copy(isLoading = false, error = r.exceptionOrNull()?.message)
            }
        }
    }
}
