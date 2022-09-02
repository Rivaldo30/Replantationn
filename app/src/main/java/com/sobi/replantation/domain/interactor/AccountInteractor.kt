package com.sobi.replantation.domain.interactor

import com.sobi.replantation.domain.PreferenceService
import com.sobi.replantation.domain.model.TokenData
import com.sobi.replantation.domain.model.UserData
import com.sobi.replantation.domain.model.UserService

class AccountInteractor(val preferenceService: PreferenceService,
                        val remoteUserService: UserService,
                        val tokenProvider : TokenProvider)    {

    suspend fun login(username: String, password: String, roleType: Int,deviceId: String): UserData {
        val token = tokenProvider.getTokenData() ?: throw InvalidTokenException(null)
        val response = remoteUserService.uniqueLogin(token.authorization, "${token.sobiDate}",
            username, password, roleType,deviceId)
        val data = response.data[0]

        preferenceService.accessId = data.userAccessId
        preferenceService.username = data.username
        preferenceService.firstName = data.firstName
        preferenceService.lastName = data.lastName
        preferenceService.koperasiId = data.koperasiId
        preferenceService.deviceId = data.deviceId
        return UserData(data.userAccessId,
            data.username,
            data.firstName,
            data.lastName,
            data.koperasiId,
            data.deviceId)
    }


    suspend fun logout(accessId: Int): UserData {
        val token = tokenProvider.getTokenData() ?: throw InvalidTokenException(null)
        val response = remoteUserService.logout(token.authorization, "${token.sobiDate}",
            accessId)
        val data = response.data[0]
        preferenceService.accessId = data.userAccessId
        preferenceService.username = data.username
        preferenceService.firstName = data.firstName
        preferenceService.lastName = data.lastName
        preferenceService.koperasiId = data.koperasiId
        preferenceService.deviceId = data.deviceId
        return UserData(data.userAccessId,
            data.username,
            data.firstName,
            data.lastName,
            data.koperasiId,
            data.deviceId)
    }

    fun getUserData(): UserData? {
        val accessId = if (preferenceService.accessId > -1) preferenceService.accessId else return null
        val username = preferenceService.username
        val firstName =  preferenceService.firstName
        val lastName =  preferenceService.lastName
        val koperasiId = preferenceService.koperasiId
        val deviceId = preferenceService.deviceId
        return UserData(accessId, username, firstName, lastName, koperasiId, deviceId)
    }

    fun logout() {
        preferenceService.accessId = -1
        preferenceService.username = ""
        preferenceService.firstName = ""
        preferenceService.lastName = ""
        preferenceService.koperasiId = -1
        preferenceService.deviceId = ""
    }

}

class InvalidTokenException(token: TokenData?): Exception("Invalid token data: $token")


