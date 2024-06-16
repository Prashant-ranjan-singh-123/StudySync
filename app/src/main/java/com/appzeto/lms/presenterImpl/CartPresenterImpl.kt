package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.*
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.CartFrag
import retrofit2.Call
import retrofit2.Response

class CartPresenterImpl(private val frag: CartFrag) : Presenter.CartPresenter {

    override fun getCart() {
        val cartReq = ApiService.apiClient!!.getCart()
        frag.addNetworkRequest(cartReq)
        cartReq.enqueue(object : CustomCallback<Data<Data<Cart?>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getCart()
                }
            }

            override fun onResponse(
                call: Call<Data<Data<Cart?>>>,
                response: Response<Data<Data<Cart?>>>
            ) {
                if (response.body() != null) {
                    frag.onCartReceived(response.body()!!.data!!.data)
                }
            }

            override fun onFailure(call: Call<Data<Data<Cart?>>>, t: Throwable) {
                super.onFailure(call, t)
                frag.onRequestFailed()
            }

        })
    }

    override fun removeFromCart(cartItemId: Int, position: Int) {
        ApiService.apiClient!!.removeFromCart(cartItemId)
            .enqueue(object : CustomCallback<BaseResponse> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        removeFromCart(cartItemId, position)
                    }
                }

                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.body() != null) {
                        frag.onCartRemoved(response.body()!!, position)
                    }
                }

            })
    }

    override fun checkout(coupon: Coupon?) {
        val cp = coupon ?: Coupon()
        val checkoutReq = ApiService.apiClient!!.checkout(cp)
        frag.addNetworkRequest(checkoutReq)
        checkoutReq.enqueue(object : CustomCallback<Data<com.appzeto.lms.model.Response>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    checkout(coupon)
                }
            }

            override fun onResponse(call: Call<Data<com.appzeto.lms.model.Response>>, response: Response<Data<com.appzeto.lms.model.Response>>) {
                if (response.body() != null) {
                    frag.onCheckout(response.body()!!)
                }
            }

            override fun onFailure(call: Call<Data<com.appzeto.lms.model.Response>>, t: Throwable) {
                super.onFailure(call, t)
                frag.onRequestFailed()
            }

        })
    }
}