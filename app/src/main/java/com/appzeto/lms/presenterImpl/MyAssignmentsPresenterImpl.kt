package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Assignment
import com.appzeto.lms.model.Data
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.MyAssignmentsFrag
import retrofit2.Call
import retrofit2.Response

class MyAssignmentsPresenterImpl(private val frag: MyAssignmentsFrag) :
    Presenter.MyAssignmentsPresenter {

    override fun getMyAssignments() {
        val myAssignments = ApiService.apiClient!!.getMyAssignments()
        frag.addNetworkRequest(myAssignments)
        myAssignments.enqueue(object : CustomCallback<Data<Data<List<Assignment>>>> {
            override fun onStateChanged(): RetryListener {
                return RetryListener {
                    getMyAssignments()
                }
            }

            override fun onResponse(
                call: Call<Data<Data<List<Assignment>>>>,
                response: Response<Data<Data<List<Assignment>>>>
            ) {
                if (response.body() != null) {
                    frag.onAssignmentsReceived(response.body()!!.data!!.data!!)
                }
            }

        })
    }

}