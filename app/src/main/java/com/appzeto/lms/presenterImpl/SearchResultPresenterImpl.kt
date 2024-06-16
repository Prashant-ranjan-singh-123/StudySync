package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.SearchObject
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.SearchResultFrag
import retrofit2.Call
import retrofit2.Response

class SearchResultPresenterImpl(private val searchFrag: SearchResultFrag) :
    Presenter.SearchResultPresenter {

    override fun search(s: String) {
        val searchCoursesAndUsers = ApiService.apiClient!!.searchCoursesAndUsers(s)
        searchFrag.addNetworkRequest(searchCoursesAndUsers)
        searchCoursesAndUsers.enqueue(object : CustomCallback<Data<SearchObject>> {
            override fun onResponse(
                call: Call<Data<SearchObject>>,
                response: Response<Data<SearchObject>>
            ) {
                if (response.body() != null) {
                    searchFrag.onSearchResultReceived(response.body()!!)
                }
            }

            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    search(s)
                }
            }

        })
    }
}