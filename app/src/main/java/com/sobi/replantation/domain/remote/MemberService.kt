package com.sobi.replantation.domain.remote

import android.util.Log
import com.sobi.replantation.domain.model.*
import kotlinx.coroutines.coroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface MemberService {
    suspend fun getAllData(auth: String, sobiDate: String, koperasiId:Int): List<BulkMemberResponseItem>

}

class HttpMemberService(val memberApi: MemberApi) : MemberService {
    override suspend fun getAllData(auth: String, sobiDate: String, koperasiId: Int): List<BulkMemberResponseItem>  = suspendCoroutine { c ->
        memberApi.getAssignmentMembers(auth, sobiDate, koperasiId).enqueue(
            object : Callback<BulkDataResponse> {
                override fun onFailure(call: Call<BulkDataResponse>, t: Throwable) {
                    c.resumeWithException(t)
                }

                override fun onResponse(call: Call<BulkDataResponse>, response: Response<BulkDataResponse>) {
                    if (response.isSuccessful) {
                        c.resume(response.body()?.data ?: listOf())
                        Log.d("all","pendataan = ${response.body()}")
                    } else {
                        val error = Exception(
                            "failed retrieving bulk member data for koperasiId($koperasiId), " +
                                    "response code: ${response.code()} message: ${response.errorBody()}"
                        )
                        c.resumeWithException(error)
                    }
                }

            }
        )
    }



}


