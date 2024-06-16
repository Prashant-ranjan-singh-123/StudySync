package com.appzeto.lms.ui.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.appzeto.lms.R
import com.appzeto.lms.databinding.EmptyStateBinding
import com.appzeto.lms.databinding.FragStudentAssignmentsBinding
import com.appzeto.lms.manager.App
import com.appzeto.lms.manager.adapter.AssignmentsRvAdapter
import com.appzeto.lms.manager.listener.ItemClickListener
import com.appzeto.lms.manager.listener.OnItemClickListener
import com.appzeto.lms.manager.net.observer.NetworkObserverFragment
import com.appzeto.lms.model.StudentAssignments
import com.appzeto.lms.presenterImpl.StudentAssignmentsPresenterImpl
import com.appzeto.lms.ui.MainActivity
import com.appzeto.lms.ui.frag.abs.EmptyState

class StudentAssignmentsFrag : NetworkObserverFragment(), EmptyState, OnItemClickListener {

    private lateinit var mBinding: FragStudentAssignmentsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragStudentAssignmentsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val presenter = StudentAssignmentsPresenterImpl(this)
        presenter.getStudentAssignments()
    }

    private fun showEmptyState() {
        mBinding.studentAssignmentsPendingHeaderContainer.visibility = View.GONE

        showEmptyState(
            R.drawable.no_submission,
            R.string.no_assignments,
            R.string.no_assignments_teacher
        )
    }

    override fun emptyViewBinding(): EmptyStateBinding {
        return mBinding.studentAssignmentsEmptyState
    }

    override fun getVisibleFrag(): Fragment {
        return this
    }

    fun onAssignmentsReceived(studentAssignments: StudentAssignments) {
        mBinding.studentAssignmentsRvProgressBar.visibility = View.GONE
        val assignments = studentAssignments.assignments

        if (assignments.isEmpty()) {
            showEmptyState()
        } else {
            mBinding.studentAssignmentsPendingTv.text =
                studentAssignments.pendingAssignments.toString()
            mBinding.studentAssignmentsPassedTv.text =
                studentAssignments.passedAssignments.toString()
            mBinding.studentAssignmentsFailedTv.text =
                studentAssignments.failedAssignments.toString()

            val rv = mBinding.studentAssignmentsRv

            rv.adapter = AssignmentsRvAdapter(assignments, true)
            rv.addOnItemTouchListener(ItemClickListener(rv, this))
        }
    }

    override fun onLongClick(view: View?, position: Int, id: Int) {
    }

    override fun onClick(view: View?, position: Int, id: Int) {
        val assignment =
            ((mBinding.studentAssignmentsRv.adapter) as AssignmentsRvAdapter).items[position]

        val bundle = Bundle()
        bundle.putParcelable(App.ITEM, assignment)
        bundle.putBoolean(App.INSTRUCTOR_TYPE, true)

        val frag = AssignmentOverviewFrag()
        frag.arguments = bundle
        (activity as MainActivity).transact(frag)
    }

}