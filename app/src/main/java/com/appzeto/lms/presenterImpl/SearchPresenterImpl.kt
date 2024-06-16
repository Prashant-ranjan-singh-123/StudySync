package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Course
import com.appzeto.lms.model.Data
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.SearchFrag
import retrofit2.Call
import retrofit2.Response

class SearchPresenterImpl(private val frag: SearchFrag) : Presenter.SearchPresenter {

    override fun getBestRatedCourses() {
        val map = HashMap<String, String>()
        map["offset"] = "0"
        map["limit"] = "3"
        map["sort"] = "best_rates"

        val courses = ApiService.apiClient!!.getCourses(map)
        frag.addNetworkRequest(courses)
        courses.enqueue(object : CustomCallback<Data<List<Course>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getBestRatedCourses()
                }
            }

            override fun onResponse(
                call: Call<Data<List<Course>>>,
                response: Response<Data<List<Course>>>
            ) {
                if (response.body() != null) {
                    frag.onBestRatedCoursesRecevied(response.body()!!)
                }
            }
        })
    }
}