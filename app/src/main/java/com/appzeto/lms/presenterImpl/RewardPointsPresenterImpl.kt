package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.Points
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.RewardPointsFrag
import retrofit2.Call
import retrofit2.Response

class RewardPointsPresenterImpl(private val frag: RewardPointsFrag) :
    Presenter.RewardPointsPresenter {

    override fun getPoints() {
        val pointsReq = ApiService.apiClient!!.getPoints()
        frag.addNetworkRequest(pointsReq)
        pointsReq.enqueue(object : CustomCallback<Data<Points>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getPoints()
                }
            }

            override fun onResponse(call: Call<Data<Points>>, res: Response<Data<Points>>) {
                if (res.body() != null) {
                    frag.onPointsReceived(res.body()!!.data!!)
                }
            }
        })
    }
}