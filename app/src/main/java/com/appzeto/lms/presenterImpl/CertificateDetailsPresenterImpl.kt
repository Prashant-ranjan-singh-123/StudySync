package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.QuizResult
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.CertificateDetailsFrag
import retrofit2.Call
import retrofit2.Response

class CertificateDetailsPresenterImpl(private val frag: CertificateDetailsFrag) :
    Presenter.CertificateDetailsPresenter {

    override fun getStudents() {
        val certificateStudentsReq = ApiService.apiClient!!.getCertificateStudents()
        frag.addNetworkRequest(certificateStudentsReq)
        certificateStudentsReq.enqueue(object : CustomCallback<Data<List<QuizResult>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getStudents()
                }
            }

            override fun onResponse(
                call: Call<Data<List<QuizResult>>>,
                response: Response<Data<List<QuizResult>>>
            ) {
                if (response.body() != null) {
                    frag.onStudentsReceived(response.body()!!.data!!)
                }
            }

        })
    }
}