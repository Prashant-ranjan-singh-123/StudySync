package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.Course
import com.appzeto.lms.model.Follow
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.widget.PricingPlansDialog
import retrofit2.Call
import retrofit2.Response

class PricingPlansPresenterImpl(private val dialog: PricingPlansDialog) :
    Presenter.PricingPlansPresenter {

    override fun purchaseWithPoints(course: Course) {
        val retryListener = RetryListener { purchaseWithPoints(course) }
        val callback = getCallback(retryListener)
        if (course.isBundle()) {
            ApiService.apiClient!!.bundlePurchaseWithPoints(course.id, Follow()).enqueue(callback)
        } else {
            ApiService.apiClient!!.purchaseWithPoints(course.id, Follow()).enqueue(callback)
        }
    }

    private fun getCallback(retryListener: RetryListener): CustomCallback<BaseResponse> {
        return object : CustomCallback<BaseResponse> {
            override fun onStateChanged(): RetryListener {
                return retryListener
            }

            override fun onResponse(
                call: Call<BaseResponse>,
                res: Response<BaseResponse>
            ) {
                if (res.body() != null) {
                    dialog.onPurchase(res.body()!!)
                }
            }
        }
    }
}