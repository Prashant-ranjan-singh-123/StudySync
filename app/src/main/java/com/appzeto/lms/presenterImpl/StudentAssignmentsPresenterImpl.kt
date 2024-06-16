package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.StudentAssignments
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.StudentAssignmentsFrag
import retrofit2.Call
import retrofit2.Response

class StudentAssignmentsPresenterImpl(private val frag: StudentAssignmentsFrag) :
    Presenter.StudentAssignmentsPresenter {

    override fun getStudentAssignments() {
        val studentAssignments = ApiService.apiClient!!.getStudentAssignments()
        frag.addNetworkRequest(studentAssignments)
        studentAssignments.enqueue(object : CustomCallback<Data<StudentAssignments>> {
            override fun onStateChanged(): RetryListener {
                return RetryListener {
                    getStudentAssignments()
                }
            }

            override fun onResponse(
                call: Call<Data<StudentAssignments>>,
                response: Response<Data<StudentAssignments>>
            ) {
                if (response.body() != null) {
                    frag.onAssignmentsReceived(response.body()!!.data!!)
                }
            }
        })
    }
}