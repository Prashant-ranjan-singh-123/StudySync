package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Category
import com.appzeto.lms.model.Count
import com.appzeto.lms.model.Data
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.widget.ProvidersFiltersDialog
import retrofit2.Call
import retrofit2.Response

class ProvidersFiltersPresenterImpl(private val dialog: ProvidersFiltersDialog) :
    Presenter.ProvidersFiltersPresenter {

    override fun getCategories() {
        val categories = ApiService.apiClient!!.getCategories()
        dialog.addNetworkRequest(categories)
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
                    dialog.onCategoriesRecevied(response.body()!!)
            }

        })
    }
}