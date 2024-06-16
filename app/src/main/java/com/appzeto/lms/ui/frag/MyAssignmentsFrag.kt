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
import com.appzeto.lms.manager.adapter.AssignmentsRvAdapter
import com.appzeto.lms.manager.listener.ItemClickListener
import com.appzeto.lms.manager.listener.OnItemClickListener
import com.appzeto.lms.manager.net.observer.NetworkObserverFragment
import com.appzeto.lms.model.Assignment
import com.appzeto.lms.presenterImpl.MyAssignmentsPresenterImpl
import com.appzeto.lms.ui.MainActivity
import com.appzeto.lms.ui.frag.abs.EmptyState

class MyAssignmentsFrag : NetworkObserverFragment(), EmptyState, OnItemClickListener {

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
        val presenter = MyAssignmentsPresenterImpl(this)
        presenter.getMyAssignments()
    }

    fun onAssignmentsReceived(assignments: List<Assignment>) {
        mBinding.rvProgressBar.visibility = View.GONE

        if (assignments.isEmpty()) {
            showEmptyState()
        } else {
            val rv = mBinding.rv
            rv.layoutManager = LinearLayoutManager(requireContext())
            rv.adapter = AssignmentsRvAdapter(assignments)
            rv.addOnItemTouchListener(ItemClickListener(rv, this))
        }
    }

    private fun showEmptyState() {
        showEmptyState(
            R.drawable.no_submission,
            R.string.no_assignments,
            R.string.no_assignments_student_desc
        )
    }

    override fun emptyViewBinding(): EmptyStateBinding {
        return mBinding.rvEmptyState
    }

    override fun getVisibleFrag(): Fragment {
        return this
    }

    override fun onLongClick(view: View?, position: Int, id: Int) {
    }

    override fun onClick(view: View?, position: Int, id: Int) {
        val assignment = (mBinding.rv.adapter as AssignmentsRvAdapter).items[position]

        val bundle = Bundle()
        bundle.putParcelable(App.ITEM, assignment)

        val overviewFrag = AssignmentOverviewFrag()
        overviewFrag.arguments = bundle
        (activity as MainActivity).transact(overviewFrag)
    }
}