package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.Comment
import com.appzeto.lms.model.Data
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.widget.CommentDialog
import retrofit2.Call
import retrofit2.Response

class ReportReplyCommentPresenterImpl(private val dialog: CommentDialog) :

    Presenter.ReportReplyCommentPresenter {
    override fun comment(comment: Comment) {
        val commentReq = ApiService.apiClient!!.comment(comment)
        dialog.addNetworkRequest(commentReq)
        commentReq.enqueue(object : CustomCallback<BaseResponse> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    comment(comment)
                }
            }

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.body() != null) {
                    dialog.onRsp(response.body()!!, comment)
                }
            }
        })
    }

    override fun reply(comment: Comment) {
        val replyReq = ApiService.apiClient!!.reply(comment.id, comment)
        dialog.addNetworkRequest(replyReq)
        replyReq.enqueue(object : CustomCallback<BaseResponse> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    reply(comment)
                }
            }

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.body() != null) {
                    dialog.onRsp(response.body()!!, comment)
                }
            }
        })
    }

    override fun reportComment(comment: Comment) {
        val reportCommentReq = ApiService.apiClient!!.reportComment(comment.id, comment)
        dialog.addNetworkRequest(reportCommentReq)
        reportCommentReq.enqueue(object : CustomCallback<BaseResponse> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    reportComment(comment)
                }
            }

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.body() != null) {
                    dialog.onRsp(response.body()!!, comment)
                }
            }
        })
    }

    override fun editComment(comment: Comment) {
        val editCommentReq = ApiService.apiClient!!.editComment(comment.id, comment)
        dialog.addNetworkRequest(editCommentReq)
        editCommentReq.enqueue(object : CustomCallback<BaseResponse> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    editComment(comment)
                }
            }

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.body() != null) {
                    dialog.onRsp(response.body()!!, comment)
                }
            }
        })
    }

    override fun reportCourse(comment: Comment) {
        val reportCourseReq = ApiService.apiClient!!.reportCourse(comment.id, comment)
        dialog.addNetworkRequest(reportCourseReq)
        reportCourseReq.enqueue(object : CustomCallback<BaseResponse> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    reportCourse(comment)
                }
            }

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.body() != null) {
                    dialog.onRsp(response.body()!!, comment)
                }
            }
        })
    }

    override fun getReasons() {
        ApiService.apiClient!!.getReportReasons()
            .enqueue(object : CustomCallback<Data<Map<String, String>>> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        getReasons()
                    }
                }

                override fun onResponse(
                    call: Call<Data<Map<String, String>>>,
                    response: Response<Data<Map<String, String>>>
                ) {
                    if (response.body() != null) {
                        dialog.onReasonsReceived(response.body()!!.data!!.values)
                    }
                }
            })
    }
}