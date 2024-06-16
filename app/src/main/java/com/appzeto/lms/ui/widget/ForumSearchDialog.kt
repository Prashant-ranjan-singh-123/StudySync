package com.appzeto.lms.ui.widget

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appzeto.lms.R
import com.appzeto.lms.databinding.DialogForumSearchBinding
import com.appzeto.lms.manager.listener.ItemCallback
import com.appzeto.lms.manager.net.observer.NetworkObserverBottomSheetDialog
import com.appzeto.lms.model.*

class ForumSearchDialog : NetworkObserverBottomSheetDialog(), View.OnClickListener {

    private lateinit var mBinding: DialogForumSearchBinding
    private lateinit var mCallback: ItemCallback<String>

    private val mTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            mBinding.forumSearchBtn.isEnabled = mBinding.forumSearchEdtx.text.toString().isNotEmpty()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

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
        mBinding = DialogForumSearchBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        mBinding.forumSearchEdtx.addTextChangedListener(mTextWatcher)
        mBinding.forumSearchBtn.setOnClickListener(this)
        mBinding.forumSearchCancelBtn.setOnClickListener(this)
    }

    fun setOnSearchListener(callback: ItemCallback<String>) {
        mCallback = callback
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.forum_search_btn -> {
                val s = mBinding.forumSearchEdtx.text.toString()
                mCallback.onItem(s)
            }
        }

        dismiss()
    }
}