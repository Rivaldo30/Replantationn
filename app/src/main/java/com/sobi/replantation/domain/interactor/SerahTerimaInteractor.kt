package com.sobi.replantation.domain.interactor

import com.sobi.replantation.domain.PreferenceService
import com.sobi.replantation.domain.local.SerahTerimaRepository

import com.sobi.replantation.domain.model.SerahTerima
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class SerahTerimaInteractor(
    val preferenceService: PreferenceService,
    val serahTerimaRepository: SerahTerimaRepository
) {

    suspend fun getSerahTerimas(memberId: Int) : List<SerahTerima> {
        val koperasiId = preferenceService.koperasiId
        var SerahTerimas = listOf<SerahTerima>()
        runBlocking {
            val result = GlobalScope.async(Dispatchers.IO){
                serahTerimaRepository.getSerahTerimas(memberId)
            }.await()

            SerahTerimas = result
        }
        return SerahTerimas
    }

    suspend fun updateSerahTerima(serahTerima: SerahTerima) = serahTerimaRepository.updateSerahTerima(serahTerima)
    suspend fun saveSerahTerima(serahTerima: SerahTerima) = serahTerimaRepository.saveSerahTerima(serahTerima)

    suspend fun getSerahTerima(memberId: Int, id: Int):SerahTerima? = serahTerimaRepository.getSerahTerima(memberId, id)

}