package com.appzeto.lms.ui.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.appzeto.lms.R
import com.appzeto.lms.databinding.EmptyStateBinding
import com.appzeto.lms.databinding.RvBinding
import com.appzeto.lms.manager.App
import com.appzeto.lms.manager.adapter.MeetingRvAdapter
import com.appzeto.lms.manager.listener.ItemClickListener
import com.appzeto.lms.manager.listener.OnItemClickListener
import com.appzeto.lms.model.Meeting
import com.appzeto.lms.ui.MainActivity
import com.appzeto.lms.ui.frag.abs.EmptyState

class MeetingsFrag : Fragment(), OnItemClickListener, EmptyState {

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
        mBinding.rvContainer.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )
        mBinding.rvEmptyState.root.visibility = View.INVISIBLE
        mBinding.rvProgressBar.visibility = View.INVISIBLE
        val meetings = requireArguments().getParcelableArrayList<Meeting>(App.MEETINGS)!!
        if (meetings.isNotEmpty()) {
            val adapter = MeetingRvAdapter(meetings)
            mBinding.rv.layoutManager = LinearLayoutManager(requireContext())
            mBinding.rv.adapter = adapter
            mBinding.rv.addOnItemTouchListener(ItemClickListener(mBinding.rv, this))
        } else {
            showEmptyState()
        }
    }

    override fun onClick(view: View?, position: Int, id: Int) {
        val meeting = (mBinding.rv.adapter as MeetingRvAdapter).items[position]

        val bundle = Bundle()
        bundle.putParcelable(App.MEETING, meeting)
        bundle.putBoolean(
            App.INSTRUCTOR_TYPE,
            requireArguments().getBoolean(App.INSTRUCTOR_TYPE, false)
        )

        val frag = MeetingDetailsFrag()
        frag.arguments = bundle
        (activity as MainActivity).transact(frag)
    }

    override fun onLongClick(view: View?, position: Int, id: Int) {
    }

    fun showEmptyState() {
        showEmptyState(R.drawable.no_meeting, R.string.no_meetings, R.string.no_meetings_desc)
    }

    override fun emptyViewBinding(): EmptyStateBinding {
        return mBinding.rvEmptyState
    }

    override fun getVisibleFrag(): Fragment {
        return this
    }
}