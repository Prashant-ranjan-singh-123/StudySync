package com.appzeto.lms.manager

import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.Response
import com.appzeto.lms.model.ThirdPartyLogin

interface ThirdPartyCallback {
    fun onThirdPartyLogin(res: Data<Response>, provider: Int, thirdPartyLogin: ThirdPartyLogin)
    fun onErrorOccured(error: BaseResponse)
}