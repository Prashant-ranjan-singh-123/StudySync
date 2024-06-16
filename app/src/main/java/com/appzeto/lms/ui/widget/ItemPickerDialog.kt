package com.appzeto.lms.ui.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.appzeto.lms.R
import com.appzeto.lms.databinding.DialogItemPickerBinding
import com.appzeto.lms.databinding.EmptyStateBinding
import com.appzeto.lms.manager.App
import com.appzeto.lms.manager.Utils
import com.appzeto.lms.manager.adapter.PickerRvAdapter
import com.appzeto.lms.manager.listener.ItemCallback
import com.appzeto.lms.manager.net.observer.NetworkObserverBottomSheetDialog
import com.appzeto.lms.model.ItemPicker
import com.appzeto.lms.ui.frag.abs.EmptyState
import kotlin.math.roundToInt

class ItemPickerDialog<T : ItemPicker> : NetworkObserverBottomSheetDialog(), View.OnClickListener,
    EmptyState {

    private lateinit var mBinding: DialogItemPickerBinding
    private lateinit var mItemCallback: ItemCallback<T>
    private var mSelectedItem: T? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DialogItemPickerBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        mBinding.selectorSelectBtn.setOnClickListener(this)
        mBinding.selectorCancelBtn.setOnClickListener(this)
        val title = requireArguments().getString(App.TITLE)
        mBinding.selectorTitleTv.text = title
    }

    fun onItemsReceived(items: List<T>, itemCallback: ItemCallback<T>) {
        mItemCallback = itemCallback
        mBinding.selectorRvProgressBar.visibility = View.GONE

        mBinding.selectorRv.layoutManager =
            SliderLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                mBinding.selectorRvSelectionView,
                object : SliderLayoutManager.OnItemSelectedListener {
                    override fun onItemSelected(position: Int) {
                        mSelectedItem = items[position]
                    }
                })
        mBinding.selectorRv.adapter = PickerRvAdapter(mBinding.selectorRv, items)
        mBinding.selectorRv.post {
            val padding =
                mBinding.selectorRv.height / 2 - Utils.changeDpToPx(requireContext(), 30f)
                    .roundToInt()
            mBinding.selectorRv.setPadding(0, padding, 0, padding)
            mBinding.selectorRvSelectionView.visibility = View.VISIBLE
            if (items.isNotEmpty()) {
                mBinding.selectorSelectBtn.isEnabled = true
                mBinding.selectorRv.smoothScrollToPosition(items.size / 2)
            } else {
                showEmptyState()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.selectorSelectBtn -> {
                if (mSelectedItem != null) {
                    dismiss()
                    mItemCallback.onItem(mSelectedItem!!)
                }
            }

            R.id.selectorCancelBtn -> {
                dismiss()
            }
        }
    }

    fun showEmptyState() {
        mBinding.selectorRvSelectionView.visibility = View.GONE
        showEmptyState(R.drawable.no_course, R.string.no_courses, R.string.purchase_no_courses2)
    }

    override fun emptyViewBinding(): EmptyStateBinding {
        return mBinding.rvEmptyState
    }

    override fun getVisibleFrag(): Fragment {
        return this
    }
}