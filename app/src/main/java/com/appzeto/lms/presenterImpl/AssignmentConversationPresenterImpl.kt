package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Assignment
import com.appzeto.lms.model.Conversation
import com.appzeto.lms.model.Data
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.AssignmentConversationFrag
import retrofit2.Call
import retrofit2.Response

class AssignmentConversationPresenterImpl(private val frag: AssignmentConversationFrag) :
    Presenter.AssignmentConversationPresenter {

    override fun getConversations(assignment: Assignment, studentId: Int?) {
        val assignmentConversations =
            ApiService.apiClient!!.getAssignmentConversations(assignment.id, studentId)
        frag.addNetworkRequest(assignmentConversations)
        assignmentConversations.enqueue(object : CustomCallback<Data<List<Conversation>>> {
            override fun onStateChanged(): RetryListener {
                return RetryListener {
                    getConversations(assignment, studentId)
                }
            }

            override fun onResponse(
                call: Call<Data<List<Conversation>>>,
                response: Response<Data<List<Conversation>>>
            ) {
                if (response.body() != null) {
                    frag.onConversationsReceived(response.body()!!.data!!)
                }
            }
        })
    }

}