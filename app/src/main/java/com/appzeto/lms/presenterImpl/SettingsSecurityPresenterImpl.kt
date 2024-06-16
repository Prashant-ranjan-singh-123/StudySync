package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.ChangePassword
import com.appzeto.lms.model.Data
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.SettingsSecurityFrag
import retrofit2.Call
import retrofit2.Response

class SettingsSecurityPresenterImpl(private val frag: SettingsSecurityFrag) : Presenter.SettingsSecurityPresenter {

    override fun changePassword(changePassword: ChangePassword) {
        ApiService.apiClient!!.changePassword(changePassword)
            .enqueue(object : CustomCallback<Data<com.appzeto.lms.model.Response>> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        changePassword(changePassword)
                    }
                }

                override fun onResponse(
                    call: Call<Data<com.appzeto.lms.model.Response>>,
                    response: Response<Data<com.appzeto.lms.model.Response>>
                ) {
                    if (response.body() != null) {
                        frag.onPasswordChanges(response.body()!!)
                    }
                }

            })
    }

}