package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.OfflinePayment
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.widget.OfflinePaymentDialog
import retrofit2.Call
import retrofit2.Response

class OfflinePaymentDialogPresenterImpl(private val dialog: OfflinePaymentDialog) :
    Presenter.OfflinePaymentDialogPresenter {

    override fun addOfflinePayment(offlinePayment: OfflinePayment) {
        val addOfflinePaymentsReq = ApiService.apiClient!!.addOfflinePayments(offlinePayment)
        dialog.addNetworkRequest(addOfflinePaymentsReq)
        addOfflinePaymentsReq.enqueue(object : CustomCallback<BaseResponse> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    addOfflinePayment(offlinePayment)
                }
            }

            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>
            ) {
                if (response.body() != null) {
                    dialog.onOfflinePaymentSaved(response.body()!!)
                }
            }

        })
    }
}