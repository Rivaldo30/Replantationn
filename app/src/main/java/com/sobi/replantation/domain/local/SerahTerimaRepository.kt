package com.sobi.replantation.domain.local

import com.sobi.replantation.domain.model.SerahTerima

interface SerahTerimaRepository {
    suspend fun getSerahTerima(memberId: Int, id: Int): SerahTerima?
    suspend fun getSerahTerimas( memberId: Int): List<SerahTerima>
    suspend fun saveSerahTerima(SerahTerima: SerahTerima)
    suspend fun updateSerahTerima( SerahTerima: SerahTerima)
    suspend fun deleteSerahTerimas()

}

class RoomSerahTerimaRepository(sobiDatabase: SobiDatabase) : SerahTerimaRepository {
    private val serahTerimaDao = sobiDatabase.serahTerimaDao()

    override suspend fun getSerahTerima(memberId: Int, id: Int): SerahTerima? {
        val result = serahTerimaDao.getserahTerima(memberId, id)
        return if (result == null) null else serahTerimaFrom(result)
    }

    override suspend fun getSerahTerimas(memberId: Int): List<SerahTerima> {
        val result = serahTerimaDao.getserahTerimas(memberId)
        return result.map {
            serahTerimaFrom(it)
        }
    }


    override suspend fun updateSerahTerima(serahTerima: SerahTerima) =
            serahTerimaDao.updateserahTerima(serahTerimaDataFrom(serahTerima))

    override suspend fun saveSerahTerima(serahTerima: SerahTerima) =
        serahTerimaDao.addserahTerima(serahTerimaDataFrom(serahTerima))

    override suspend fun deleteSerahTerimas() {
        serahTerimaDao.deleteserahTerimas()
    }



}