package com.appzeto.lms.ui.widget

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.appzeto.lms.R
import com.appzeto.lms.databinding.DialogBaseInputBinding
import com.appzeto.lms.manager.App
import com.appzeto.lms.manager.ToastMaker
import com.appzeto.lms.manager.Utils
import com.appzeto.lms.manager.listener.ItemCallback
import com.appzeto.lms.manager.net.observer.NetworkObserverBottomSheetDialog
import com.appzeto.lms.model.BaseResponse
import com.appzeto.lms.model.ForumItem
import com.appzeto.lms.model.Reply
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.presenterImpl.ReplyToCourseForumPresenterImpl

class ForumReplyDialog : NetworkObserverBottomSheetDialog(), View.OnClickListener {
    private lateinit var mBinding: DialogBaseInputBinding
    private lateinit var mPresenter: Presenter.ReplyToCourseForumPresenter
    private var mReplySuccessfuilCallback: ItemCallback<Any>? = null
    private lateinit var mForumItem: ForumItem
    private lateinit var mLoadingDialog: LoadingDialog
    private var mPreviousDialog: BottomSheetDialogFragment? = null

    private val mInputTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            enableDisableBtn()
        }
    }

    private fun enableDisableBtn() {
        val input = mBinding.baseInputEdtx.text.toString()
        mBinding.baseInputBtn.isEnabled = input.isNotEmpty()
    }

    override fun onStart() {
        super.onStart()
        WidgetHelper.removeBottomSheetDialogHalfExpand(dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DialogBaseInputBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        mForumItem = requireArguments().getParcelable(App.ITEM)!!
        mPresenter = ReplyToCourseForumPresenterImpl(this)

        initUI()
    }

    private fun initUI() {
        mBinding.baseInputEdtx.addTextChangedListener(mInputTextWatcher)
        mBinding.baseInputBtn.isEnabled = false

        mBinding.baseInputHeaderTv.text = getString(R.string.reply_to_course_forum)
        mBinding.baseInputEdtx.hint = getString(R.string.descrption)
        mBinding.baseInputEdtx.minHeight = Utils.changeDpToPx(requireContext(), 160f).toInt()

        mBinding.baseInputCancelBtn.setOnClickListener(this)
        mBinding.baseInputBtn.setOnClickListener(this)

        if (mForumItem.isAnswer()) {
            mBinding.baseInputEdtx.setText(mForumItem.description)
            mBinding.baseInputBtn.text = getString(R.string.edit)
        } else {
            mBinding.baseInputBtn.text = getString(R.string.send)
        }
    }

    fun setOnReplySavedListener(
        replySuccessfuilCallback: ItemCallback<Any>,
        previousDialog: BottomSheetDialogFragment? = null
    ) {
        mPreviousDialog = previousDialog
        mReplySuccessfuilCallback = replySuccessfuilCallback
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.baseInputCancelBtn -> {
                dismiss()
            }

            R.id.baseInputBtn -> {
                mLoadingDialog = LoadingDialog.instance
                mLoadingDialog.show(childFragmentManager, null)

                val text = mBinding.baseInputEdtx.text.toString()

                val reply = Reply()
                reply.description = text

                mPresenter.reply(mForumItem, reply)
            }
        }
    }

    fun onRsp(response: BaseResponse) {
        mLoadingDialog.dismiss()
        ToastMaker.show(requireContext(), response)

        if (response.isSuccessful) {
            mPreviousDialog?.dismiss()
            dismiss()
            mReplySuccessfuilCallback?.onItem(Any())
        }
    }
}