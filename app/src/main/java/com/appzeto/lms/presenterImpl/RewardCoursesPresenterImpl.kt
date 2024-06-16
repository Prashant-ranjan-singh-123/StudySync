package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.Utils.toInt
import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Course
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.KeyValuePair
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.RewardCoursesFrag
import retrofit2.Call
import retrofit2.Response

class RewardCoursesPresenterImpl(private val frag: RewardCoursesFrag) :
    Presenter.RewardCoursesPresenter {

    override fun getRewardCourses(categories: List<KeyValuePair>?, options: List<KeyValuePair>?) {
        val filter = HashMap<String, String>()
        filter["reward"] = true.toInt().toString()

        if (!categories.isNullOrEmpty()){
            filter[categories[0].key] = categories[0].value
        }

        if (!options.isNullOrEmpty()) {
            for (option in options) {
                filter[option.key] = option.value
            }
        }

        ApiService.apiClient!!.getCourses(filter).enqueue(object : CustomCallback<Data<List<Course>>>{
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getRewardCourses(categories, options)
                }
            }

            override fun onResponse(
                call: Call<Data<List<Course>>>,
                res: Response<Data<List<Course>>>
            ) {
                if (res.body() != null) {
                    frag.onResultReceived(res.body()!!.data!!)
                }
            }

        })
    }
}