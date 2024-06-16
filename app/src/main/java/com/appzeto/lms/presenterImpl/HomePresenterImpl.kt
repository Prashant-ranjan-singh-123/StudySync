package com.appzeto.lms.presenterImpl

import com.appzeto.lms.manager.net.ApiService
import com.appzeto.lms.manager.net.CustomCallback
import com.appzeto.lms.manager.net.RetryListener
import com.appzeto.lms.model.Course
import com.appzeto.lms.model.Data
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.ui.frag.HomeFrag
import retrofit2.Call
import retrofit2.Response

class HomePresenterImpl(private val frag: HomeFrag) : Presenter.HomePresenter {

    override fun getFeaturedCourses() {
        val featuredCourses = ApiService.apiClient!!.getFeaturedCourses()
        frag.addNetworkRequest(featuredCourses)
        featuredCourses.enqueue(object : CustomCallback<Data<List<Course>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getFeaturedCourses()
                }
            }

            override fun onResponse(
                call: Call<Data<List<Course>>>,
                response: Response<Data<List<Course>>>
            ) {
                if (response.body() != null) {
                    frag.onFeaturedCoursesReceived(response.body()!!.data!!)
                }
            }
        })
    }

    override fun getNewestCourses(map: HashMap<String, String>) {
        val courses = ApiService.apiClient!!.getCourses(map)
        frag.addNetworkRequest(courses)
        courses.enqueue(object : CustomCallback<Data<List<Course>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getNewestCourses(map)
                }
            }

            override fun onResponse(
                call: Call<Data<List<Course>>>,
                response: Response<Data<List<Course>>>
            ) {
                if (response.body() != null) {
                    frag.onNewestCoursesRecevied(response.body()!!)
                }
            }
        })
    }

    override fun getBestRatedCourses(map: HashMap<String, String>) {
        val courses = ApiService.apiClient!!.getCourses(map)
        frag.addNetworkRequest(courses)
        courses.enqueue(object : CustomCallback<Data<List<Course>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getBestRatedCourses(map)
                }
            }

            override fun onResponse(
                call: Call<Data<List<Course>>>,
                response: Response<Data<List<Course>>>
            ) {
                if (response.body() != null) {
                    frag.onBestRatedCoursesRecevied(response.body()!!)
                }
            }
        })
    }

    override fun getBestSellingCourses(map: HashMap<String, String>) {
        val courses = ApiService.apiClient!!.getCourses(map)
        frag.addNetworkRequest(courses)
        courses.enqueue(object : CustomCallback<Data<List<Course>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getBestSellingCourses(map)
                }
            }

            override fun onResponse(
                call: Call<Data<List<Course>>>,
                response: Response<Data<List<Course>>>
            ) {
                if (response.body() != null) {
                    frag.onBestSellersCoursesRecevied(response.body()!!)
                }
            }
        })
    }

    override fun getDiscountedCourses(map: HashMap<String, String>) {
        val courses = ApiService.apiClient!!.getCourses(map)
        frag.addNetworkRequest(courses)
        courses.enqueue(object : CustomCallback<Data<List<Course>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getDiscountedCourses(map)
                }
            }

            override fun onResponse(
                call: Call<Data<List<Course>>>,
                response: Response<Data<List<Course>>>
            ) {
                if (response.body() != null) {
                    frag.onDiscountedCoursesRecevied(response.body()!!)
                }
            }
        })
    }

    override fun getFreeCourses(map: HashMap<String, String>) {
        val courses = ApiService.apiClient!!.getCourses(map)
        frag.addNetworkRequest(courses)
        courses.enqueue(object : CustomCallback<Data<List<Course>>> {
            override fun onStateChanged(): RetryListener? {
                return RetryListener {
                    getFreeCourses(map)
                }
            }

            override fun onResponse(
                call: Call<Data<List<Course>>>,
                response: Response<Data<List<Course>>>
            ) {
                if (response.body() != null) {
                    frag.onFreeCoursesRecevied(response.body()!!)
                }
            }
        })
    }

    override fun getBundles() {
        val courses = ApiService.apiClient!!.getBundleClasses()
        frag.addNetworkRequest(courses)
        courses.enqueue(object : CustomCallback<Data<Data<List<Course>>>> {
            override fun onStateChanged(): RetryListener {
                return RetryListener {
                    getBundles()
                }
            }

            override fun onResponse(
                call: Call<Data<Data<List<Course>>>>,
                response: Response<Data<Data<List<Course>>>>
            ) {
                if (response.body() != null) {
                    frag.onBundleCoursesReceived(response.body()!!.data!!.data!!)
                }
            }
        } )
    }
}