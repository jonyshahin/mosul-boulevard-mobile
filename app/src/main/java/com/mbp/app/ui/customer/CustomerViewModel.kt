package com.mbp.app.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbp.app.data.model.CustomerDto
import com.mbp.app.data.model.TowerUnitDto
import com.mbp.app.data.model.VillaDto
import com.mbp.app.data.repository.AuthRepository
import com.mbp.app.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CustomerHomeState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val customer: CustomerDto? = null,
)

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CustomerHomeState())
    val state: StateFlow<CustomerHomeState> = _state.asStateFlow()

    init {
        val cached = customerRepository.cachedCustomer
        if (cached != null) {
            _state.update { it.copy(customer = cached) }
        } else {
            refresh()
        }
    }

    fun refresh() {
        _state.update { it.copy(isRefreshing = true, error = null) }
        viewModelScope.launch {
            val r = customerRepository.refreshCustomerData()
            _state.update {
                if (r.isSuccess) {
                    it.copy(isRefreshing = false, customer = r.getOrNull())
                } else {
                    it.copy(isRefreshing = false, error = r.exceptionOrNull()?.message)
                }
            }
        }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onDone()
        }
    }
}

data class UnitDetailState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val villa: VillaDto? = null,
    val towerUnit: TowerUnitDto? = null,
)

@HiltViewModel
class UnitDetailViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(UnitDetailState())
    val state: StateFlow<UnitDetailState> = _state.asStateFlow()

    fun load(unitId: Int, unitType: String) {
        _state.update { UnitDetailState(isLoading = true) }
        viewModelScope.launch {
            if (unitType == "villa") {
                val r = customerRepository.getVillaDetail(unitId)
                _state.update {
                    if (r.isSuccess) it.copy(isLoading = false, villa = r.getOrNull())
                    else it.copy(isLoading = false, error = r.exceptionOrNull()?.message)
                }
            } else {
                val r = customerRepository.getTowerUnitDetail(unitId)
                _state.update {
                    if (r.isSuccess) it.copy(isLoading = false, towerUnit = r.getOrNull())
                    else it.copy(isLoading = false, error = r.exceptionOrNull()?.message)
                }
            }
        }
    }
}
