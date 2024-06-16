package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.Message
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.widget.NewMessageDialog
import retrofit2.Call
import retrofit2.Response

class NewMessagePresenterImpl(private val dialog: NewMessageDialog) :
    Presenter.NewMessagePresenter {


    override fun addMessage(userId: Int, message: Message) {
        ApiService.apiClient!!.addNewMessage(userId, message)
            .enqueue(object : CustomCallback<BaseResponse> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        addMessage(userId, message)
                    }
                }

                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.body() != null) {
                        dialog.onMessageAdded(response.body()!!)
                    }
                }

            })
    }
}