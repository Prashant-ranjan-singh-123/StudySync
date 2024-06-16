package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.ForgetPassword
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.ForgetPasswordFrag
import retrofit2.Call
import retrofit2.Response

class ForgotPasswordPresenterImpl(private val frag: ForgetPasswordFrag): Presenter.ForgotPasswordPresenter {

    override fun sendChangePasswordLink(forgetPassword: ForgetPassword) {
        val sendChangePasswordLinkReq = ApiService.apiClient!!.sendChangePasswordLink(forgetPassword)
        frag.addNetworkRequest(sendChangePasswordLinkReq)
        sendChangePasswordLinkReq.enqueue(object : CustomCallback<BaseResponse>{
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    sendChangePasswordLink(forgetPassword)
                }
            }

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.body() != null) {
                    frag.onPasswordChanged(response.body()!!)
                }
            }

        })
    }
}