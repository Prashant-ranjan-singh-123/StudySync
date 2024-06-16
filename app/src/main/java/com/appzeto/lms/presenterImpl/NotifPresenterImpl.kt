package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Count
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.Notif
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.NotifsFrag
import retrofit2.Call
import retrofit2.Response

class NotifPresenterImpl(private val frag: NotifsFrag) : Presenter.NotifPresenter {

    override fun getNotifs() {
        val notifsReq = ApiService.apiClient!!.getNotifs()
        frag.addNetworkRequest(notifsReq)
        notifsReq.enqueue(object :CustomCallback<Data<Count<Notif>>>{
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getNotifs()
                }
            }

            override fun onResponse(
                call: Call<Data<Count<Notif>>>,
                response: Response<Data<Count<Notif>>>
            ) {
                if (response.body() != null) {
                    frag.onNotifsReceived(response.body()!!)
                }
            }
        })
    }
}