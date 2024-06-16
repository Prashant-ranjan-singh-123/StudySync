package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.Grade
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.widget.RateAssignmentDialog
import retrofit2.Call
import retrofit2.Response

class RateAssignmentPresenterImpl(private val dialog: RateAssignmentDialog) :
    Presenter.RateAssignmentPresenter {

    override fun rateAssignment(assignmentId: Int, grade: Grade) {
        ApiService.apiClient!!.rateAssignment(assignmentId, grade)
            .enqueue(object : CustomCallback<BaseResponse> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        rateAssignment(assignmentId, grade)
                    }
                }

                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.body() != null) {
                        dialog.onResponse(response.body()!!)
                    } else {
                        dialog.onRequestFailed()
                    }
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    super.onFailure(call, t)
                    dialog.onRequestFailed()
                }
            })
    }
}