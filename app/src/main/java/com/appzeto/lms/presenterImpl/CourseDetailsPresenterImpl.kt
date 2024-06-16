package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.AddToCart
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.CourseDetailsFrag
import retrofit2.Call
import retrofit2.Response

class CourseDetailsPresenterImpl(private val frag: CourseDetailsFrag) :
    Presenter.CourseDetailsPresenter {

    override fun subscribe(addToCart: AddToCart) {
        ApiService.apiClient!!.subscribe(addToCart).enqueue(object : CustomCallback<BaseResponse> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    subscribe(addToCart)
                }
            }

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.body() != null) {
                    frag.onSubscribed(response.body()!!)
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                super.onFailure(call, t)
                frag.onRequestFailed()
            }
        })
    }

    override fun addCourseToUserCourse(courseId: Int) {
        val addFreeCourseReq = ApiService.apiClient!!.addFreeCourse(courseId)
        frag.addNetworkRequest(addFreeCourseReq)
        addFreeCourseReq.enqueue(object : CustomCallback<BaseResponse> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    addCourseToUserCourse(courseId)
                }
            }

            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>
            ) {
                if (response.body() != null) {
                    frag.onCourseAdded(response.body()!!)
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                super.onFailure(call, t)
                frag.onRequestFailed()
            }
        })
    }
}