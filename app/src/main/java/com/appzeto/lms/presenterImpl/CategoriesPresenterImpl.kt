package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Category
import com.appzeto.lms.model.Count
import com.appzeto.lms.model.Data
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.CategoriesFrag
import retrofit2.Call
import retrofit2.Response

open class CategoriesPresenterImpl(private val frag: CategoriesFrag) : Presenter.CategoriesPresenter {

    override fun getTrendingCategories() {
        val trendingCategories = ApiService.apiClient!!.getTrendingCategories()
        frag.addNetworkRequest(trendingCategories)
        trendingCategories
            .enqueue(object : CustomCallback<Data<Count<Category>>> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        getTrendingCategories()
                    }
                }

                override fun onResponse(
                    call: Call<Data<Count<Category>>>,
                    response: Response<Data<Count<Category>>>
                ) {
                    if (response.body() != null)
                        frag.onTrendingCategoriesRecevied(response.body()!!)
                }

            })
    }

    override fun getCategories() {
        val categories = ApiService.apiClient!!.getCategories()
        frag.addNetworkRequest(categories)
        categories.enqueue(object : CustomCallback<Data<Count<Category>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getCategories()
                }
            }

            override fun onResponse(
                call: Call<Data<Count<Category>>>,
                response: Response<Data<Count<Category>>>
            ) {
                if (response.body() != null)
                    frag.onCategoriesRecevied(response.body()!!)
            }

        })
    }

}