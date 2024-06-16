package com.appzeto.lms.manager.listener

interface MapCallback<T, U> {
    fun onMapReceived(map: Map<T, U>)
}