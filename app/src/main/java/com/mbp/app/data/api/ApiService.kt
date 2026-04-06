package com.mbp.app.data.api

import com.mbp.app.data.model.CustomerDetailResponse
import com.mbp.app.data.model.CustomerDto
import com.mbp.app.data.model.DashboardStatsResponse
import com.mbp.app.data.model.LoginRequest
import com.mbp.app.data.model.LoginResponse
import com.mbp.app.data.model.MessageResponse
import com.mbp.app.data.model.PaginatedResponse
import com.mbp.app.data.model.PhoneLoginRequest
import com.mbp.app.data.model.PhoneLoginResponse
import com.mbp.app.data.model.SalesSummaryResponse
import com.mbp.app.data.model.SetupListResponse
import com.mbp.app.data.model.StageDto
import com.mbp.app.data.model.StatusDto
import com.mbp.app.data.model.StatusReportResponse
import com.mbp.app.data.model.TowerUnitDetailResponse
import com.mbp.app.data.model.TowerUnitDto
import com.mbp.app.data.model.VillaDetailResponse
import com.mbp.app.data.model.VillaDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @POST("auth/phone-login")
    suspend fun phoneLogin(@Body body: PhoneLoginRequest): PhoneLoginResponse

    @POST("auth/logout")
    suspend fun logout(): MessageResponse

    @GET("reports/dashboard-stats")
    suspend fun dashboardStats(): DashboardStatsResponse

    @GET("villas")
    suspend fun villas(
        @Query("page") page: Int? = null,
        @Query("search") search: String? = null,
        @Query("villa_type_id") villaTypeId: Int? = null,
    ): PaginatedResponse<VillaDto>

    @GET("villas/{id}")
    suspend fun villa(@Path("id") id: Int): VillaDetailResponse

    @GET("tower-units")
    suspend fun towerUnits(
        @Query("page") page: Int? = null,
        @Query("search") search: String? = null,
        @Query("tower_definition_id") towerDefinitionId: Int? = null,
    ): PaginatedResponse<TowerUnitDto>

    @GET("tower-units/{id}")
    suspend fun towerUnit(@Path("id") id: Int): TowerUnitDetailResponse

    @GET("customers")
    suspend fun customers(
        @Query("page") page: Int? = null,
        @Query("search") search: String? = null,
    ): PaginatedResponse<CustomerDto>

    @GET("customers/{id}")
    suspend fun customer(@Path("id") id: Int): CustomerDetailResponse

    @GET("reports/sales-summary")
    suspend fun salesSummary(): SalesSummaryResponse

    @GET("reports/structural-status")
    suspend fun structuralStatus(): StatusReportResponse

    @GET("reports/finishing-status")
    suspend fun finishingStatus(): StatusReportResponse

    @GET("reports/facade-status")
    suspend fun facadeStatus(): StatusReportResponse

    @GET("setup/stages")
    suspend fun stages(): SetupListResponse<StageDto>

    @GET("setup/statuses")
    suspend fun statuses(): SetupListResponse<StatusDto>
}
