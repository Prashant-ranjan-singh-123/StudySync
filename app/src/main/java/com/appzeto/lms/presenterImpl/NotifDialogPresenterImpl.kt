package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.widget.NotifDialog
import retrofit2.Call
import retrofit2.Response

class NotifDialogPresenterImpl(private val dialog: NotifDialog) : Presenter.NotifDialogPresenter {

    override fun setStatusToSeen(notifId: Int) {
        ApiService.apiClient!!.setNotifStatusToSeen(notifId)
            .enqueue(object : CustomCallback<BaseResponse> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        setStatusToSeen(notifId)
                    }
                }

                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.body() != null && response.body()!!.isSuccessful) {
                        dialog.onNotifSatusChange()
                    }
                }
            })
    }
}