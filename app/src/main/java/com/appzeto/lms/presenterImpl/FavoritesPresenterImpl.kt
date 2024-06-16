package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.*
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.FavoritesFrag
import retrofit2.Call
import retrofit2.Response

class FavoritesPresenterImpl(private val frag: FavoritesFrag) : Presenter.FavoritesPresenter {

    override fun getFavorites() {
        val favoritesReq = ApiService.apiClient!!.getFavorites()
        frag.addNetworkRequest(favoritesReq)
        favoritesReq.enqueue(object : CustomCallback<Data<Count<Favorite>>> {
                override fun onStateChanged(): RetryListener {
                    return RetryListener {
                        getFavorites()
                    }
                }

                override fun onResponse(
                    call: Call<Data<Count<Favorite>>>,
                    response: Response<Data<Count<Favorite>>>
                ) {
                    if (response.body() != null) {
                        frag.onFavoritesReceived(response.body()!!.data!!.items)
                    }
                }

            })
    }

    override fun removeFromFavorite(addToFav: AddToFav, adapterPosition: Int) {
        ApiService.apiClient!!.addRemoveFromFavorite(addToFav)
            .enqueue(object : CustomCallback<BaseResponse> {
                override fun onStateChanged(): RetryListener? {
                    return RetryListener {
                        removeFromFavorite(addToFav, adapterPosition)
                    }
                }

                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.body() != null) {
                        frag.onItemRemoved(response.body()!!, adapterPosition)
                    }
                }

            })
    }
}