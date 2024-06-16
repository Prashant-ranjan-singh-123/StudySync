package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Category
import com.appzeto.lms.model.Data
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.widget.BlogCategoriesDialog
import retrofit2.Call
import retrofit2.Response

class BlogCategoriesPresenterImpl(private val dialog: BlogCategoriesDialog) : Presenter.BlogCategoriesPresenter {
    override fun getBlogCategories() {
        ApiService.apiClient!!.getBlogCategories()
            .enqueue(object : CustomCallback<Data<List<Category>>> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        getBlogCategories()
                    }
                }

                override fun onResponse(
                    call: Call<Data<List<Category>>>,
                    response: Response<Data<List<Category>>>
                ) {
                    if (response.body() != null) {
                        dialog.onBlogCatsReceived(response.body()!!.data!!)
                    }
                }

            })
    }
}