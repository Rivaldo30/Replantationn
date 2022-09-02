package com.sobi.replantation.domain.local

import com.sobi.replantation.domain.model.BuktiSerahTerima
import com.sobi.replantation.domain.model.SerahTerima

interface BuktiSerahTerimaRepository {
    suspend fun getSerahTerima(memberId: Int, id: Int): BuktiSerahTerima?
    suspend fun getSerahTerimas( memberId: Int): List<BuktiSerahTerima>
    suspend fun saveSerahTerima(SerahTerima: BuktiSerahTerima)
    suspend fun updateSerahTerima(koperasiId: Int, buktiSerahTerima: BuktiSerahTerima)
    suspend fun deleteSerahTerimas()

}

class RoomBuktiSerahTerimaRepository(sobiDatabase: SobiDatabase) : BuktiSerahTerimaRepository {
    private val buktiSerahTerimaDao = sobiDatabase.buktiSerahTerimaDao()

    override suspend fun getSerahTerima(memberId: Int, id: Int): BuktiSerahTerima? {
        val result = buktiSerahTerimaDao.getserahTerima(memberId, id)
        return if (result == null) null else buktiSerahTerimaFrom(result)
    }

    override suspend fun getSerahTerimas(memberId: Int): List<BuktiSerahTerima> {
        val result = buktiSerahTerimaDao.getserahTerimas(memberId)
        return result.map {
            buktiSerahTerimaFrom(it)
        }
    }

    override suspend fun updateSerahTerima(koperasiId: Int, serahTerima: BuktiSerahTerima) =
        buktiSerahTerimaDao.updateserahTerima(buktiSerahTerimaDataFrom(serahTerima))

    override suspend fun saveSerahTerima(serahTerima: BuktiSerahTerima) =
        buktiSerahTerimaDao.addserahTerima(buktiSerahTerimaDataFrom(serahTerima))

    override suspend fun deleteSerahTerimas() {
        buktiSerahTerimaDao.deleteserahTerimas()
    }



}