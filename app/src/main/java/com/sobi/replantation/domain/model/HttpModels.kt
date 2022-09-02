package com.sobi.replantation.domain.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

//Requests & Responses

//Login
data class LoginRequest(val data: LoginRequestData)

data class LoginRequestData(
    val username: String,
    val password: String,
    @field:JsonProperty("role_type")
    val roleType: Int
)

data class UniqueLoginRequest(val data: UniqueLoginRequestData)

data class UniqueLoginRequestData(
        @field:JsonProperty("username")
        val username: String,
        @field:JsonProperty("password")
        val password: String,
        @field:JsonProperty("role_type")
        val roleType: Int,
        @field:JsonProperty("primary_device_id")
        val deviceId: String
)


data class LoginResponse(
    val status: Int,
    val message: String,
    val data: List<LoginResponseData>
)

data class LogoutResponse(
        val status: Int,
        val message: String,
        val data: List<LogoutResponseData>
)

data class TreeResponse(
    val status: Int,
    val message: String,
    @field:JsonProperty("tree_id")
    val tree_id: Int
)

data class LoginResponseData(
    @field:JsonProperty("user_access_id")
    val userAccessId: Int,
    @field:JsonProperty("username")
    val username: String,
    @field:JsonProperty("password_app")
    val passwordApp: String,
    @field:JsonProperty("firstname")
    val firstName: String,
    @field:JsonProperty("lastname")
    val lastName: String,
    @field:JsonProperty("koperasi_id")
    val koperasiId: Int,
    @field:JsonProperty("primary_device_id")
    val deviceId: String?
)

data class LogoutResponseData(
        @field:JsonProperty("user_access_id")
        val userAccessId: Int,
        @field:JsonProperty("username")
        val username: String?,
        @field:JsonProperty("password_app")
        val passwordApp: String?,
        @field:JsonProperty("firstname")
        val firstName: String?,
        @field:JsonProperty("lastname")
        val lastName: String?,
        @field:JsonProperty("koperasi_id")
        val koperasiId: Int,
        @field:JsonProperty("primary_device_id")
        val deviceId: String?
)



//token
data class TokenResponse(
    val status: String,
    val data: TokenData
)

data class TokenData(
    @field:JsonProperty("sobi-date")
    val sobiDate: Int,
    val authorization: String
)



data class BulkDataResponse(
    val status: Int,
    val message: String,
    val data: List<BulkMemberResponseItem>
)


@JsonIgnoreProperties(ignoreUnknown = true)
data class BulkMemberResponseItem(
    @field:JsonProperty("id")
    val id: Int,
    @field:JsonProperty("koperasi_id")
    val koperasiId: Int,
    @field:JsonProperty("member_id")
    val memberId: Int,
    @field:JsonProperty("member_name")
    val memberName: String,
    @field:JsonProperty("member_number")
    val memberNo: String,
    val photo: String?,
    @field:JsonProperty("id_photo")
    val idPhoto: String?,
    @field:JsonProperty("statement_photo")
    val statementPhoto: String?,
    @field:JsonProperty("membership_form_photo")
    val membershipPhoto: String?,
    @field:JsonProperty("jumlah_lahan")
    val lahanCount: Int,
    @field:JsonProperty("jumlah_bibit")
    val jumlahBibit :Int,
    @field:JsonProperty("areas")
    val areas : List<BulkAreaResponseItem>
)


@JsonIgnoreProperties(ignoreUnknown = true)
data class BulkAreaResponseItem(
    @field:JsonProperty("member_id")
    val memberId: Int,
    @field:JsonProperty("koperasi_id")
    val koperasiId: Int,
    @field:JsonProperty("parent_id")
    val id:Int,
    @field:JsonProperty("area_name")
    val areaName:String,
    @field:JsonProperty("certificate_number")
    val certificateNumber:String,
    @field:JsonProperty("area_measure")
    val areaMeasure:String,
    @field:JsonProperty("foto_sertifikat")
    val photoSertif: String?,
    @field:JsonProperty("jumlah_pohon")
    val jumlahTebang : String?,
    @field:JsonProperty("foto_sketsa")
    val photoSketsa: String?,
    @field:JsonProperty("trees")
    val pohon: String?
)


data class BulkTreeResponseItem(
    @field:JsonProperty("id")
    val id: Int,
    @field:JsonProperty("area_id")
    val areaId: Int,
    @field:JsonProperty("tree_number")
    val treeNumber: String,
    @field:JsonProperty("species_id")
    val speciesId: Int?,
    @field:JsonProperty("log_form_id")
    val logFormId: Int,
    @field:JsonProperty("log_condition_id")
    val logConditionId: Int,
    val photo: String,
    val diameter: Double,
    @field:JsonProperty("diameter_manual")
    val diameterManual: String?,
    @field:JsonProperty("keliling")
    val keliling: Double?,
    @field:JsonProperty("keliling_manual")
    val kelilingManual: String,
    val length: Double,
    @field:JsonProperty("length_manual")
    val lengthManual: String?,
    val lat: String,
    val lng: String,
    val timestamp: String,
    @field:JsonProperty("user_id")
    val userId: Int?,
    @field:JsonProperty("is_valid")
    val isValid: Int,
    @field:JsonProperty("non_valid_reason")
    val nonValidReason: String?,
    @field:JsonProperty("koperasi_id")
    val koperasiId: Int,
    @field:JsonProperty("verification_date")
    val verificationDate: String?,
    @field:JsonProperty("verification_by")
    val verificationBy: Int?,
    @field:JsonProperty("verification_status")
    val verificationStatus: Int,
    val uid: String,
    @field:JsonProperty("tree_status")
    val treeStatus: Int?,
    @field:JsonProperty("app_source")
    val appSource: Int,
    @field:JsonProperty("last_adjustment")
    val lastAdjustment: String?,
    @field:JsonProperty("adjustment_sequence")
    val adjustmentSequence: Int?,
    @field:JsonProperty("booking_contract_id")
    val bookingContractId: String?,
    @field:JsonProperty("created_on")
    val createdOn: String?,
    @field:JsonProperty("created_by")
    val createdBy: Int?,
    @field:JsonProperty("modified_on")
    val modifiedOn: String?,
    @field:JsonProperty("modified_by")
    val modifiedBy: String?,
    val deleted: Int,
    @field:JsonProperty("available_status")
    val availableStatus: Int,
    @field:JsonProperty("available_updated_at")
    val availableUpdatedAt: String?,
    @field:JsonProperty("status_pup")
    val statusPup: Int,
    @field:JsonProperty("akurasi_maps")
    val akurasi_maps: String?
)


