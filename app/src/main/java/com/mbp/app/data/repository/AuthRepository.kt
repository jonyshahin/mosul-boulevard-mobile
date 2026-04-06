package com.mbp.app.data.repository

import com.mbp.app.data.api.ApiService
import com.mbp.app.data.local.TokenManager
import com.mbp.app.data.model.LoginRequest
import com.mbp.app.data.model.LoginResponse
import com.mbp.app.data.model.PhoneLoginRequest
import com.mbp.app.data.model.PhoneLoginResponse
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager,
    private val customerRepository: CustomerRepository,
) {
    suspend fun loginWithEmail(email: String, password: String): Result<LoginResponse> = try {
        val resp = apiService.login(LoginRequest(email, password))
        tokenManager.saveToken(resp.token)
        tokenManager.saveUserRole("admin")
        Result.success(resp)
    } catch (e: Exception) {
        Result.failure(Exception(extractError(e), e))
    }

    suspend fun loginWithPhone(phone: String): Result<PhoneLoginResponse> = try {
        val resp = apiService.phoneLogin(PhoneLoginRequest(phone))
        tokenManager.saveToken(resp.token)
        tokenManager.saveUserRole("customer")
        tokenManager.saveCustomerId(resp.customer.id)
        customerRepository.setCachedCustomer(resp.customer)
        Result.success(resp)
    } catch (e: Exception) {
        Result.failure(Exception(extractError(e), e))
    }

    suspend fun logout() {
        runCatching { apiService.logout() }
        customerRepository.setCachedCustomer(null)
        tokenManager.clearToken()
    }

    suspend fun isLoggedIn(): Boolean = !tokenManager.getToken().isNullOrBlank()

    suspend fun getUserRole(): String? = tokenManager.getUserRole()

    private fun extractError(e: Exception): String {
        if (e is HttpException) {
            val body = runCatching { e.response()?.errorBody()?.string() }.getOrNull()
            if (!body.isNullOrBlank()) {
                Regex("\"message\"\\s*:\\s*\"([^\"]+)\"").find(body)?.groupValues?.get(1)
                    ?.let { return it }
            }
            return "Request failed (${e.code()})"
        }
        return e.message ?: "Unknown error"
    }
}
