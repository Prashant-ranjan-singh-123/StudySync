package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.Notice
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.NoticesFrag
import retrofit2.Call
import retrofit2.Response

class NoticesPresenterImpl(private val frag: NoticesFrag) : Presenter.NoticesPresenter {

    override fun getNotices(courseId: Int) {
        val notices = ApiService.apiClient!!.getNotices(courseId)
        frag.addNetworkRequest(notices)
        notices.enqueue(object : CustomCallback<Data<List<Notice>>> {
            override fun onStateChanged(): RetryListener {
                return RetryListener {
                    getNotices(courseId)
                }
            }

            override fun onResponse(
                call: Call<Data<List<Notice>>>,
                response: Response<Data<List<Notice>>>
            ) {
                if (response.body() != null) {
                    frag.onNoticesReceived(response.body()!!.data!!)
                }
            }
        })
    }
}