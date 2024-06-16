package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.PaymentRequest
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.ChargeAccountPaymentFrag
import retrofit2.Call
import retrofit2.Response

class ChargeAccountPaymentPresenterImpl(private val frag: ChargeAccountPaymentFrag) :
    Presenter.ChargeAccountPaymentPresenter {

    override fun requestPayment(paymentRequest: PaymentRequest) {
        ApiService.apiClient!!.requestPayment(paymentRequest)
    }

    override fun chargeAccount(paymentRequest: PaymentRequest) {
        ApiService.apiClient!!.chargeAccount(paymentRequest)
    }

    override fun requestPaymentFromCharge(paymentRequest: PaymentRequest) {
        ApiService.apiClient!!.payWithCredit(paymentRequest).enqueue(object : CustomCallback<BaseResponse>{
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    requestPaymentFromCharge(paymentRequest)
                }
            }

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.body() != null) {
                    frag.onPaymentWithCharge(response.body()!!)
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                super.onFailure(call, t)
                frag.onRequestFailed()
            }

        })
    }

}