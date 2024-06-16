package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.Review
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.widget.CourseReviewDialog
import retrofit2.Call
import retrofit2.Response

class CourseReviewPresenterImpl(private val dialog: CourseReviewDialog) :
    Presenter.CourseReviewPresenter {

    override fun addReview(review: Review) {
        ApiService.apiClient!!.addCourseReview(review)
            .enqueue(object : CustomCallback<BaseResponse> {
                override fun onStateChanged(): RetryListener {
                    return RetryListener {
                        addReview(review)
                    }
                }

                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.body() != null) {
                        dialog.onReviewSaved(response.body()!!, review)
                    }
                }

            })
    }

}