data class BulkDataPengayaanResponse(
    val status: Int,
    val message: String,
    val data: List<BulkMemberPengayaanResponseItem>
)


@JsonIgnoreProperties(ignoreUnknown = true)
data class BulkMemberPengayaanResponseItem(
    @field:JsonProperty("member_id")
    val memberId: Int,
    @field:JsonProperty("user_access_id")
    val userAccessId: Int,
    @field:JsonProperty("member_name")
    val memberName: String,
    @field:JsonProperty("member_no")
    val memberNo: String,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val photo: String?,
    @field:JsonProperty("id_photo")
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val idPhoto: String?,
    @field:JsonProperty("statement_photo")
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val statementPhoto: String?,
    @field:JsonProperty("membership_form_photo")
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val membershipPhoto: String?,
    @field:JsonProperty("jumlah_lahan")
    val areaCount: Int,
    val areas: List<BulkAreaPengayaanResponseItem>
)


@JsonIgnoreProperties(ignoreUnknown = true)
data class BulkAreaPengayaanResponseItem(
    @field:JsonProperty("member_id")
    val memberId: Int,
    @field:JsonProperty("area_id")
    val id:Int,
    @field:JsonProperty("area_name")
    val areaName:String,
    @field:JsonProperty("koperasi_id")
    val koperasiId:Int?,
    @field:JsonProperty("koperasi_name")
    val koperasiName:String?,
    @field:JsonProperty("kode")
    val kode:String?,
    @field:JsonProperty("certificate_number")
    val certificateNumber:String,
    @field:JsonProperty("area_measure")
    val areaMeasure:Int,
    @field:JsonProperty("status_konservasi")
    val statusKonservasi:Int,
    val lat:String?,
    val lng:String?,
    @field:JsonProperty("foto_sertifikat")
    val certificatePhoto:String?,
    @field:JsonProperty("foto_sketsa")
    val sketchPhoto: String?,
    @field:JsonProperty("finished_pengayaan")
    val finishedPengayaan: Int,
    @field:JsonProperty("deleted")
    val deleted: Int,
    @field:JsonProperty("delineasi_id")
    val delineasiId : Int,
    @field:JsonProperty("trees_pengayaan")
    val trees: List<BulkTreePengayaanResponseItem>?
)

data class BulkTreePengayaanResponseItem(
        @field:JsonProperty("id")
        val id: Int,
        @field:JsonProperty("area_id")
        val areaId: Int,
        @field:JsonProperty("tree_number")
        val treeNumber: String,
        @field:JsonProperty("species_id")
        val speciesId: Int?,
        val photo: String,
        val diameter: Double,
        @field:JsonProperty("diameter_manual")
        val diameterManual: String?,
        @field:JsonProperty("keliling")
        val keliling: Double?,
        @field:JsonProperty("keliling_manual")
        val kelilingManual: String,
        val length: Double,
        @field:JsonProperty("length_manual")
        val lengthManual: String?,
        val lat: String,
        val lng: String,
        val timestamp: String,
        @field:JsonProperty("user_id")
        val userId: Int?,
        @field:JsonProperty("is_valid")
        val isValid: Int,
        @field:JsonProperty("non_valid_reason")
        val nonValidReason: String?,
        @field:JsonProperty("koperasi_id")
        val koperasiId: Int,
        @field:JsonProperty("verification_date")
        val verificationDate: String?,
        @field:JsonProperty("verification_by")
        val verificationBy: Int?,
        @field:JsonProperty("verification_status")
        val verificationStatus: Int,
        val uid: String,
        @field:JsonProperty("tree_status")
        val treeStatus: Int?,
        @field:JsonProperty("app_source")
        val appSource: Int,
        @field:JsonProperty("last_adjustment")
        val lastAdjustment: String?,
        @field:JsonProperty("adjustment_sequence")
        val adjustmentSequence: Int?,
        @field:JsonProperty("booking_contract_id")
        val bookingContractId: String?,
        @field:JsonProperty("created_on")
        val createdOn: String?,
        @field:JsonProperty("created_by")
        val createdBy: Int?,
        @field:JsonProperty("modified_on")
        val modifiedOn: String?,
        @field:JsonProperty("modified_by")
        val modifiedBy: String?,
        val deleted: Int,
        @field:JsonProperty("available_status")
        val availableStatus: Int,
        @field:JsonProperty("available_updated_at")
        val availableUpdatedAt: String?,
        @field:JsonProperty("status_pup")
        val statusPup: Int,
        @field:JsonProperty("akurasi_maps")
        val akurasi_maps: String?,
        @field:JsonProperty("desc")
        val desc: String?
)

//species
data class SpeciesResponse(val status: Int, val message: String, val data: List<Species>)








