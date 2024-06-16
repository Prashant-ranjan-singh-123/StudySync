package com.appzeto.lms.presenterImpl

import com.google.gson.Gson
import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.User
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.widget.UserRegistrationDialog
import retrofit2.Call
import retrofit2.Response

class UserRegistrationPresenterImpl(private val dialog: UserRegistrationDialog) :
    Presenter.UserRegistrationPresenter {

    override fun register(user: User) {
        val registerUserRequest = ApiService.apiClient!!.registerUser(user)
        dialog.addNetworkRequest(registerUserRequest)
        registerUserRequest.enqueue(object : CustomCallback<Data<com.appzeto.lms.model.Response>> {

            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    register(user)
                }
            }

            override fun onResponse(call: Call<Data<com.appzeto.lms.model.Response>>,
                                    response: Response<Data<com.appzeto.lms.model.Response>>) {
                if (response.body() != null) {
                    dialog.onRegistrationSaved(response.body()!!, user)

                } else {
                    val error = Gson().fromJson<BaseResponse>(
                        response.errorBody()?.string(),
                        BaseResponse::class.java
                    )

                    dialog.onErrorOccured(error)
                }
            }
        })
    }
}