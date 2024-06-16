package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.Count
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.FinancialSettings
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.SettingsFinancialFrag
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class SettingsFinancialPresenterImpl(private val frag: SettingsFinancialFrag) :
    Presenter.SettingsFinancialPresenter {

    override fun uploadFinancialSettings(financialSettings: FinancialSettings) {
        ApiService.apiClient!!.changeProfileSettings(financialSettings)
            .enqueue(object : CustomCallback<BaseResponse> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        uploadFinancialSettings(financialSettings)
                    }
                }

                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.body() != null) {
                        frag.onSettingsSaved(response.body()!!, financialSettings)
                    }
                }

            })
    }

    override fun getAccountTypes() {
        ApiService.apiClient!!.getAccountTypes().enqueue(object :
            CustomCallback<Data<Count<String>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getAccountTypes()
                }
            }

            override fun onResponse(
                call: Call<Data<Count<String>>>,
                response: Response<Data<Count<String>>>
            ) {
                if (response.body() != null) {
                    frag.onAccountTypesReceived(response.body()!!.data!!)
                }
            }

        })
    }

    override fun uploadFinancialSettingsFiles(identityFile: File, certFile: File) {
        val identityFileBody = identityFile.asRequestBody()
        val identityFilePart =
            MultipartBody.Part.createFormData("identity_scan", identityFile.name, identityFileBody)

        val certFileBody = certFile.asRequestBody()
        val certFilePart =
            MultipartBody.Part.createFormData("certificate", certFile.name, certFileBody)

        ApiService.apiClient!!.uploadFinanicalSettings(identityFilePart, certFilePart)
            .enqueue(object : CustomCallback<BaseResponse> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        uploadFinancialSettingsFiles(identityFile, certFile)
                    }
                }

                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.body() != null) {
                        frag.onFilesSaved(response.body()!!)
                    }
                }

            })
    }
}