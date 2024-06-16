package com.appzeto.lms.model

import com.google.gson.annotations.SerializedName

class ForgetPassword {
    @SerializedName("email")
    lateinit var email: String
}