package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.OfflinePayment
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.FinancialOfflinePaymentsFrag
import retrofit2.Call
import retrofit2.Response

class FinancialOfflinePaymentsPresenterImpl(private val frag: FinancialOfflinePaymentsFrag) :
    Presenter.FinancialOfflinePaymentsPresenter {

    override fun getOfflinePayments() {
        val offlinePaymentsReq = ApiService.apiClient!!.getOfflinePayments()
        frag.addNetworkRequest(offlinePaymentsReq)
        offlinePaymentsReq.enqueue(object : CustomCallback<Data<List<OfflinePayment>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getOfflinePayments()
                }
            }

            override fun onResponse(
                call: Call<Data<List<OfflinePayment>>>,
                response: Response<Data<List<OfflinePayment>>>
            ) {
                if (response.body() != null) {
                    frag.onPaymentsReceived(response.body()!!.data!!)
                }
            }

        })
    }
}