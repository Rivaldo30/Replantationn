package com.sobi.replantation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sobi.replantation.domain.PreferenceService
import com.sobi.replantation.domain.interactor.AreaInteractor
import com.sobi.replantation.domain.interactor.TokenProvider
import com.sobi.replantation.domain.local.AreaRepository
import com.sobi.replantation.domain.model.Area
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class AreaDetailViewModel @Inject constructor(
    val preferenceService: PreferenceService,
    val tokenProvider: TokenProvider,
    areaRepository: AreaRepository
) : ViewModel() {

    private val areaInteractor = AreaInteractor(preferenceService, areaRepository)

    val areaData = MutableLiveData<Area>()

    suspend fun loadDetail(memberId: Int, areaId: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            val result = async(Dispatchers.IO) { areaInteractor.getArea(memberId, areaId) }
            areaData.value = result.await()
        }
    }


}

