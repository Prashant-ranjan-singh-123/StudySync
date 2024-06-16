package com.appzeto.lms.manager.listener

interface ListCallback<T> {
    fun onMapReceived(items: List<T>)
}