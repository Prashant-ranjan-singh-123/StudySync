package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.PaymentRequest
import com.appzeto.lms.model.Response
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.widget.ChargeDialog
import retrofit2.Call

class ChargeAccountPresenterImpl(private val dialog: ChargeDialog) :
    Presenter.ChargeAccountPresenter {
    override fun chargeAccount(paymentRequest: PaymentRequest) {
        ApiService.apiClient!!.chargeAccount(paymentRequest)
            .enqueue(object : CustomCallback<Data<Response>> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        chargeAccount(paymentRequest)
                    }
                }

                override fun onResponse(
                    call: Call<Data<Response>>,
                    response: retrofit2.Response<Data<Response>>
                ) {
                    if (response.body() != null) {
                        dialog.onCheckout(response.body()!!)
                    }
                }

            })
    }

}