package com.appzeto.lms.ui.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.appzeto.lms.R
import com.appzeto.lms.databinding.EmptyStateBinding
import com.appzeto.lms.databinding.FragFinancialPayoutBinding
import com.appzeto.lms.manager.App
import com.appzeto.lms.manager.Utils
import com.appzeto.lms.manager.adapter.CommonRvAdapter
import com.appzeto.lms.manager.net.observer.NetworkObserverFragment
import com.appzeto.lms.model.PayoutAccount
import com.appzeto.lms.model.PayoutRes
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.presenterImpl.FinancialPayoutPresenterImpl
import com.appzeto.lms.ui.frag.abs.EmptyState
import com.appzeto.lms.ui.widget.PayoutRequestDialog

class FinancialPayoutFrag : NetworkObserverFragment(), View.OnClickListener, EmptyState {

    private lateinit var mBinding: FragFinancialPayoutBinding
    private lateinit var mPresenter: Presenter.FinancialPayoutPresenter

    private lateinit var mPayoutAccount: PayoutAccount

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragFinancialPayoutBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        mPresenter = FinancialPayoutPresenterImpl(this)
        mPresenter.getPayouts()
    }

    fun onPayoutsReceived(res: PayoutRes) {
        mPayoutAccount = res.payoutAccount
        mBinding.financialPayoutRvProgressBar.visibility = View.GONE
        if (res.payouts.isNotEmpty()) {
            mBinding.financialPayoutRv.adapter = CommonRvAdapter(res.payouts)
        } else {
            showEmptyState()
        }

        mBinding.financialPayoutAmountTv.text =
            Utils.formatPrice(requireContext(), res.payoutAccount.amonut, false)
        mBinding.financialPayoutRequestBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val bundle = Bundle()
        bundle.putParcelable(App.PAYOUT_ACCOUT, mPayoutAccount)

        val dialog = PayoutRequestDialog()
        dialog.arguments = bundle
        dialog.show(childFragmentManager, null)
    }

    fun showEmptyState() {
        showEmptyState(R.drawable.no_payout, R.string.no_payout, R.string.no_payout_desc)
    }

    override fun emptyViewBinding(): EmptyStateBinding {
        return mBinding.financialPayoutEmptyState
    }

    override fun getVisibleFrag(): Fragment {
        return this
    }
}