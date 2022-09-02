package com.sobi.replantation.domain.interactor

import com.sobi.replantation.domain.local.*
import com.sobi.replantation.domain.model.Assignment
import com.sobi.replantation.domain.remote.MemberService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking


class AssignmentInteractor(val assignmentRepository: AssignmentRepository,
                           val areaRepository: AreaRepository
) {


    suspend fun getAssignments(koperasiId: Int): List<Assignment> {
       var data = listOf<Assignment>()
       runBlocking {
           val result = GlobalScope.async(Dispatchers.IO){
               val tmp = assignmentRepository.getAssignments(koperasiId)
               tmp.map {
                   it.copy(totalTerima = assignmentRepository.getTotalTerima(it.memberId))
               }
           }
           data = result.await()
       }
        return data
    }

    suspend fun searchAssignments(accessId: Int, query: String) : List<Assignment> {
        var load = listOf<Assignment>()
        runBlocking {
            val result = GlobalScope.async(Dispatchers.IO) {
                val search = assignmentRepository.searchAssignments(query)
                search.map {
                    it.copy(totalTerima = assignmentRepository.getTotalTerima(it.memberId) )

                }
            }

            load = result.await()
        }
        return load
    }

    suspend fun getAssignment(memberId: Int, koperasiId: Int) : Assignment? =
        assignmentRepository.getAssignment(memberId, koperasiId)

    suspend fun deleteAssignment(){
        return assignmentRepository.deleteAllData()
    }

    suspend fun getTotalTerima(memberId: Int): Int? =
        assignmentRepository.getTotalTerima(memberId)

    suspend fun updateAssignment(assignment: Assignment) =
        assignmentRepository.updateAssignment(assignment)





}