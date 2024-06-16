package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.QuizAnswer
import com.appzeto.lms.model.QuizResult
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.QuizFrag
import retrofit2.Call
import retrofit2.Response

class QuizPresenterImpl(private val frag: QuizFrag) : Presenter.QuizPresenter {

    override fun storeResult(quizId: Int, answer: QuizAnswer) {
        ApiService.apiClient!!.storeQuizResult(quizId, answer)
            .enqueue(object : CustomCallback<Data<Data<QuizResult>>> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        storeResult(quizId, answer)
                    }
                }

                override fun onResponse(
                    call: Call<Data<Data<QuizResult>>>,
                    response: Response<Data<Data<QuizResult>>>
                ) {
                    if (response.body() != null) {
                        frag.onQuizResultSaved(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<Data<Data<QuizResult>>>, t: Throwable) {
                    super.onFailure(call, t)
                    frag.onRequestFailed()
                }
            })
    }
}