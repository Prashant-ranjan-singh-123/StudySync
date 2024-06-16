package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.CommentDetailsFrag
import retrofit2.Call
import retrofit2.Response

class CommentDetailsPresenterImpl(private val frag: CommentDetailsFrag) :
    Presenter.CommentDetailsPresenter {

    override fun removeComment(commentId: Int) {
        ApiService.apiClient!!.removeComment(commentId)
            .enqueue(object : CustomCallback<BaseResponse> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        removeComment(commentId)
                    }
                }

                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.body() != null) {
                        frag.onCommentRemoved(response.body()!!)
                    }
                }
            })
    }
}