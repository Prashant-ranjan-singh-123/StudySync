package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Count
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.FinancialSummary
import com.appzeto.lms.model.PaymentRequest
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.FinancialSummaryFrag
import retrofit2.Call
import retrofit2.Response

class FinancialSummaryPresenterImpl(private val frag: FinancialSummaryFrag) :
    Presenter.FinancialSummaryPresenter {

    override fun getSummary() {
        val financialSummaryReq = ApiService.apiClient!!.getFinancialSummary()
        frag.addNetworkRequest(financialSummaryReq)
        financialSummaryReq.enqueue(object : CustomCallback<Data<Count<FinancialSummary>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getSummary()
                }
            }

            override fun onResponse(
                call: Call<Data<Count<FinancialSummary>>>,
                response: Response<Data<Count<FinancialSummary>>>
            ) {
                if (response.body() != null) {
                    frag.onSummariesReceived(response.body()!!.data!!.items)
                }
            }
        })
    }

    override fun chargeAccount(paymentRequest: PaymentRequest) {
        ApiService.apiClient!!.chargeAccount(paymentRequest)
            .enqueue(object : CustomCallback<Data<com.appzeto.lms.model.Response>> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        chargeAccount(paymentRequest)
                    }
                }

                override fun onResponse(
                    call: Call<Data<com.appzeto.lms.model.Response>>,
                    response: retrofit2.Response<Data<com.appzeto.lms.model.Response>>
                ) {
                    if (response.body() != null) {
                        frag.onCheckout(response.body()!!)
                    }
                }

            })
    }

}