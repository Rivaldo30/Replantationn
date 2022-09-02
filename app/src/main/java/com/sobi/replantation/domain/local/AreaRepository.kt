package com.sobi.replantation.domain.local

import com.sobi.replantation.domain.model.Area

interface AreaRepository {
    suspend fun getArea(memberId: Int, id: Int): Area?
    suspend fun getAreas(koperasiId: Int, memberId: Int): List<Area>
    suspend fun saveAreas(areas: List<Area>)
    suspend fun saveArea(area: Area)
    suspend fun updateArea(area: Area)
    suspend fun deleteAreas()
    suspend fun getAreaName(area_id:Int): String
}

class RoomAreaRepository(sobiDatabase: SobiDatabase) : AreaRepository {
    private val areaDao = sobiDatabase.areaDao()
    override suspend fun getArea(memberId: Int, id: Int): Area? {
        val result = areaDao.getArea(memberId, id)
        return if (result == null) null else areaFrom(result)
    }

    override suspend fun getAreas(koperasiId: Int, memberId: Int): List<Area> {
        val result = areaDao.getAreas(memberId,koperasiId)
        return result.map {
            areaFrom(it)
        }
    }

    override suspend fun saveAreas(areas: List<Area>) =
        areas.forEach { saveArea(it) }

    override suspend fun updateArea(area: Area) =
            areaDao.updateArea(areaDataFrom(area))

    override suspend fun saveArea(area: Area) =
        areaDao.addArea(areaDataFrom(area))

    override suspend fun deleteAreas() {
        areaDao.deleteAreas()
    }



    override suspend fun getAreaName(area_id: Int): String =
            areaDao.getNameArea(area_id)



}