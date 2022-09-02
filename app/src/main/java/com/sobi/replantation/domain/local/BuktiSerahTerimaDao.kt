package com.sobi.replantation.domain.local

import androidx.room.*

@Dao
interface BuktiSerahTerimaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addserahTerima(serahTerima: BuktiSerahTerimaData)

    @Query("SELECT * FROM bukti_serah_terima WHERE _id = :id AND member_id = :memberId")
    fun getserahTerima(memberId: Int, id: Int): BuktiSerahTerimaData?


    @Query("SELECT * FROM bukti_serah_terima WHERE member_id = :memberId ORDER BY _id asc")
    fun getserahTerimas(memberId: Int): List<BuktiSerahTerimaData>


    @Query("DELETE FROM bukti_serah_terima")
    fun deleteserahTerimas()

    @Update
    fun updateserahTerima(serahTerima: BuktiSerahTerimaData)
}