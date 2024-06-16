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
import com.appzeto.lms.manager.adapter.NoticeRvAdapter
import com.appzeto.lms.manager.listener.ItemClickListener
import com.appzeto.lms.manager.listener.OnItemClickListener
import com.appzeto.lms.manager.net.observer.NetworkObserverFragment
import com.appzeto.lms.model.Notice
import com.appzeto.lms.presenterImpl.NoticesPresenterImpl
import com.appzeto.lms.ui.frag.abs.EmptyState
import com.appzeto.lms.ui.widget.NoticeDialog

class NoticesFrag : NetworkObserverFragment(), EmptyState, OnItemClickListener {

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
        val courseId = requireArguments().getInt(App.ID)

        val presenter = NoticesPresenterImpl(this)
        presenter.getNotices(courseId)
    }

    fun onNoticesReceived(notices: List<Notice>) {
        mBinding.rvProgressBar.visibility = View.GONE
        if (notices.isEmpty()) {
            showEmptyState()
        } else {
            mBinding.rv.layoutManager = LinearLayoutManager(requireContext())
            mBinding.rv.adapter = NoticeRvAdapter(notices)
            mBinding.rv.addOnItemTouchListener(ItemClickListener(mBinding.rv, this))
        }
    }

    fun showEmptyState() {
        showEmptyState(R.drawable.no_blog, R.string.no_notices, R.string.no_notices_desc)
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
        val notice = (mBinding.rv.adapter as NoticeRvAdapter).items[position]

        val bundle = Bundle()
        bundle.putParcelable(App.ITEM, notice)

        val dialog = NoticeDialog()
        dialog.arguments = bundle
        dialog.show(childFragmentManager, null)
    }
}