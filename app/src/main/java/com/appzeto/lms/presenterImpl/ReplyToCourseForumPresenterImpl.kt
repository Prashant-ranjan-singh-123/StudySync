package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.ForumItem
import com.appzeto.lms.model.Reply
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.widget.ForumReplyDialog
import retrofit2.Call
import retrofit2.Response

class ReplyToCourseForumPresenterImpl(val dialog: ForumReplyDialog) :
    Presenter.ReplyToCourseForumPresenter {

    override fun reply(forum: ForumItem, reply: Reply) {
        if (forum.isAnswer()) {
            editReply(forum.id, reply)
        } else {
            replyToQuestion(forum.id, reply)
        }
    }

    private fun editReply(id: Int, reply: Reply) {
        ApiService.apiClient!!.editReplyToForumQuestion(id, reply)
            .enqueue(getCallback { editReply(id, reply) })
    }

    private fun replyToQuestion(id: Int, reply: Reply) {
        ApiService.apiClient!!.replyToForumQuestion(id, reply)
            .enqueue(getCallback { replyToQuestion(id, reply) })
    }

    fun getCallback(retryListener: RetryListener): CustomCallback<BaseResponse> {
        return object : CustomCallback<BaseResponse> {
            override fun onStateChanged(): RetryListener {
                return retryListener
            }

            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>
            ) {
                if (response.body() != null) {
                    dialog.onRsp(response.body()!!)
                }
            }
        }
    }

}