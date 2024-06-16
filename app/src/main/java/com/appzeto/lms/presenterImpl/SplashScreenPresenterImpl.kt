package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.App
import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.AppConfig
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.SplashScreenActivity
import retrofit2.Call
import retrofit2.Response

class SplashScreenPresenterImpl(private val activity: SplashScreenActivity) :
    Presenter.SplashScreenPresenter {

    override fun getAppConfig() {
        val customerSettings = ApiService.apiClient?.getAppConfig()
        activity.addNetworkRequest(customerSettings)
        customerSettings?.enqueue(object : CustomCallback<AppConfig> {
            override fun onResponse(call: Call<AppConfig>, response: Response<AppConfig>) {
                if (response.body() != null) {
                    App.appConfig = response.body()!!
                    activity.onAppConfigReceived()
                }
            }

            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getAppConfig()
                }
            }

            override fun onFailure(call: Call<AppConfig>, t: Throwable) {
                activity.showNoNetFrag()
            }

        })
    }
}