package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Assignment
import com.appzeto.lms.model.Data
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.AssignmentOverviewFrag
import retrofit2.Call
import retrofit2.Response

class AssignmentOverviewPresenterImpl(val frag: AssignmentOverviewFrag) :
    Presenter.AssignmentOverviewPresenter {

    override fun getAssignmentStudents(assignmentId: Int) {
        val assignmentStudents = ApiService.apiClient!!.getAssignmentStudents(assignmentId)
        frag.addNetworkRequest(assignmentStudents)
        assignmentStudents.enqueue(object : CustomCallback<Data<List<Assignment>>> {
            override fun onStateChanged(): RetryListener {
                return RetryListener {
                    getAssignmentStudents(assignmentId)
                }
            }

            override fun onResponse(
                call: Call<Data<List<Assignment>>>,
                response: Response<Data<List<Assignment>>>
            ) {
                if (response.body() != null) {
                    frag.onStudentsReceived(response.body()!!.data!!)
                }
            }
        })
    }

    override fun getAssignment(assignmentId: Int) {
        val assignment = ApiService.apiClient!!.getAssignment(assignmentId)
        frag.addNetworkRequest(assignment)
        assignment.enqueue(object : CustomCallback<Data<Assignment>> {
            override fun onStateChanged(): RetryListener {
                return RetryListener {
                    getAssignmentStudents(assignmentId)
                }
            }

            override fun onResponse(
                call: Call<Data<Assignment>>,
                response: Response<Data<Assignment>>
            ) {
                if (response.body() != null) {
                    frag.onAssignmentReceived(response.body()!!.data!!)
                }
            }
        })
    }

}