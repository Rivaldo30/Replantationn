package com.sobi.replantation.domain.local

import androidx.room.*

@Dao
interface AssignmentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAssignment(assignment: AssignmentData)

    @Query("SELECT * FROM assignments WHERE member_id = :memberId AND koperasi_id = :koperasiId")
    fun getAssignment(memberId: Int, koperasiId: Int): AssignmentData?

    @Query("SELECT * FROM assignments WHERE koperasi_id = :koperasiId")
    fun getAssignments(koperasiId: Int): List<AssignmentData>

    @Query("SELECT * FROM assignments WHERE member_no LIKE :query OR name LIKE :query")
    fun searchAssignments(query: String): List<AssignmentData>


//    @Query("SELECT COUNT(trees.uid) FROM trees LEFT JOIN areas ON areas._id = trees.area_id LEFT JOIN assignments ON assignments._id = areas.member_id WHERE assignments._id =:member_id AND trees.deleted=0")
//    fun getCount(member_id:Int): Int

    @Query("SELECT SUM(serah_terima.jumlah_bibit) FROM serah_terima LEFT JOIN assignments ON assignments.member_id = serah_terima.member_id WHERE assignments.member_id =:memberId")
    fun getTotalSudahTerima(memberId: Int) : Int

    @Query("delete from assignments")
    fun deletaAll()

    @Update
    fun updateAssignment(assignment: AssignmentData)
}