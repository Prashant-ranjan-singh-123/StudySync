package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.Follow
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.ProfileAboutFrag
import retrofit2.Call
import retrofit2.Response

class ProfileAboutPresenterImpl(private val frag: ProfileAboutFrag) :
    Presenter.ProfileAboutPresenter {

    override fun follow(userId: Int, follow: Follow) {
        ApiService.apiClient!!.followUser(userId, follow)
            .enqueue(object : CustomCallback<BaseResponse> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        follow(userId, follow)
                    }
                }

                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.body() != null) {
                        frag.onFollowed(follow)
                    }
                }
            })
    }
}