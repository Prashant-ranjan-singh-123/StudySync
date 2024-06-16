package com.appzeto.lms.ui.frag.course

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.appzeto.lms.manager.adapter.ViewPagerAdapter
import com.appzeto.lms.manager.listener.ItemCallback
import com.appzeto.lms.model.AddToCart
import com.appzeto.lms.model.Course
import com.appzeto.lms.model.Review

abstract class BaseCourseDetails {
    abstract fun getToolbarTitle(): Int
    abstract fun getTabsAdapter(
        context: Context,
        fragmentManager: FragmentManager,
    ): ViewPagerAdapter
    abstract fun getCourseDetails(
        id: Int,
        callback: ItemCallback<Course>
    )
    abstract fun getAddToCartItem() : AddToCart
    abstract fun getBaseReviewObj() : Review
    abstract fun getCourseType(): String
}