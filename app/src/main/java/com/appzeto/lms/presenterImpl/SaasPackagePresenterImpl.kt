package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.SaasPackage
import com.appzeto.lms.model.SaasPackageItem
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.SaasPackageFrag
import retrofit2.Call
import retrofit2.Response

class SaasPackagePresenterImpl(private val frag: SaasPackageFrag) : Presenter.SaasPackagePresenter {

    override fun getSaasPackages() {
        ApiService.apiClient!!.getSaasPackages()
            .enqueue(object : CustomCallback<Data<SaasPackage>> {
                override fun onStateChanged(): RetryListener {
                    return RetryListener {
                        getSaasPackages()
                    }
                }

                override fun onResponse(
                    call: Call<Data<SaasPackage>>,
                    res: Response<Data<SaasPackage>>
                ) {
                    if (res.body() != null) {
                        frag.onSaasPackageReceived(res.body()!!.data!!)
                    }
                }

            })
    }

    override fun checkoutSubscription(saasPackageItem: SaasPackageItem) {
        ApiService.apiClient!!.checkoutSaasPackage(saasPackageItem)
            .enqueue(object : CustomCallback<Data<com.appzeto.lms.model.Response>> {
                override fun onStateChanged(): RetryListener {
                    return RetryListener {
                        checkoutSubscription(saasPackageItem)
                    }
                }

                override fun onResponse(
                    call: Call<Data<com.appzeto.lms.model.Response>>,
                    res: Response<Data<com.appzeto.lms.model.Response>>
                ) {
                    if (res.body() != null) {
                        frag.onCheckout(res.body()!!)
                    }
                }

            })
    }
}