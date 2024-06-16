package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.ForumItemAnswer
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.ForumAnswersFrag
import retrofit2.Call
import retrofit2.Response

class ForumAnswersPresenterImpl(val frag: ForumAnswersFrag) : Presenter.ForumAnswersPresenter {

    override fun getForumQuestionAnswers(forumId: Int) {
        val forumQuestionAnswers = ApiService.apiClient!!.getForumQuestionAnswers(forumId)
        frag.addNetworkRequest(forumQuestionAnswers)
        forumQuestionAnswers.enqueue(object : CustomCallback<Data<Data<List<ForumItemAnswer>>>> {
            override fun onStateChanged(): RetryListener {
                return RetryListener {
                    getForumQuestionAnswers(forumId)
                }
            }

            override fun onResponse(
                call: Call<Data<Data<List<ForumItemAnswer>>>>,
                response: Response<Data<Data<List<ForumItemAnswer>>>>
            ) {
                if (response.body() != null) {
                    frag.onAnswersReceived(response.body()!!.data!!.data!!)
                }
            }
        })
    }
}