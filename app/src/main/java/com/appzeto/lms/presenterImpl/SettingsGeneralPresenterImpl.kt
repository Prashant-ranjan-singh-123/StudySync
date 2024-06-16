package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.UserChangeSettings
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.SettingsGeneralFrag
import retrofit2.Call
import retrofit2.Response

class SettingsGeneralPresenterImpl(private val frag: SettingsGeneralFrag) :
    Presenter.SettingsGeneralPresenter {

    override fun changeProfileSettings(changeSettings: UserChangeSettings) {
        ApiService.apiClient!!.changeProfileSettings(changeSettings)
            .enqueue(object : CustomCallback<BaseResponse> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        changeProfileSettings(changeSettings)
                    }
                }

                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.body() != null) {
                        frag.onSettingsChanged(response.body()!!, changeSettings)
                    }
                }

            })
    }
}