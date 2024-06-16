package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.SalesRes
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.FinancialSalesFrag
import retrofit2.Call
import retrofit2.Response

class FinancialSalesPresenterImpl(private val frag: FinancialSalesFrag) :
    Presenter.FinancialSalesPresenter {

    override fun getSales() {
        val salesReq = ApiService.apiClient!!.getSales()
        frag.addNetworkRequest(salesReq)
        salesReq.enqueue(object : CustomCallback<Data<SalesRes>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getSales()
                }
            }

            override fun onResponse(
                call: Call<Data<SalesRes>>,
                response: Response<Data<SalesRes>>
            ) {
                if (response.body() != null) {
                    frag.onSalesReceived(response.body()!!.data!!)
                }
            }

        })
    }
}