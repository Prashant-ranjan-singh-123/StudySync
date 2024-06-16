package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.QuizResult
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.QuizOverviewFrag
import retrofit2.Call
import retrofit2.Response

class QuizOverviewPresenterImpl(private val frag: QuizOverviewFrag) :
    Presenter.QuizOverviewPresenter {

    override fun startQuiz(id: Int) {
        val startQuizReq = ApiService.apiClient!!.startQuiz(id)
        frag.addNetworkRequest(startQuizReq)
        startQuizReq.enqueue(object : CustomCallback<Data<QuizResult>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    startQuiz(id)
                }
            }

            override fun onResponse(
                call: Call<Data<QuizResult>>,
                response: Response<Data<QuizResult>>
            ) {
                if (response.body() != null && response.body()!!.isSuccessful) {
                    frag.onQuizStartBegin(response.body()!!)
                } else {
                    frag.cannotStartQuiz(response.body()!!)
                }
            }

            override fun onFailure(call: Call<Data<QuizResult>>, t: Throwable) {
                super.onFailure(call, t)
                frag.cannotStartQuiz(null)
            }

        })
    }
}