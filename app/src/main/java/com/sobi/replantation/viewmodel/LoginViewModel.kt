package com.sobi.replantation.viewmodel

import androidx.lifecycle.ViewModel
import com.sobi.replantation.domain.PreferenceService
import com.sobi.replantation.domain.interactor.AccountInteractor
import com.sobi.replantation.domain.interactor.TokenProvider
import com.sobi.replantation.domain.model.UserData
import com.sobi.replantation.domain.model.UserService
import com.sobi.replantation.domain.remote.BaseUrlLoader
import javax.inject.Inject

class LoginViewModel @Inject constructor(val preferenceService: PreferenceService,
                                         val userService: UserService,
                                         val tokenProvider: TokenProvider):ViewModel(){

    fun isUsernamePasswordValid(username: String?, password: String?) = username != null && password != null

    val accountInteractor = AccountInteractor(preferenceService, userService, tokenProvider)

    suspend fun login(username: String?, password: String?,deviceId: String?): UserData? {
        val uname = username ?: return null
        val pass = password ?: return null
        val device = deviceId ?: return null
        return accountInteractor.login(uname, pass, 3,device)
    }



    fun getAccountData(): UserData? = accountInteractor.getUserData()
}
