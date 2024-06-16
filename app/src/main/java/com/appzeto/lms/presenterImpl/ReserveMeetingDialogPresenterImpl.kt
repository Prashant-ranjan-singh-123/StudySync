package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.*
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.widget.ReserveMeetingDialog
import retrofit2.Call
import retrofit2.Response

class ReserveMeetingDialogPresenterImpl(private val dialog: ReserveMeetingDialog) :
    Presenter.ReserveMeetingDialogPresenter {

    override fun getAvailableMeetingTimes(userId: Int, date: String) {
        val availableMeetingTimesReq = ApiService.apiClient!!.getAvailableMeetingTimes(userId, date)
        dialog.addNetworkRequest(availableMeetingTimesReq)
        availableMeetingTimesReq.enqueue(object : CustomCallback<Data<Count<Timing>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getAvailableMeetingTimes(userId, date)
                }
            }

            override fun onResponse(
                call: Call<Data<Count<Timing>>>,
                response: Response<Data<Count<Timing>>>
            ) {
                if (response.body() != null) {
                    dialog.onTimingsReceived(response.body()!!.data!!.items)
                }
            }
        })
    }
}