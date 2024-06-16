package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.Follow
import com.appzeto.lms.presenter.Presenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogoutPresenterImpl: Presenter.LogoutPresenter {
    override fun logout() {
        ApiService.apiClient!!.logout(Follow()).enqueue(object : Callback<BaseResponse> {
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {

            }
        })
    }
}