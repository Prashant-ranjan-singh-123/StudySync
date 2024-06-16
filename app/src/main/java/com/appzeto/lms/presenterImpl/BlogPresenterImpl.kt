package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Blog
import com.appzeto.lms.model.Data
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.BlogsFrag
import retrofit2.Call
import retrofit2.Response

class BlogPresenterImpl(private val frag: BlogsFrag) : Presenter.BlogPresenter {

    override fun getBlogs() {
        val blogs = ApiService.apiClient!!.getBlogs()
        frag.addNetworkRequest(blogs)
        blogs.enqueue(object : CustomCallback<Data<List<Blog>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getBlogs()
                }
            }

            override fun onResponse(
                call: Call<Data<List<Blog>>>,
                response: Response<Data<List<Blog>>>
            ) {
                if (response.body() != null) {
                    frag.onBlogsRecevied(response.body()!!.data!!)
                }
            }
        })
    }

    override fun getBlogs(catId: Int) {
        val blogs = ApiService.apiClient!!.getBlogs(catId)
        frag.addNetworkRequest(blogs)
        blogs.enqueue(object : CustomCallback<Data<List<Blog>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getBlogs(catId)
                }
            }

            override fun onResponse(
                call: Call<Data<List<Blog>>>,
                response: Response<Data<List<Blog>>>
            ) {
                if (response.body() != null) {
                    frag.onBlogsRecevied(response.body()!!.data!!)
                }
            }
        })
    }
}