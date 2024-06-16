package com.appzeto.lms.ui.frag.course

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.appzeto.lms.R
import com.appzeto.lms.manager.App
import com.appzeto.lms.manager.adapter.ViewPagerAdapter
import com.appzeto.lms.manager.listener.ItemCallback
import com.appzeto.lms.model.AddToCart
import com.appzeto.lms.model.Course
import com.appzeto.lms.model.Review
import com.appzeto.lms.presenterImpl.CommonApiPresenterImpl
import com.appzeto.lms.ui.frag.CourseDetailsCommentsFrag
import com.appzeto.lms.ui.frag.CourseDetailsContentFrag
import com.appzeto.lms.ui.frag.CourseDetailsInformationFrag
import com.appzeto.lms.ui.frag.CourseDetailsReviewsFrag

class NormalCourseDetails(private val course: Course) : BaseCourseDetails() {

    override fun getToolbarTitle(): Int {
        return R.string.course_details
    }

    override fun getTabsAdapter(
        context: Context,
        fragmentManager: FragmentManager,
    ): ViewPagerAdapter {

        val bundle = Bundle()
        bundle.putParcelable(App.COURSE, course)

        val informationFrag = CourseDetailsInformationFrag()
        val contentFrag = CourseDetailsContentFrag()
        val reviewsFrag = CourseDetailsReviewsFrag()
        val commentsFrag = CourseDetailsCommentsFrag()

        informationFrag.arguments = bundle
        contentFrag.arguments = bundle
        reviewsFrag.arguments = bundle
        commentsFrag.arguments = bundle

        val adapter = ViewPagerAdapter(fragmentManager)
        adapter.add(informationFrag, context.getString(R.string.information))
        adapter.add(contentFrag, context.getString(R.string.content))
        adapter.add(reviewsFrag, context.getString(R.string.reviews))
        adapter.add(commentsFrag, context.getString(R.string.comments))
        return adapter
    }

    override fun getCourseDetails(id: Int, callback: ItemCallback<Course>) {
        val presenter = CommonApiPresenterImpl.getInstance()
        presenter.getCourseDetails(id, callback)
    }

    override fun getAddToCartItem(): AddToCart {
        val addToCart = AddToCart()
        addToCart.itemId = course.id
        addToCart.itemName = AddToCart.ItemType.WEBINAR.value
        return addToCart
    }

    override fun getBaseReviewObj(): Review {
        val review = Review()
        review.id = course.id
        review.item = Course.Type.WEBINAR.value
        return review
    }

    override fun getCourseType(): String {
        return Course.Type.WEBINAR.value
    }
}