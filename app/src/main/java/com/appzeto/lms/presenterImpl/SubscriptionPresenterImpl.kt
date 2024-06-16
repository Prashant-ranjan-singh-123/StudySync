package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.Subscription
import com.appzeto.lms.model.SubscriptionItem
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.SubscriptionFrag
import retrofit2.Call
import retrofit2.Response

class SubscriptionPresenterImpl(private val frag: SubscriptionFrag) :
    Presenter.SubscriptionPresenter {

    override fun getSubscriptions() {
        val subscriptionsReq = ApiService.apiClient!!.getSubscriptions()
        frag.addNetworkRequest(subscriptionsReq)
        subscriptionsReq.enqueue(object : CustomCallback<Data<Subscription>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getSubscriptions()
                }
            }

            override fun onResponse(
                call: Call<Data<Subscription>>,
                response: Response<Data<Subscription>>
            ) {
                if (response.body() != null) {
                    frag.onSubscriptionsReceived(response.body()!!.data!!)
                }
            }

        })
    }

    override fun checkoutSubscription(subscriptionItem: SubscriptionItem) {
        val checkoutSubscriptionReq = ApiService.apiClient!!.checkoutSubscription(subscriptionItem)
        frag.addNetworkRequest(checkoutSubscriptionReq)
        checkoutSubscriptionReq.enqueue(object :
            CustomCallback<Data<com.appzeto.lms.model.Response>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    checkoutSubscription(subscriptionItem)
                }
            }

            override fun onResponse(
                call: Call<Data<com.appzeto.lms.model.Response>>,
                response: Response<Data<com.appzeto.lms.model.Response>>
            ) {
                if (response.body() != null) {
                    frag.onCheckout(response.body()!!)
                }
            }

            override fun onFailure(
                call: Call<Data<com.appzeto.lms.model.Response>>,
                t: Throwable
            ) {
                super.onFailure(call, t)
                frag.onRequestFailed()
            }
        }

        )
    }
}