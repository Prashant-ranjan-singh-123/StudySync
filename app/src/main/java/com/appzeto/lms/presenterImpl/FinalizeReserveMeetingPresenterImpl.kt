package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.ReserveTimeMeeting
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.widget.FinalizeReserveMeetingDialog
import retrofit2.Call
import retrofit2.Response

class FinalizeReserveMeetingPresenterImpl(private val dialog: FinalizeReserveMeetingDialog) :
    Presenter.FinalizeReserveMeetingPresenter {

    override fun reserveMeeting(reserveMeeting: ReserveTimeMeeting) {
        ApiService.apiClient!!.reserveMeeting(reserveMeeting)
            .enqueue(object : CustomCallback<BaseResponse> {
                override fun onStateChanged(): RetryListener {
                    return RetryListener {
                        reserveMeeting(reserveMeeting)
                    }
                }

                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.body() != null) {
                        dialog.onMeetingReserved(response.body()!!)
                    } else {
                        dialog.onFailed()
                    }
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    super.onFailure(call, t)
                    dialog.onFailed()
                }
            })
    }
}