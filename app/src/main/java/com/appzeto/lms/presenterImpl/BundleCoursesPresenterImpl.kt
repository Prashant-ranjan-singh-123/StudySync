package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Course
import com.appzeto.lms.model.Data
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.BundleCoursesFrag
import retrofit2.Call
import retrofit2.Response

class BundleCoursesPresenterImpl(private val frag: BundleCoursesFrag) :
    Presenter.BundleCoursesPresenter {

    override fun getBundleCourses(id: Int) {
        val coursesForBundle = ApiService.apiClient!!.getCoursesForBundle(id)
        frag.addNetworkRequest(coursesForBundle)
        coursesForBundle.enqueue(object : CustomCallback<Data<Data<List<Course>>>> {
            override fun onStateChanged(): RetryListener {
                return RetryListener {
                    getBundleCourses(id)
                }
            }

            override fun onResponse(
                call: Call<Data<Data<List<Course>>>>,
                response: Response<Data<Data<List<Course>>>>
            ) {
                if (response.body() != null) {
                    frag.onCoursesReceived(response.body()!!.data!!.data!!)
                }
            }
        })
    }
}