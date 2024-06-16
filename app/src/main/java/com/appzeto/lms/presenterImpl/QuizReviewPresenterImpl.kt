package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.QuizAnswerItem
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.QuizReviewFrag
import retrofit2.Call
import retrofit2.Response

class QuizReviewPresenterImpl(private val frag: QuizReviewFrag) : Presenter.QuizReviewPresenter {

    override fun storeReviewResult(resultId: Int, review: List<QuizAnswerItem>) {
        ApiService.apiClient!!.storeReviewResult(resultId, review)
            .enqueue(object : CustomCallback<BaseResponse> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        storeReviewResult(resultId, review)
                    }
                }

                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.body() != null) {
                        frag.onResultStored(response.body()!!)
                    }
                }

            })
    }
}