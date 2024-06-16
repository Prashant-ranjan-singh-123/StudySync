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
import com.appzeto.lms.manager.adapter.CommonRvAdapter
import com.appzeto.lms.manager.listener.ItemCallback
import com.appzeto.lms.manager.listener.ItemClickListener
import com.appzeto.lms.manager.listener.OnItemClickListener
import com.appzeto.lms.manager.net.observer.NetworkObserverFragment
import com.appzeto.lms.model.Count
import com.appzeto.lms.model.Data
import com.appzeto.lms.model.Notif
import com.appzeto.lms.model.ToolbarOptions
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.presenterImpl.NotifPresenterImpl
import com.appzeto.lms.ui.MainActivity
import com.appzeto.lms.ui.frag.abs.EmptyState
import com.appzeto.lms.ui.widget.NotifDialog

class NotifsFrag : NetworkObserverFragment(), OnItemClickListener, ItemCallback<Int>, EmptyState, MainActivity.OnRefreshListener {

    private lateinit var mBinding: RvBinding
    private lateinit var mPresenter: Presenter.NotifPresenter
    private lateinit var mNotifs: List<Notif>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = RvBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val toolbarOptions = ToolbarOptions()
        toolbarOptions.startIcon = ToolbarOptions.Icon.NAV

        (activity as MainActivity).showToolbar(toolbarOptions, R.string.notifications)

       initData()
    }

    private fun initData() {
        if (!App.isLoggedIn()) {
            mBinding.rvProgressBar.visibility = View.GONE
            showLoginState()
            return
        }

        mPresenter = NotifPresenterImpl(this)
        mPresenter.getNotifs()
    }

    fun onNotifsReceived(data: Data<Count<Notif>>) {
        mNotifs = data.data!!.items
        mBinding.rvProgressBar.visibility = View.GONE
        if (mNotifs.isNotEmpty()) {
            mBinding.rv.layoutManager = LinearLayoutManager(requireContext())
            mBinding.rv.adapter = CommonRvAdapter(mNotifs)
            mBinding.rv.addOnItemTouchListener(ItemClickListener(mBinding.rv, this))
        } else {
            showEmptyState()
        }
    }

    override fun onClick(view: View?, position: Int, id: Int) {
        val bundle = Bundle()
        bundle.putParcelable(App.NOTIF, mNotifs[position])

        val dialog = NotifDialog()
        dialog.setOnStatusChangeListener(this, position)
        dialog.arguments = bundle
        dialog.show(childFragmentManager, null)
    }

    override fun onLongClick(view: View?, position: Int, id: Int) {
    }

    override fun onItem(position: Int, vararg args: Any) {
        val viewHolder =
            mBinding.rv.findViewHolderForAdapterPosition(position) as CommonRvAdapter.ViewHolder

        viewHolder.hideSatusCircle()
    }

    fun showEmptyState() {
        showEmptyState(R.drawable.no_notification, R.string.no_notifications, R.string.no_notifications_desc)
    }

    override fun emptyViewBinding(): EmptyStateBinding {
        return mBinding.rvEmptyState
    }

    override fun getVisibleFrag(): Fragment {
        return this
    }

    override fun getRefreshListener(): MainActivity.OnRefreshListener? {
        return this
    }

    override fun refresh() {
        hideEmptyState()
        initData()
    }
}