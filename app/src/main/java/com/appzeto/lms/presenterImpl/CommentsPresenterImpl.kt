package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.listener.ItemCallback
import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Comments
import com.appzeto.lms.model.Data
import com.appzeto.lms.presenter.Presenter
import retrofit2.Call
import retrofit2.Response

class CommentsPresenterImpl() : Presenter.CommentsPresenter {

    override fun getComments(callback: ItemCallback<Comments>) {
        ApiService.apiClient!!.getComments().enqueue(object : CustomCallback<Data<Comments>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getComments(callback)
                }
            }

            override fun onResponse(
                call: Call<Data<Comments>>,
                response: Response<Data<Comments>>
            ) {
                if (response.body() != null) {
                    callback.onItem(response.body()!!.data!!)
                }
            }
        })
    }
}