package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.widget.MeetingDetailsMoreDialog
import retrofit2.Call
import retrofit2.Response

class MeetingDetailsMorePresenterImpl(private val dialog: MeetingDetailsMoreDialog) :
    Presenter.MeetingDetailsMorePresenter {

    override fun finishMeeting(meetingId: Int) {
        ApiService.apiClient!!.finishMeeting(meetingId, Any())
            .enqueue(object : CustomCallback<BaseResponse> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        finishMeeting(meetingId)
                    }
                }

                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.body() != null) {
                        dialog.onMeetingFinished(response.body()!!)
                    }
                }
            })
    }
}