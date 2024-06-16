package com.appzeto.lms.presenterImpl

import com.google.gson.Gson
import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.Login
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.SignInFrag
import retrofit2.Call
import retrofit2.Response
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Data

class SignInPresenterImpl(private val frag: SignInFrag) : ThirdPartyPresenterImpl(frag),
    Presenter.SignInPresenter {

    override fun login(login: Login) {
        val loginRequest = ApiService.apiClient!!.login(login)
        frag.addNetworkRequest(loginRequest)
        loginRequest.enqueue(object : CustomCallback<Data<com.appzeto.lms.model.Response>> {

            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    login(login)
                }
            }

            override fun onResponse(
                call: Call<Data<com.appzeto.lms.model.Response>>,
                response: Response<Data<com.appzeto.lms.model.Response>>
            ) {
                if (response.body() != null) {
                    frag.onSuccessfulLogin(response.body()!!)
                } else {
                    val error = Gson().fromJson<BaseResponse>(
                        response.errorBody()?.string(),
                        BaseResponse::class.java
                    )

                    frag.onErrorOccured(error)
                }
            }

            override fun onFailure(
                call: Call<Data<com.appzeto.lms.model.Response>>,
                t: Throwable
            ) {
                super.onFailure(call, t)
                frag.onErrorOccured(null)
            }
        })
    }
}