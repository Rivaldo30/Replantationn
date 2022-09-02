package com.sobi.replantation.domain.local

import androidx.room.*

@Dao
interface SerahTerimaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addserahTerima(serahTerima: SerahTerimaData)

    @Query("SELECT * FROM serah_terima WHERE _id = :id AND member_id = :memberId")
    fun getserahTerima(memberId: Int, id: Int): SerahTerimaData?


    @Query("SELECT * FROM serah_terima WHERE member_id = :memberId ORDER BY _id asc")
    fun getserahTerimas(memberId: Int): List<SerahTerimaData>


    @Query("DELETE FROM serah_terima")
    fun deleteserahTerimas()

    @Update
    fun updateserahTerima(serahTerima: SerahTerimaData)
}