package com.appzeto.lms.ui.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.appzeto.lms.R
import com.appzeto.lms.databinding.EmptyStateBinding
import com.appzeto.lms.databinding.RvBinding
import com.appzeto.lms.manager.App
import com.appzeto.lms.manager.adapter.ClassListGridRvAdapter
import com.appzeto.lms.manager.net.observer.NetworkObserverFragment
import com.appzeto.lms.model.Course
import com.appzeto.lms.presenterImpl.BundleCoursesPresenterImpl
import com.appzeto.lms.ui.MainActivity
import com.appzeto.lms.ui.frag.abs.EmptyState

class BundleCoursesFrag : NetworkObserverFragment(), EmptyState {

    private lateinit var mBinding: RvBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = RvBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val course = requireArguments().getParcelable<Course>(App.COURSE)!!

        val presenter = BundleCoursesPresenterImpl(this)
        presenter.getBundleCourses(course.id)
    }

    fun onCoursesReceived(items: List<Course>) {
        mBinding.rvProgressBar.visibility = View.GONE

        if (items.isNotEmpty()) {
            val rv = mBinding.rv
            val layoutManager = LinearLayoutManager(requireContext())
            rv.layoutManager = layoutManager

            rv.adapter = ClassListGridRvAdapter(
                items, activity as MainActivity, layoutManager
            )
        } else {
            showEmptyState()
        }
    }

    fun showEmptyState() {
        showEmptyState(R.drawable.no_course, R.string.no_courses, R.string.no_courses_found)
    }

    override fun emptyViewBinding(): EmptyStateBinding {
        return mBinding.rvEmptyState
    }

    override fun getVisibleFrag(): Fragment {
        return this
    }
}