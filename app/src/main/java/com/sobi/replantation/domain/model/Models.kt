package com.sobi.replantation.domain.model

//import com.google.gson.annotations.SerializedName
import java.util.*

data class UserData(
        val accessId: Int?,
        var username: String?,
        val firstName: String?,
        val lastName: String?,
        val koperasiId: Int?,
        val deviceId: String?
)

data class UserDataLogout(
        val accessId: Int,
        var username: String?,
        val firstName: String?,
        val lastName: String?,
        val koperasiId: Int,
        val deviceId: String?
)

data class Assignment(
        val id: Int,
        val memberId: Int,
        val koperasiId: Int,
        val memberName: String,
        val memberNo: String,
        val photoUrl: String?,
        val idPhotoUrl: String?,
        val statementPhotoUrl: String?,
        val membershipPhoto: String?,
        val areaCount: Int,
        val totalBibit : Int,
        val finishedSerahTerima: Int = 0,
        val totalTerima :Int = 0

)

data class Area(
        val memberId: Int,
        val id: Int,
        val name: String,
        val certificateNumber: String,
        val koperasiId: Int?,
        val areaMeasure: String?,
        val jumlahTebang: String?,
        val photoSketsaUrl: String?,
        val photoSertifUrl: String?,
        val tree : String?

)


data class Species(
        val id: Int?,
        val name: String
)

data class SerahTerima(
        val id : Int,
        val accessId: Int,
        val memberId : Int,
        val speciesId: Int,
        val totalBibit: Int,
        val description : String?,
        val createdOn: String?,
        val status : Int? = 0
)

data class BuktiSerahTerima(
        val id : Int,
        val accessId: Int,
        val memberId : Int,
        val photoUrl : String,
        val createdOn: String?,
        val status : Int? = 0
)




