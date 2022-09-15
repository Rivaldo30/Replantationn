package com.sobi.replantation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sobi.replantation.domain.PreferenceService
import com.sobi.replantation.domain.interactor.*
import com.sobi.replantation.domain.local.AreaRepository
import com.sobi.replantation.domain.local.AssignmentRepository
import com.sobi.replantation.domain.local.BuktiSerahTerimaRepository
import com.sobi.replantation.domain.local.SerahTerimaRepository
import com.sobi.replantation.domain.model.Area
import com.sobi.replantation.domain.model.Assignment
import com.sobi.replantation.domain.model.BuktiSerahTerima
import com.sobi.replantation.domain.model.SerahTerima
import kotlinx.coroutines.*
import javax.inject.Inject

class TaskDetailViewModel @Inject constructor(
    val preferenceService: PreferenceService,
    tokenProvider: TokenProvider,
    areaRepository: AreaRepository,
    buktiSerahTerimaRepository: BuktiSerahTerimaRepository,
    serahTerimaRepository: SerahTerimaRepository,
    assignmentRepository: AssignmentRepository
) : ViewModel() {

    private val assignmentInteractor = AssignmentInteractor(assignmentRepository, areaRepository)
    private val areaInteractor = AreaInteractor(preferenceService, areaRepository)
    private val serahTerimaInteractor =
        SerahTerimaInteractor(preferenceService, serahTerimaRepository)
    private val buktiSerahTerimaInteractor =
        BuktiSerahTerimaInteractor(preferenceService, buktiSerahTerimaRepository)

    val buktiDokumenData = MutableLiveData<List<BuktiSerahTerima>>()
    val assignmentData = MutableLiveData<Assignment>()
    val areaData = MutableLiveData<List<Area>>()
    val totalTerima = MutableLiveData<Int>()

    suspend fun loadDetail(memberId: Int) {
        val koperasiId = preferenceService.koperasiId
        GlobalScope.launch(Dispatchers.Main) {
            val result = async(Dispatchers.IO) {
                assignmentInteractor.getAssignment(memberId, koperasiId)
            }.await()
            assignmentData.value = result
            val areaDeffered = async(Dispatchers.IO) {
                areaInteractor.getAreas(memberId)
            }.await()
            areaData.value = areaDeffered
            Log.d("data", "area $areaDeffered")

        }
        GlobalScope.launch(Dispatchers.Main) {
            val photo = async(Dispatchers.IO) {
                buktiSerahTerimaInteractor.getSerahTerimas(memberId)
            }.await()
            buktiDokumenData.value = photo
        }
        val foto = GlobalScope.async(Dispatchers.IO) {
            assignmentInteractor.getTotalTerima(memberId)
        }.await()
        GlobalScope.launch(Dispatchers.Main) {
            totalTerima.value = foto
        }
    }



    suspend fun saveSerahTerima(
        memberId: Int,
        speciesId: Int,
        jumlahBibit: Int,
        keterangan: String?,
        date: String
    ) {
        val accesId = preferenceService.accessId
        runBlocking(Dispatchers.IO) {
            serahTerimaInteractor.saveSerahTerima(
                SerahTerima(
                    0,
                    accesId,
                    memberId,
                    speciesId,
                    jumlahBibit,
                    keterangan,
                    date
                )
            )
        }
        GlobalScope.launch(Dispatchers.Main){
            loadDetail(memberId)
        }
    }

    suspend fun updateFinishedSerahTerima(update: Int, memberId: Int) {
        val data = assignmentData.value ?: return
        runBlocking(Dispatchers.IO) {
            assignmentInteractor.updateAssignment(data.copy(finishedSerahTerima = update))
        }
        GlobalScope.launch(Dispatchers.Main){
            loadDetail(memberId)
        }
    }

    suspend fun saveImage(memberId: Int){
        val accessId = preferenceService.accessId
        GlobalScope.async(Dispatchers.IO){
            buktiSerahTerimaInteractor.saveSerahTerima(BuktiSerahTerima(0,accessId,memberId, "ffdjjdj","25-08-2022",1  ))
        }
        GlobalScope.launch(Dispatchers.Main){
            loadDetail(memberId)
        }
    }


}