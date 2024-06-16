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
import com.appzeto.lms.manager.adapter.BankInfoRvAdapter
import com.appzeto.lms.manager.net.observer.NetworkObserverFragment
import com.appzeto.lms.model.SystemBankAccount
import com.appzeto.lms.model.ToolbarOptions
import com.appzeto.lms.presenterImpl.BanksInfoPresenterImpl
import com.appzeto.lms.ui.MainActivity
import com.appzeto.lms.ui.frag.abs.EmptyState

class BanksInfoFrag : NetworkObserverFragment(), EmptyState {

    private lateinit var mBinding: RvBinding

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
        toolbarOptions.startIcon = ToolbarOptions.Icon.BACK

        (activity as MainActivity).showToolbar(toolbarOptions, R.string.bank_accounts)

        val presenter = BanksInfoPresenterImpl(this)
        presenter.getBanksInfo()
    }

    fun onInfosReceived(items: List<SystemBankAccount>) {
        mBinding.rvProgressBar.visibility = View.GONE
        if (items.isNotEmpty()) {
            mBinding.rv.layoutManager = LinearLayoutManager(requireContext())
            mBinding.rv.adapter = BankInfoRvAdapter(items)
        } else {
            showEmptyState()
        }
    }

    fun showEmptyState() {
        showEmptyState(R.drawable.no_offline_payments, R.string.no_bank_accounts, R.string.no_bank_accounts_desc)
    }

    override fun emptyViewBinding(): EmptyStateBinding {
        return mBinding.rvEmptyState
    }

    override fun getVisibleFrag(): Fragment {
        return this
    }
}