package com.sobi.replantation.domain.interactor

import com.sobi.replantation.domain.PreferenceService
import com.sobi.replantation.domain.model.TokenData
import com.sobi.replantation.domain.model.TokenResponse
import com.sobi.replantation.domain.remote.TokenApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface TokenProvider {
    suspend fun getTokenData() : TokenData?
}

class LocaTokenProvider : TokenProvider {
    lateinit var preferenceService: PreferenceService
    lateinit var tokenApi: TokenApi
    override suspend fun getTokenData(): TokenData? = if (preferenceService.token==null) {
        val data = suspendCoroutine<TokenData?> { continuation ->
            tokenApi.getToken().enqueue(object : Callback<TokenResponse>{
                override fun onResponse(
                    call: Call<TokenResponse>,
                    response: Response<TokenResponse>
                ) {
                    if (response.isSuccessful) {
                        continuation.resume(response.body()?.data)
                    } else {
                        val error = Exception("failed to generate token, response code ${response.code()}")
                        continuation.resumeWithException(error)
                    }
                }

                override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

            })
        }

        preferenceService.token = data
        data
    } else preferenceService.token
}