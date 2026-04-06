package com.mbp.app.data.repository

import com.mbp.app.data.api.ApiService
import com.mbp.app.data.local.TokenManager
import com.mbp.app.data.model.CustomerDto
import com.mbp.app.data.model.TowerUnitDto
import com.mbp.app.data.model.VillaDto
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager,
) {
    @Volatile
    var cachedCustomer: CustomerDto? = null
        private set

    fun setCachedCustomer(customer: CustomerDto?) {
        cachedCustomer = customer
    }

    suspend fun refreshCustomerData(): Result<CustomerDto> = try {
        val id = tokenManager.getCustomerId()
            ?: return Result.failure(IllegalStateException("No customer id"))
        val resp = apiService.customer(id)
        cachedCustomer = resp.data
        Result.success(resp.data)
    } catch (e: Exception) {
        Result.failure(Exception(extractError(e), e))
    }

    suspend fun getVillaDetail(id: Int): Result<VillaDto> = try {
        Result.success(apiService.villa(id).data)
    } catch (e: Exception) {
        Result.failure(Exception(extractError(e), e))
    }

    suspend fun getTowerUnitDetail(id: Int): Result<TowerUnitDto> = try {
        Result.success(apiService.towerUnit(id).data)
    } catch (e: Exception) {
        Result.failure(Exception(extractError(e), e))
    }

    private fun extractError(e: Exception): String {
        if (e is HttpException) return "Request failed (${e.code()})"
        return e.message ?: "Unknown error"
    }
}
