package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.widget.PayoutRequestDialog
import retrofit2.Call
import retrofit2.Response

class PayoutRequestPresenterImpl(private val dialog: PayoutRequestDialog) :
    Presenter.PayoutRequestPresenter {

    override fun requestPayout() {
        ApiService.apiClient!!.requestPayout(Any()).enqueue(object : CustomCallback<BaseResponse> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    requestPayout()
                }
            }

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.body() != null) {
                    dialog.onPayoutSaved(response.body()!!)
                }
            }

        })
    }
}