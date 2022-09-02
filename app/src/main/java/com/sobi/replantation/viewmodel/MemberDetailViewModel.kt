package com.sobi.replantation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sobi.replantation.domain.PreferenceService
import com.sobi.replantation.domain.interactor.AssignmentInteractor
import com.sobi.replantation.domain.interactor.TokenProvider
import com.sobi.replantation.domain.local.AreaRepository
import com.sobi.replantation.domain.local.AssignmentRepository
import com.sobi.replantation.domain.model.Assignment
import com.sobi.replantation.domain.remote.MemberService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class MemberDetailViewModel @Inject constructor(assignmentRepository: AssignmentRepository,
                                                areaRepository: AreaRepository,
                                                val preferenceService: PreferenceService
): ViewModel() {
    private val assignmentInteractor = AssignmentInteractor(
        assignmentRepository,
        areaRepository)

    val assignmentData = MutableLiveData<Assignment>()

    suspend fun loadDetail(memberId: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            val koperasiId = preferenceService.koperasiId
            val result =
                async(Dispatchers.IO) { assignmentInteractor.getAssignment(memberId, koperasiId) }
            assignmentData.value = result.await()

        }
        Log.d("detail", "anggota = ${assignmentData.value}")
    }

}