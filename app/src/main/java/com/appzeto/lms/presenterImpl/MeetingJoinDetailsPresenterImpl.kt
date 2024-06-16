package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.ReserveMeeting
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.widget.MeetingJoinDetailsDialog
import retrofit2.Call
import retrofit2.Response

class MeetingJoinDetailsPresenterImpl(private val dialog: MeetingJoinDetailsDialog) :
    Presenter.MeetingJoinDetailsPresenter {

    override fun createJoin(reserveMeeting: ReserveMeeting) {
        ApiService.apiClient!!.createJoinForMeeting(reserveMeeting)
            .enqueue(object : CustomCallback<BaseResponse> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        createJoin(reserveMeeting)
                    }
                }

                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.body() != null) {
                        dialog.onMeetingJoinAdded(response.body()!!)
                    }
                }

            })
    }
}