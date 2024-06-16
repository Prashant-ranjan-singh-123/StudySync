package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.AddToFav
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.widget.ClassDetailsMoreDialog
import retrofit2.Call
import retrofit2.Response

class CourseDetailsMorePresenterImpl(private val dialog: ClassDetailsMoreDialog) :
    Presenter.CourseDetailsMorePresenter {

    override fun addToFavorite(addToFav: AddToFav) {
        ApiService.apiClient!!.addRemoveFromFavorite(addToFav)
            .enqueue(object : CustomCallback<BaseResponse> {
                override fun onStateChanged(): RetryListener {
                    return RetryListener {
                        addToFavorite(addToFav)
                    }
                }

                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.body() != null) {
                        dialog.onItemAddedToFavorites(response.body()!!)
                    }
                }

            })

    }

}