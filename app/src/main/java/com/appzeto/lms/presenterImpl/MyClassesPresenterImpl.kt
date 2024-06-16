package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Count
import com.appzeto.lms.model.Course
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.MyClasses
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.MyClassesFrag
import retrofit2.Call
import retrofit2.Response

class MyClassesPresenterImpl(private val frag: MyClassesFrag) : Presenter.MyClassesPresenter {

    override fun getMyClasses() {
        val myClassesPageDataReq = ApiService.apiClient!!.getMyClassesPageData()
        frag.addNetworkRequest(myClassesPageDataReq)
        myClassesPageDataReq.enqueue(object : CustomCallback<MyClasses> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getMyClasses()
                }
            }

            override fun onResponse(call: Call<MyClasses>, response: Response<MyClasses>) {
                if (response.body() != null) {
                    frag.onMyClassesReceived(response.body()!!)
                }
            }

        })
    }

    override fun getPurchased() {
        val myPurchasesReq = ApiService.apiClient!!.getMyPurchases()
        frag.addNetworkRequest(myPurchasesReq)
        myPurchasesReq.enqueue(object : CustomCallback<Data<Count<Course>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getPurchased()
                }
            }

            override fun onResponse(
                call: Call<Data<Count<Course>>>,
                response: Response<Data<Count<Course>>>
            ) {

                if (response.body() != null) {
                    frag.onPurchasedReceived(response.body()!!.data!!.items, true)
                }
            }

        })
    }

    override fun getOrganizations() {
        val myPurchasesReq = ApiService.apiClient!!.getCoursesOfOrganizations()
        frag.addNetworkRequest(myPurchasesReq)
        myPurchasesReq.enqueue(object : CustomCallback<Data<Count<Course>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getPurchased()
                }
            }

            override fun onResponse(
                call: Call<Data<Count<Course>>>,
                response: Response<Data<Count<Course>>>
            ) {

                if (response.body() != null) {
                    frag.onPurchasedReceived(response.body()!!.data!!.items, false)
                }
            }

        })
    }

}