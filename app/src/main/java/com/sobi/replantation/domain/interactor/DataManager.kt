package com.sobi.replantation.domain.interactor

import android.util.Log

import com.sobi.replantation.domain.local.AreaRepository
import com.sobi.replantation.domain.local.AssignmentRepository
import com.sobi.replantation.domain.model.Area
import com.sobi.replantation.domain.model.Assignment
import com.sobi.replantation.domain.remote.MemberService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class DataManager(val memberService: MemberService,
                  val assignmentRepository: AssignmentRepository,
                  val areaRepository: AreaRepository,
) {
    suspend fun load(auth: String, sobiDate: Int, accessId: Int) {
        val bulkData = GlobalScope.async(Dispatchers.IO) {
            memberService.getAllData(auth, sobiDate.toString(), accessId)
        }.await()

        Log.d("data","all = $bulkData")
        bulkData.forEach { member ->
            assignmentRepository.saveAssignment(
                Assignment(
                    member.id,
                    member.memberId,
                    member.koperasiId,
                    member.memberName,
                    member.memberNo,
                    member.photo,
                    member.idPhoto,
                    member.statementPhoto,
                    member.membershipPhoto,
                    member.lahanCount,
                    member.jumlahBibit,


                )
            )
            member.areas.forEach { area ->
                areaRepository.saveArea(
                    Area(
                        area.memberId,
                        area.id,
                        area.areaName,
                        area.certificateNumber,
                        area.koperasiId,
                        area.areaMeasure,
                        area.jumlahTebang,
                        area.photoSketsa,
                        area.photoSertif,
                        area.pohon

                    )
                )

            }
        }

    }

}