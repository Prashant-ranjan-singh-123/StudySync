package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.Meetings
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.MeetingsTabFrag
import retrofit2.Call
import retrofit2.Response

class MeetingsPresenterImpl(private val frag: MeetingsTabFrag) : Presenter.MeetingsPresenter {

    override fun getMeetings() {
        val meetingsReq = ApiService.apiClient!!.getMeetings()
        frag.addNetworkRequest(meetingsReq)
        meetingsReq.enqueue(object : CustomCallback<Data<Meetings>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getMeetings()
                }
            }

            override fun onResponse(
                call: Call<Data<Meetings>>,
                response: Response<Data<Meetings>>
            ) {
                if (response.body() != null) {
                    frag.onMeetingsReceived(response.body()!!.data!!)
                }
            }
        })
    }
}