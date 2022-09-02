package com.sobi.replantation.viewmodel


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sobi.replantation.domain.PreferenceService
import com.sobi.replantation.domain.interactor.AccountInteractor
import com.sobi.replantation.domain.interactor.AssignmentInteractor
import com.sobi.replantation.domain.interactor.DataManager
import com.sobi.replantation.domain.interactor.TokenProvider
import com.sobi.replantation.domain.local.AreaRepository
import com.sobi.replantation.domain.local.AssignmentRepository
import com.sobi.replantation.domain.model.Assignment
import com.sobi.replantation.domain.model.UserService
import com.sobi.replantation.domain.remote.BaseUrlLoader
import com.sobi.replantation.domain.remote.MemberService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskListViewModel @Inject constructor(val baseUrlLoader: BaseUrlLoader,
                                            val preferenceService: PreferenceService,
                                            memberService: MemberService,
                                            val userService: UserService,
                                            val tokenProvider: TokenProvider,
                                            val areaRepository: AreaRepository,
                                            assignmentRepository: AssignmentRepository,
) : ViewModel(){

    val assignments = MutableLiveData<List<Assignment>>()

    private val assignmentInteractor = AssignmentInteractor(assignmentRepository, areaRepository)
    private val accountInteractor = AccountInteractor(preferenceService, userService, tokenProvider)
    private val dataManager = DataManager(memberService, assignmentRepository, areaRepository)



    suspend fun refreshAssignments() {
        val koperasiId = preferenceService.koperasiId

        GlobalScope.async(Dispatchers.IO) {
            assignmentInteractor.deleteAssignment()
        }.await()

        GlobalScope.async(Dispatchers.IO) {
            areaRepository.deleteAreas()
        }.await()


        val token = tokenProvider.getTokenData() ?: return
        val result = GlobalScope.async(Dispatchers.IO) {
            dataManager.load(token.authorization, token.sobiDate, koperasiId)
            assignmentInteractor.getAssignments(koperasiId)
        }.await()

        GlobalScope.launch(Dispatchers.Main) { assignments.value = result }
    }
    suspend fun loadAssignments() {
        val koperasiId = preferenceService.koperasiId
        GlobalScope.launch(Dispatchers.Main) {
            val result = async(Dispatchers.IO) { assignmentInteractor.getAssignments(koperasiId) }
            assignments.value = result.await()
            Log.d("keluaran", "penugasan = ${assignments.value}")
        }
    }


    suspend fun searchAssignments(query: String?) {
        val koperasiId = preferenceService.koperasiId

        if (query == null || query.isEmpty())
            loadAssignments()
        else {
            GlobalScope.launch(Dispatchers.Main) {
                val result = async(Dispatchers.IO) { assignmentInteractor.searchAssignments(koperasiId, query) }
                assignments.value = result.await()
            }
        }
    }

    suspend fun deleteData(){
        GlobalScope.async(Dispatchers.IO) {
            assignmentInteractor.deleteAssignment()
        }.await()

        GlobalScope.async(Dispatchers.IO) {
            areaRepository.deleteAreas()
        }.await()


    }

}