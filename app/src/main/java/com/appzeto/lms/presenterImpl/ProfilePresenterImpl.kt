package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.UserProfile
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.ProfileFrag
import retrofit2.Call
import retrofit2.Response

class ProfilePresenterImpl(private val frag: ProfileFrag) : Presenter.ProfilePresenter {

    override fun getUserProfile(userId: Int) {
        val userProfileReq = ApiService.apiClient!!.getUserProfile(userId)
        frag.addNetworkRequest(userProfileReq)
        userProfileReq.enqueue(object : CustomCallback<Data<Data<UserProfile>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getUserProfile(userId)
                }
            }

            override fun onResponse(
                call: Call<Data<Data<UserProfile>>>,
                response: Response<Data<Data<UserProfile>>>
            ) {
                if (response.body() != null) {
                    frag.onUserProfileReceived(response.body()!!.data!!)
                }
            }

        })
    }
}