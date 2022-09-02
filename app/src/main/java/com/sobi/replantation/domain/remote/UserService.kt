package com.sobi.replantation.domain.model

import android.util.Log
import com.sobi.replantation.domain.model.*
import com.appsobi.pendataan.util.md5
import com.sobi.replantation.domain.remote.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface UserService {
    suspend fun login(auth: String, sobiDate: String, username: String, password: String, roleType: Int): LoginResponse
    suspend fun uniqueLogin(auth: String, sobiDate: String, username: String, password: String, roleType: Int,primaryDeviceId: String): LoginResponse
    suspend fun logout(auth: String, sobiDate: String, accessId: Int): LogoutResponse

}

class HttpUserService(val userApi: UserApi) : UserService {
    override suspend fun login(
            auth: String,
            sobiDate: String,
            username: String,
            password: String,
            roleType: Int
    ): LoginResponse = suspendCoroutine { continuation ->
        userApi.login(
                auth, sobiDate,
                LoginRequest(
                        LoginRequestData(
                                username,
                                password.md5(),
                                roleType
                        )
                )
        ).enqueue(
                object : Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        continuation.resumeWithException(t)
                    }

                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.isSuccessful) {
                            val result = response.body()
                            if (result == null) {
                                continuation.resumeWithException(Exception("no result from login"))
                            } else continuation.resume(result)
                        } else {

                            val error = if (response.code() == 404) LoginFailedExeption()
                            else Exception("failed to call login, response code:${response.code()}")
                            continuation.resumeWithException(error)
                        }
                    }

                }
        )
    }

    override suspend fun uniqueLogin(auth: String, sobiDate: String,  username: String, password: String, roleType: Int, primaryDeviceId: String): LoginResponse = suspendCoroutine { continuation ->
        userApi.uniqueLogin(auth, sobiDate, UniqueLoginRequest(
            UniqueLoginRequestData(
                username,
                password.md5(),
                roleType,
                primaryDeviceId
            )
        )).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result == null) {
                        continuation.resumeWithException(Exception("no result from login"))
                    } else continuation.resume(result)
                } else {

                    val error = if (response.code() == 404) LoginFailedExeption()
                    else Exception("failed to call login, response code:${response.code()}")
                    continuation.resumeWithException(error)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                continuation.resumeWithException(t)
            }

        })
    }


    override suspend fun logout(auth: String, sobiDate: String, accessId: Int): LogoutResponse = suspendCoroutine { continuation ->
        userApi.logout(auth, sobiDate, accessId).enqueue(object : Callback<LogoutResponse>{
            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                continuation.resumeWithException(t)
            }

            override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result == null) {
                        continuation.resumeWithException(Exception("no result from login"))
                    } else {
                        Log.d("hasil","logout = $result")
                        continuation.resume(result)
                    }
                } else {
                    Log.d("error","login = ${response.code()}")
                    val error = if (response.code() == 404) LoginFailedExeption()
                    else Exception("failed to call login, response code:${response.code()}")
                    continuation.resumeWithException(error)
                }
            }

        })
    }




}

class LoginFailedExeption : Exception()