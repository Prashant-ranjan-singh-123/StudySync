package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.SettingsFrag
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class SettingsPresenterImpl(private val frag: SettingsFrag) :
    Presenter.SettingsPresenter {

    override fun uploadPhoto(path: String) {
        val file = File(path)
        val fileBody = file.asRequestBody()
        val filePart: MultipartBody.Part =
            MultipartBody.Part.createFormData("profile_image", file.name, fileBody)
        ApiService.apiClient!!.changeProfileImage(filePart)
            .enqueue(object : CustomCallback<BaseResponse> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        uploadPhoto(path)
                    }
                }

                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.body() != null) {
                        frag.onProfileSaved(response.body()!!)
                    }
                }

            })
    }
}