package com.sobi.replantation.domain.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import com.sobi.replantation.domain.model.*

//val gson = Gson()

@Entity(tableName = "assignments", primaryKeys = ["member_id"])
data class AssignmentData(
    @ColumnInfo(name = "_id")
    val id: Int,
    @ColumnInfo(name = "member_id")
    val memberId: Int,
    @ColumnInfo(name = "koperasi_id")
    val koperasiId: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "member_no")
    val memberNo: String,
    @ColumnInfo(name = "photo_url")
    val photoUrl: String?,
    @ColumnInfo(name = "id_photo_url")
    val idPhotoUrl: String?,
    @ColumnInfo(name = "statement_photo_url")
    val statementPhotoUrl: String?,
    @ColumnInfo(name = "membership_photo_url")
    val membershipPhotoUrl: String?,
    @ColumnInfo(name = "area_count")
    val areaCount: Int,
    @ColumnInfo(name = "total_bibit")
    val bibitCount: Int,
    @ColumnInfo(name = "finish_serah_terima")
    val finishSerahTerima : Int
)

fun assignmentDataFrom(assignment: Assignment):AssignmentData = AssignmentData(
    id = assignment.id,
    memberId = assignment.memberId,
    koperasiId = assignment.koperasiId,
    name = assignment.memberName,
    memberNo = assignment.memberNo,
    photoUrl = assignment.photoUrl,
    idPhotoUrl = assignment.idPhotoUrl,
    statementPhotoUrl = assignment.statementPhotoUrl,
    membershipPhotoUrl = assignment.membershipPhoto,
    areaCount = assignment.areaCount,
    bibitCount = assignment.totalBibit,
    finishSerahTerima = assignment.finishedSerahTerima
)

fun assignmentFrom(data: AssignmentData):Assignment = Assignment(
    id = data.id,
    memberId = data.memberId,
    koperasiId = data.koperasiId,
    memberName = data.name,
    memberNo = data.memberNo,
    photoUrl = data.photoUrl,
    idPhotoUrl = data.idPhotoUrl,
    statementPhotoUrl = data.statementPhotoUrl,
    membershipPhoto = data.membershipPhotoUrl,
    areaCount = data.areaCount,
    totalBibit = data.bibitCount,
    finishedSerahTerima = data.finishSerahTerima
)


@Entity(tableName = "areas", primaryKeys = ["_id"])
data class AreaData(
    @ColumnInfo(name = "_id")
    val id: Int,
    @ColumnInfo(name = "member_id")
    val memberId: Int,
    @ColumnInfo(name = "area_name")
    val areaName: String,
    @ColumnInfo(name = "certificate_number")
    val certificateNumber: String,
    @ColumnInfo(name = "koperasi_id")
    val koperasiId: Int?,
    @ColumnInfo(name = "area_measure")
    val areaMeasure: String?,
    @ColumnInfo(name = "jumlah_tebang")
    val jumlahTebang: String?,
    @ColumnInfo(name = "foto_sertifikat")
    val photoSertifUrl: String?,
    @ColumnInfo(name = "foto_sketsa")
    val photoSketsaUrl: String?,
    @ColumnInfo(name = "tree")
    val tree: String?
)

fun areaDataFrom(area: Area): AreaData = AreaData(
    id = area.id,
    memberId = area.memberId,
    areaName = area.name,
    certificateNumber = area.certificateNumber,
    koperasiId = area.koperasiId,
    areaMeasure = area.areaMeasure,
    jumlahTebang = area.jumlahTebang,
    photoSertifUrl = area.photoSertifUrl,
    photoSketsaUrl = area.photoSketsaUrl,
    tree = area.tree
)

fun areaFrom(areaData: AreaData): Area = Area(
    id = areaData.id,
    memberId = areaData.memberId,
    name = areaData.areaName,
    koperasiId = areaData.koperasiId,
    certificateNumber = areaData.certificateNumber,
    areaMeasure = areaData.areaMeasure,
    jumlahTebang = areaData.jumlahTebang,
    photoSertifUrl = areaData.photoSertifUrl,
    photoSketsaUrl = areaData.photoSketsaUrl,
    tree = areaData.tree
)

@Entity(tableName = "bukti_serah_terima")
data class BuktiSerahTerimaData(
    @ColumnInfo(name = "_id")
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "access_id")
    val accessId: Int,
    @ColumnInfo(name = "member_id")
    val memberId: Int,
    @ColumnInfo(name = "photoPath")
    val photoPath : String,
    @ColumnInfo(name = "created_on")
    val createdOn : String?,
    @ColumnInfo(name = "status")
    val status: Int?
)

fun buktiSerahTerimaDataFrom(serahTerima: BuktiSerahTerima) : BuktiSerahTerimaData = BuktiSerahTerimaData(
    id = serahTerima.id,
    accessId = serahTerima.accessId,
    memberId = serahTerima.memberId,
    photoPath = serahTerima.photoUrl,
    createdOn = serahTerima.createdOn,
    status = serahTerima.status
)

fun buktiSerahTerimaFrom(serahTerimaData: BuktiSerahTerimaData) : BuktiSerahTerima = BuktiSerahTerima(
    id = serahTerimaData.id,
    accessId = serahTerimaData.accessId,
    memberId = serahTerimaData.memberId,
    photoUrl = serahTerimaData.photoPath,
    createdOn = serahTerimaData.createdOn,
    status = serahTerimaData.status
)

@Entity(tableName = "serah_terima")
data class SerahTerimaData(
    @ColumnInfo(name = "_id")
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "access_id")
    val accessId: Int,
    @ColumnInfo(name = "member_id")
    val memberId: Int,
    @ColumnInfo(name = "species_id")
    val speciesId: Int,
    @ColumnInfo(name = "jumlah_bibit")
    val jumlahBibit : Int,
    @ColumnInfo(name = "keterangan")
    val description : String?,
    @ColumnInfo(name = "created_on")
    val createdOn : String?,
    @ColumnInfo(name = "status")
    val status: Int?
    )

fun serahTerimaDataFrom(serahTerima: SerahTerima) : SerahTerimaData = SerahTerimaData(
    id = serahTerima.id,
    accessId = serahTerima.accessId,
    memberId = serahTerima.memberId,
    speciesId = serahTerima.speciesId,
    jumlahBibit = serahTerima.totalBibit,
    description = serahTerima.description,
    createdOn = serahTerima.createdOn,
    status = serahTerima.status
)

fun serahTerimaFrom(serahTerimaData: SerahTerimaData) : SerahTerima = SerahTerima(
    id = serahTerimaData.id,
    accessId = serahTerimaData.accessId,
    memberId = serahTerimaData.memberId,
    speciesId = serahTerimaData.speciesId,
    totalBibit = serahTerimaData.jumlahBibit,
    description = serahTerimaData.description,
    createdOn = serahTerimaData.createdOn,
    status = serahTerimaData.status
)