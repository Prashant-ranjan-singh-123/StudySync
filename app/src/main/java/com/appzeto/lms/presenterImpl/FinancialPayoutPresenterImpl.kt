package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.PayoutRes
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.FinancialPayoutFrag
import retrofit2.Call
import retrofit2.Response

class FinancialPayoutPresenterImpl(private val frag: FinancialPayoutFrag) :
    Presenter.FinancialPayoutPresenter {

    override fun getPayouts() {
        val payoutsReq = ApiService.apiClient!!.getPayouts()
        frag.addNetworkRequest(payoutsReq)
        payoutsReq.enqueue(object :CustomCallback<Data<PayoutRes>>{
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getPayouts()
                }
            }

            override fun onResponse(
                call: Call<Data<PayoutRes>>,
                response: Response<Data<PayoutRes>>
            ) {
                if (response.body() != null) {
                    frag.onPayoutsReceived(response.body()!!.data!!)
                }
            }

        })
    }
}