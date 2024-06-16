package com.appzeto.lms.ui.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.appzeto.lms.R
import com.appzeto.lms.databinding.DialogPayoutRequestBinding
import com.appzeto.lms.manager.App
import com.appzeto.lms.manager.ToastMaker
import com.appzeto.lms.manager.Utils
import com.appzeto.lms.manager.net.observer.NetworkObserverBottomSheetDialog
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.PayoutAccount
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.presenterImpl.PayoutRequestPresenterImpl
import com.appzeto.lms.ui.MainActivity
import com.appzeto.lms.ui.frag.SettingsFrag

class PayoutRequestDialog : NetworkObserverBottomSheetDialog(), View.OnClickListener {

    private lateinit var mBinding: DialogPayoutRequestBinding
    private lateinit var mPayoutAccount: PayoutAccount
    private lateinit var mPresenter: Presenter.PayoutRequestPresenter

    override fun onStart() {
        super.onStart()
        WidgetHelper.removeBottomSheetDialogHalfExpand(dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DialogPayoutRequestBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        mPayoutAccount = requireArguments().getParcelable(App.PAYOUT_ACCOUT)!!

        mBinding.payoutRequestCancelBtn.setOnClickListener(this)
        mBinding.payoutRequestSendBtn.setOnClickListener(this)

        if (mPayoutAccount.cardId == null || mPayoutAccount.iban == null) {
            mBinding.payoutRequestAmountKeyTv.visibility = View.GONE
            mBinding.payoutRequestAmountValueTv.visibility = View.GONE

            mBinding.payoutRequestCardIdKeyTv.visibility = View.GONE
            mBinding.payoutRequestCardIdValueTv.visibility = View.GONE

            mBinding.payoutRequestAccountKeyTv.visibility = View.GONE
            mBinding.payoutRequestAccountValueTv.visibility = View.GONE

            mBinding.payoutRequestIBANKeyTv.visibility = View.GONE
            mBinding.payoutRequestIBANValueTv.visibility = View.GONE

            val gatewayImg = mBinding.payoutRequestGatewayImg
            val params = gatewayImg.layoutParams as ConstraintLayout.LayoutParams
            params.width = Utils.changeDpToPx(requireContext(), 80f).toInt()
            params.height = Utils.changeDpToPx(requireContext(), 80f).toInt()
            gatewayImg.requestLayout()

            mBinding.payoutRequestGatewayTv.text = getString(R.string.payout_disabled)
            mBinding.payoutRequestGatewayDescTv.text = getText(R.string.payout_disabled_desc)
            mBinding.payoutRequestGatewayDescTv.visibility = View.VISIBLE
            mBinding.payoutRequestSendBtn.text = getString(R.string.financial_settings)

        } else if (mPayoutAccount.amonut < mPayoutAccount.minimumPayout) {
            mBinding.payoutRequestAmountKeyTv.visibility = View.GONE
            mBinding.payoutRequestAmountValueTv.visibility = View.GONE

            mBinding.payoutRequestSendBtn.isEnabled = false
            mBinding.payoutRequestDescTv.text = getString(R.string.payout_lower_than_min)

            showAccountInfo()

        } else {
            mPresenter = PayoutRequestPresenterImpl(this)
            mBinding.payoutRequestDescTv.text = getString(R.string.request_payout_desc)
            mBinding.payoutRequestAmountValueTv.text =
                Utils.formatPrice(requireContext(), mPayoutAccount.amonut)
            showAccountInfo()
        }
    }

    private fun showAccountInfo() {
        mBinding.payoutRequestCardIdValueTv.text = mPayoutAccount.cardId
        mBinding.payoutRequestAccountValueTv.text = mPayoutAccount.account
        mBinding.payoutRequestIBANValueTv.text = mPayoutAccount.iban
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.payoutRequestCancelBtn -> {
                dismiss()
            }

            R.id.payoutRequestSendBtn -> {
                if (mBinding.payoutRequestSendBtn.text == getString(R.string.financial_settings)) {
                    val bundle = Bundle()
                    bundle.putBoolean(App.FINANCIAL, true)

                    val frag = SettingsFrag()
                    frag.arguments = bundle
                    (activity as MainActivity).transact(frag)

                } else {
                    mPresenter.requestPayout()
                }
            }
        }
    }

    fun onPayoutSaved(response: BaseResponse) {
        if (context == null) return

        if (response.isSuccessful) {
            dismiss()
        } else {
            ToastMaker.show(
                requireContext(),
                getString(R.string.error),
                response.message,
                ToastMaker.Type.ERROR
            )
        }
    }
}