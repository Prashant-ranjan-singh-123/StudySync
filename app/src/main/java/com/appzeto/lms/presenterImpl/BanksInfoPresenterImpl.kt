package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Count
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.SystemBankAccount
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.BanksInfoFrag
import retrofit2.Call
import retrofit2.Response

class BanksInfoPresenterImpl(private val frag: BanksInfoFrag) : Presenter.BanksInfoPresenter {

    override fun getBanksInfo() {
        val bankInfosReq = ApiService.apiClient!!.getBankInfos()
        frag.addNetworkRequest(bankInfosReq)
        bankInfosReq.enqueue(object : CustomCallback<Data<Count<SystemBankAccount>>> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        getBanksInfo()
                    }
                }

                override fun onResponse(
                    call: Call<Data<Count<SystemBankAccount>>>,
                    response: Response<Data<Count<SystemBankAccount>>>
                ) {
                    if (response.body() != null) {
                        frag.onInfosReceived(response.body()!!.data!!.items)
                    }
                }

            })
    }
}