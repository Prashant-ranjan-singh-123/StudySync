package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.Forums
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.ForumsFrag
import retrofit2.Call
import retrofit2.Response

class ForumsPresenterImpl(private val frag: ForumsFrag) : Presenter.ForumsPresenter {

    override fun getForumQuestions(courseId: Int) {
        val courseForum = ApiService.apiClient!!.getCourseForum(courseId)
        frag.addNetworkRequest(courseForum)
        courseForum.enqueue(object : CustomCallback<Data<Forums>> {
            override fun onStateChanged(): RetryListener {
                return RetryListener {
                    getForumQuestions(courseId)
                }
            }

            override fun onResponse(
                call: Call<Data<Forums>>,
                response: Response<Data<Forums>>
            ) {
                if (response.body() != null) {
                    frag.onForumReceived(response.body()!!.data!!)
                }
            }
        })
    }

    override fun searchInCourseForum(courseId: Int, s: String) {
        val searchInCourseForum = ApiService.apiClient!!.searchInCourseForum(courseId, s)
        frag.addNetworkRequest(searchInCourseForum)
        searchInCourseForum.enqueue(object : CustomCallback<Data<Forums>> {
            override fun onStateChanged(): RetryListener {
                return RetryListener {
                    getForumQuestions(courseId)
                }
            }

            override fun onResponse(
                call: Call<Data<Forums>>,
                response: Response<Data<Forums>>
            ) {
                if (response.body() != null) {
                    frag.onForumReceived(response.body()!!.data!!)
                }
            }
        })

    }
}