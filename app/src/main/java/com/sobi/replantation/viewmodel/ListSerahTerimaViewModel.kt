package com.sobi.replantation.viewmodel

import android.provider.Settings
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sobi.replantation.domain.PreferenceService
import com.sobi.replantation.domain.interactor.AssignmentInteractor
import com.sobi.replantation.domain.interactor.SerahTerimaInteractor
import com.sobi.replantation.domain.interactor.TokenProvider
import com.sobi.replantation.domain.local.AreaRepository
import com.sobi.replantation.domain.local.AssignmentRepository
import com.sobi.replantation.domain.local.BuktiSerahTerimaRepository
import com.sobi.replantation.domain.local.SerahTerimaRepository
import com.sobi.replantation.domain.model.Assignment
import com.sobi.replantation.domain.model.SerahTerima
import com.sobi.replantation.domain.remote.MemberService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListSerahTerimaViewModel @Inject constructor(val preferenceService: PreferenceService,
                                                   serahTerimaRepository: SerahTerimaRepository

) : ViewModel() {

    private val serahTerimaInteractor =
        SerahTerimaInteractor(preferenceService, serahTerimaRepository)

    val serahTerimaData = MutableLiveData<List<SerahTerima>>()

    suspend fun loadAllSerahTerima(memberId: Int) {
        GlobalScope.launch(Dispatchers.Main){
            val result = async(Dispatchers.IO){
                serahTerimaInteractor.getSerahTerimas(memberId)
            }.await()
            serahTerimaData.value = result
        }
    }

    suspend fun loadDataDetailTerima(memberId: Int, id: Int) =
        GlobalScope.async(Dispatchers.IO){
                serahTerimaInteractor.getSerahTerima(memberId, id)
            }.await()



    suspend fun updateData(serahTerima: SerahTerima, memberId: Int){
        GlobalScope.async(Dispatchers.IO){
            serahTerimaInteractor.updateSerahTerima(serahTerima)
        }.await()
        GlobalScope.launch(Dispatchers.Main) {
            loadAllSerahTerima(memberId)
        }
    }


}