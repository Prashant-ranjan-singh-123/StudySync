package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Count
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.Quiz
import com.appzeto.lms.model.QuizResult
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.QuizzesFrag
import retrofit2.Call
import retrofit2.Response

class QuzziesPresenterImpl(private val frag: QuizzesFrag) : Presenter.QuzziesPresenter {

    override fun getMyResults() {
        val myQuizzesResultReq = ApiService.apiClient!!.getMyQuizzesResult()
        frag.addNetworkRequest(myQuizzesResultReq)
        myQuizzesResultReq
            .enqueue(object : CustomCallback<Data<Count<QuizResult>>> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        getMyResults()
                    }
                }

                override fun onResponse(
                    call: Call<Data<Count<QuizResult>>>,
                    response: Response<Data<Count<QuizResult>>>
                ) {
                    if (response.body() != null) {
                        frag.onQuizzesResultRecevied(response.body()!!.data!!.items)
                    }
                }

            })
    }

    override fun getNotParticipated() {
        val notParticipatedQuizzesReq = ApiService.apiClient!!.getNotParticipatedQuizzes()
        frag.addNetworkRequest(notParticipatedQuizzesReq)
        notParticipatedQuizzesReq.enqueue(object : CustomCallback<Data<Count<Quiz>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getNotParticipated()
                }
            }

            override fun onResponse(
                call: Call<Data<Count<Quiz>>>,
                response: Response<Data<Count<Quiz>>>
            ) {
                if (response.body() != null) {
                    frag.onQuizListRecevied(response.body()!!.data!!.items)
                }
            }

        })
    }

    override fun getQuizList() {
        val quizzesListReq = ApiService.apiClient!!.getQuizzesList()
        frag.addNetworkRequest(quizzesListReq)
        quizzesListReq.enqueue(object : CustomCallback<Data<Count<Quiz>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getQuizList()
                }
            }

            override fun onResponse(
                call: Call<Data<Count<Quiz>>>,
                response: Response<Data<Count<Quiz>>>
            ) {
                if (response.body() != null) {
                    frag.onQuizListRecevied(response.body()!!.data!!.items)
                }
            }

        })
    }

    override fun getStudentResults() {
        val studentResultsReq = ApiService.apiClient!!.getStudentResults()
        frag.addNetworkRequest(studentResultsReq)
        studentResultsReq.enqueue(object : CustomCallback<Data<Count<QuizResult>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getStudentResults()
                }
            }

            override fun onResponse(
                call: Call<Data<Count<QuizResult>>>,
                response: Response<Data<Count<QuizResult>>>
            ) {
                if (response.body() != null) {
                    frag.onStudentResultRecevied(response.body()!!.data!!.items)
                }
            }
        })
    }
}