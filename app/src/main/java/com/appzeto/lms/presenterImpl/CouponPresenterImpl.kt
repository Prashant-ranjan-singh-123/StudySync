package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Coupon
import com.appzeto.lms.model.CouponValidation
import com.appzeto.lms.model.Data
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.widget.CouponDialog
import retrofit2.Call
import retrofit2.Response

class CouponPresenterImpl(private val dialog: CouponDialog) : Presenter.CouponPresenter {

    override fun validateCoupon(coupon: Coupon) {
        ApiService.apiClient!!.validateCoupon(coupon)
            .enqueue(object : CustomCallback<Data<CouponValidation>> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        validateCoupon(coupon)
                    }
                }

                override fun onResponse(
                    call: Call<Data<CouponValidation>>,
                    response: Response<Data<CouponValidation>>
                ) {
                    if (response.body() != null) {
                        dialog.onCouponValidated(response.body()!!)
                    }
                }

            })
    }


}