package com.appzeto.lms.ui.widget

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appzeto.lms.R
import com.appzeto.lms.databinding.DialogMeetingJoinDetailsBinding
import com.appzeto.lms.manager.App
import com.appzeto.lms.manager.ToastMaker
import com.appzeto.lms.manager.net.observer.NetworkObserverBottomSheetDialog
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.Meeting
import com.appzeto.lms.model.ReserveMeeting
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.presenterImpl.MeetingJoinDetailsPresenterImpl

class MeetingJoinDetailsDialog : NetworkObserverBottomSheetDialog(), View.OnClickListener {

    private lateinit var mBinding: DialogMeetingJoinDetailsBinding
    private lateinit var mMeeting: Meeting
    private lateinit var mPresenter: Presenter.MeetingJoinDetailsPresenter

    private val mTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            enableDisableBtn()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }

    private fun enableDisableBtn() {
        val url = mBinding.meetingJoinDetailsUrlEdtx.text.toString()
        mBinding.meetingJoinDetailsSaveBtn.isEnabled = url.isNotEmpty()
    }

    override fun onStart() {
        super.onStart()
        WidgetHelper.removeBottomSheetDialogHalfExpand(dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DialogMeetingJoinDetailsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        mPresenter = MeetingJoinDetailsPresenterImpl(this)
        mMeeting = requireArguments().getParcelable(App.MEETING)!!
        mBinding.meetingJoinDetailsUrlEdtx.addTextChangedListener(mTextWatcher)
        mBinding.meetingJoinDetailsCancelBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.meetingJoinDetailsCancelBtn -> {
                dismiss()
            }

            R.id.meetingJoinDetailsSaveBtn -> {
                val url = mBinding.meetingJoinDetailsUrlEdtx.text.toString()
                val password = mBinding.meetingJoinDetailsPasswordEdtx.text.toString()

                val reserveMeeting = ReserveMeeting()
                reserveMeeting.reservedMeetingId = mMeeting.id
                reserveMeeting.link = url
                if (password.isNotEmpty()) {
                    reserveMeeting.password = password
                }

                mPresenter.createJoin(reserveMeeting)
            }
        }
    }

    fun onMeetingJoinAdded(response: BaseResponse) {
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