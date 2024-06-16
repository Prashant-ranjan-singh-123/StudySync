package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.Follow
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.widget.ForumOptionsDialog
import retrofit2.Call
import retrofit2.Response

class ForumOptionsPresenterImpl(val dialog: ForumOptionsDialog) : Presenter.ForumOptionsPresenter {
    override fun pinForumItem(id: Int) {
        ApiService.apiClient!!.pinForumItem(id, Follow())
            .enqueue(object : CustomCallback<BaseResponse> {
                override fun onStateChanged(): RetryListener {
                    return RetryListener {
                        pinForumItem(id)
                    }
                }

                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.body() != null) {
                        dialog.showResult(response.body()!!)
                    }
                }

            })
    }

    override fun pinForumItemAnswer(id: Int) {
        ApiService.apiClient!!.pinForumItemAnswer(id, Follow())
            .enqueue(object : CustomCallback<BaseResponse> {
                override fun onStateChanged(): RetryListener {
                    return RetryListener {
                        pinForumItemAnswer(id)
                    }
                }

                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.body() != null) {
                        dialog.showResult(response.body()!!)
                    }
                }

            })
    }

    override fun markAnswerAsResolved(id: Int) {
        ApiService.apiClient!!.markAnswerAsResolved(id, Follow())
            .enqueue(object : CustomCallback<BaseResponse> {
                override fun onStateChanged(): RetryListener {
                    return RetryListener {
                        markAnswerAsResolved(id)
                    }
                }

                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.body() != null) {
                        dialog.showResult(response.body()!!)
                    }
                }

            })
    }


}