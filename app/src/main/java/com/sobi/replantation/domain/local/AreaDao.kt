package com.sobi.replantation.domain.local

import androidx.room.*

@Dao
interface AreaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addArea(area: AreaData)

    @Query("SELECT * FROM areas WHERE _id = :id AND member_id = :memberId")
    fun getArea(memberId: Int, id: Int): AreaData?

    @Query("SELECT assignments.name FROM areas LEFT JOIN assignments ON assignments._id = areas._id WHERE areas._id = :area_id")
    fun getNameArea(area_id: Int): String


    @Query("SELECT * FROM areas WHERE koperasi_id= :koperasiId and member_id = :memberId ORDER BY area_name")
    fun getAreas(memberId: Int, koperasiId: Int): List<AreaData>


    @Query("DELETE FROM areas")
    fun deleteAreas()


    @Update
    fun updateArea(area: AreaData)
}