package com.sobi.replantation.domain.interactor

import com.sobi.replantation.domain.PreferenceService
import com.sobi.replantation.domain.local.AreaRepository
import com.sobi.replantation.domain.model.Area
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class AreaInteractor(
    val preferenceService: PreferenceService,
    val areaRepository: AreaRepository
) {

    suspend fun getAreas(memberId: Int) : List<Area> {
        val koperasiId = preferenceService.koperasiId
        var areas = listOf<Area>()
        runBlocking {
            val result = GlobalScope.async(Dispatchers.IO){
                areaRepository.getAreas(koperasiId, memberId)
            }.await()

            areas = result
        }
        return areas
    }

    suspend fun getArea(memberId: Int, areaId: Int):Area? = areaRepository.getArea(memberId, areaId)

}