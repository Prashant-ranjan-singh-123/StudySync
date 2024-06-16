package com.appzeto.lms.presenterImpl

import com.google.gson.Gson
import com.appzeto.lms.manager.App
import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.Response
import com.appzeto.lms.model.ThirdPartyLogin
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.abs.UserAuthFrag
import retrofit2.Call

open class ThirdPartyPresenterImpl(
    private val frag: UserAuthFrag
) : Presenter.ThirdPartyPresenter {

    override fun facebookSignInUp(thirdPartyLogin: ThirdPartyLogin) {
        val signUpRequest = ApiService.apiClient!!.registerWithFacebook(thirdPartyLogin)
        frag.addNetworkRequest(signUpRequest)
        signUpRequest.enqueue(object : CustomCallback<Data<Response>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    facebookSignInUp(thirdPartyLogin)
                }
            }

            override fun onResponse(
                call: Call<Data<Response>>,
                response: retrofit2.Response<Data<Response>>
            ) {
                onThridPartyRes(
                    response,
                    App.Companion.RegistrationProvider.FACEBOOK.value(),
                    thirdPartyLogin
                )
            }

            override fun onFailure(call: Call<Data<Response>>, t: Throwable) {
                super.onFailure(call, t)
                frag.onErrorOccured(null)
            }
        })
    }

    override fun googleSignInUp(thirdPartyLogin: ThirdPartyLogin) {
        val signUpRequest = ApiService.apiClient!!.registerWithGoogle(thirdPartyLogin)
        frag.addNetworkRequest(signUpRequest)
        signUpRequest.enqueue(object : CustomCallback<Data<Response>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    googleSignInUp(thirdPartyLogin)
                }
            }

            override fun onResponse(
                call: Call<Data<Response>>,
                response: retrofit2.Response<Data<Response>>
            ) {
                onThridPartyRes(
                    response,
                    App.Companion.RegistrationProvider.GOOGLE.value(),
                    thirdPartyLogin
                )
            }

            override fun onFailure(call: Call<Data<Response>>, t: Throwable) {
                super.onFailure(call, t)
                frag.onErrorOccured(null)
            }
        })
    }

    private fun onThridPartyRes(
        response: retrofit2.Response<Data<Response>>,
        provider: Int,
        thirdPartyLogin: ThirdPartyLogin
    ) {
        if (response.body() != null) {
            frag.onThirdPartyLogin(response.body()!!, provider, thirdPartyLogin)

        } else {
            val error = Gson().fromJson<BaseResponse>(
                response.errorBody()?.string(),
                BaseResponse::class.java
            )

            frag.onErrorOccured(error)
        }
    }
}