package com.sobi.replantation.domain.local

import com.sobi.replantation.domain.local.AssignmentData
import com.sobi.replantation.domain.local.SobiDatabase
import com.sobi.replantation.domain.model.Assignment

interface AssignmentRepository {
    suspend fun getAssignment(memberId: Int, koperasiId: Int): Assignment?
    suspend fun getAssignments(koperasiId: Int): List<Assignment>
    suspend fun searchAssignments(query: String): List<Assignment>
    suspend fun saveAssignments(assignments: List<Assignment>)
    suspend fun deleteAllData()
    suspend fun updateAssignment(assignment: Assignment)
    suspend fun saveAssignment(assignment: Assignment)
    suspend fun getTotalTerima(memberId: Int) : Int
}

class RoomAssignmentRepository(sobiDatabase: SobiDatabase) : AssignmentRepository {
    private val assignmentDao = sobiDatabase.assignmentDao()

    override suspend fun getAssignment(memberId: Int, koperasiId: Int): Assignment? {
        val result = assignmentDao.getAssignment(memberId, koperasiId)
        return if (result == null) null else Assignment(
            result.id,
            result.memberId,
            result.koperasiId,
            result.name,
            result.memberNo,
            result.photoUrl,
            result.idPhotoUrl,
            result.statementPhotoUrl,
            result.membershipPhotoUrl,
            result.areaCount,
            result.bibitCount,
            result.finishSerahTerima
        )
    }

    override suspend fun getAssignments(koperasiId: Int): List<Assignment> {
        val result = assignmentDao.getAssignments(koperasiId)
        return result.map {
            Assignment(
                it.id,
                it.memberId,
                it.koperasiId,
                it.name,
                it.memberNo,
                it.photoUrl,
                it.idPhotoUrl,
                it.statementPhotoUrl,
                it.membershipPhotoUrl,
                it.areaCount,
                it.bibitCount,
                it.finishSerahTerima
            )
        }
    }

    override suspend fun searchAssignments(query: String): List<Assignment> {
        val result = assignmentDao.searchAssignments("%$query%")
        return result.map {
            Assignment(
                it.id,
                it.memberId,
                it.koperasiId,
                it.name,
                it.memberNo,
                it.photoUrl,
                it.idPhotoUrl,
                it.statementPhotoUrl,
                it.membershipPhotoUrl,
                it.areaCount,
                it.bibitCount,
                it.finishSerahTerima
            )
        }
    }

    override suspend fun saveAssignments(assignments: List<Assignment>) {
        assignments.forEach {
            saveAssignment(it)
        }
    }

    override suspend fun deleteAllData() {
        assignmentDao.deletaAll()
    }

    override suspend fun updateAssignment(assignment: Assignment) =
        assignmentDao.updateAssignment(assignmentDataFrom(assignment))

    override suspend fun saveAssignment(assignment: Assignment) {
        assignmentDao.addAssignment(
            assignmentDataFrom(assignment)
        )
    }

    override suspend fun getTotalTerima(memberId: Int): Int =
        assignmentDao.getTotalSudahTerima(memberId)

}