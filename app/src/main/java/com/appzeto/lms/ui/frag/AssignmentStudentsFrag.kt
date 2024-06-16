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
import com.appzeto.lms.manager.adapter.AssignmentStudentRvAdapter
import com.appzeto.lms.manager.listener.ItemClickListener
import com.appzeto.lms.manager.listener.OnItemClickListener
import com.appzeto.lms.manager.net.observer.NetworkObserverFragment
import com.appzeto.lms.model.Assignment
import com.appzeto.lms.ui.MainActivity
import com.appzeto.lms.ui.frag.abs.EmptyState

class AssignmentStudentsFrag : NetworkObserverFragment(), OnItemClickListener, EmptyState {

    private lateinit var mBinding: RvBinding
    private var mIsInstructorType = false

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
        val assignments = requireArguments().getParcelableArrayList<Assignment>(App.ITEMS)!!
        mIsInstructorType = requireArguments().getBoolean(App.INSTRUCTOR_TYPE)

        mBinding.rvProgressBar.visibility = View.GONE
        if (assignments.isNotEmpty()) {
            mBinding.rv.layoutManager = LinearLayoutManager(requireContext())
            mBinding.rv.adapter = AssignmentStudentRvAdapter(assignments)
            mBinding.rv.addOnItemTouchListener(ItemClickListener(mBinding.rv, this))
        } else {
            showEmptyState()
        }
    }

    override fun onClick(view: View?, position: Int, id: Int) {
        val assignment = ((mBinding.rv.adapter) as AssignmentStudentRvAdapter).items[position]

        val bundle = Bundle()
        bundle.putParcelable(App.ITEM, assignment)
        bundle.putBoolean(App.INSTRUCTOR_TYPE, mIsInstructorType)

        val frag = AssignmentConversationFrag()
        frag.arguments = bundle
        (activity as MainActivity).transact(frag)
    }

    override fun onLongClick(view: View?, position: Int, id: Int) {
    }

    fun showEmptyState() {
        showEmptyState(
            R.drawable.no_submission,
            R.string.no_submissions,
            R.string.no_submissions_desc
        )
    }

    override fun emptyViewBinding(): EmptyStateBinding {
        return mBinding.rvEmptyState
    }

    override fun getVisibleFrag(): Fragment {
        return this
    }
}