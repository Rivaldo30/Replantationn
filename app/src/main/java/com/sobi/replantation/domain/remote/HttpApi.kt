package com.sobi.replantation.domain.remote

import com.sobi.replantation.domain.model.*
import retrofit2.Call
import retrofit2.http.*

interface UserApi {
    @POST("v2/user/login")
    fun login(@Header("Authorization") auth: String, @Header("Sobi-Date") sobiDate: String, @Body request: LoginRequest): Call<LoginResponse>
    @POST("v2/user/login_pendataan")
    fun uniqueLogin(@Header("Authorization") auth: String, @Header("Sobi-Date") sobiDate: String, @Body request: UniqueLoginRequest): Call<LoginResponse>
    @POST("v2/user/inventLogout")
    fun logout(@Header("Authorization") auth: String, @Header("Sobi-Date") sobiDate: String,  @Query("user_access_id") accessId: Int) : Call<LogoutResponse>
}

interface TokenApi {
    @GET("token/generator")
    fun getToken(): Call<TokenResponse>
}

interface MemberApi {
    @GET("v2/replantation/get_list_pembibitan")
    fun getAssignmentMembers(
        @Header("Authorization") auth: String,
        @Header("Sobi-Date") sobiDate: String,
        @Query("koperasi_id") koperasiId: Int
    ): Call<BulkDataResponse>

    @POST("/v2/replantation/upload_serah_terima")
    fun uploadSerahTeima(
        @Header("Authorization") auth: String,
        @Header("Sobi-Date") sobiDate: String,
        @Body request: UniqueLoginRequest
    ) : Call<Void>

}