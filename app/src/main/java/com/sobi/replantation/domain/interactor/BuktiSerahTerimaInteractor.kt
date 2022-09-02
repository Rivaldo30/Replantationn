package com.sobi.replantation.domain.interactor

import com.sobi.replantation.domain.PreferenceService
import com.sobi.replantation.domain.local.BuktiSerahTerimaRepository
import com.sobi.replantation.domain.local.SerahTerimaRepository
import com.sobi.replantation.domain.model.BuktiSerahTerima

import com.sobi.replantation.domain.model.SerahTerima
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class BuktiSerahTerimaInteractor(
    val preferenceService: PreferenceService,
    val serahTerimaRepository: BuktiSerahTerimaRepository
) {

    suspend fun getSerahTerimas(memberId: Int) : List<BuktiSerahTerima> {
        val koperasiId = preferenceService.koperasiId
        var SerahTerimas = listOf<BuktiSerahTerima>()
        runBlocking {
            val result = GlobalScope.async(Dispatchers.IO){
                serahTerimaRepository.getSerahTerimas(memberId)
            }.await()

            SerahTerimas = result
        }
        return SerahTerimas
    }

    suspend fun saveSerahTerima(serahTerima: BuktiSerahTerima) = serahTerimaRepository.saveSerahTerima(serahTerima)

    suspend fun getSerahTerima(memberId: Int, id: Int):BuktiSerahTerima? = serahTerimaRepository.getSerahTerima(memberId, id)

}