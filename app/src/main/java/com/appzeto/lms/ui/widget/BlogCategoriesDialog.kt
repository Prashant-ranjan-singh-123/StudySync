package com.appzeto.lms.ui.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.appzeto.lms.R
import com.appzeto.lms.databinding.DialogBlogCategoriesBinding
import com.appzeto.lms.manager.adapter.BlogCategoriesRvAdapter
import com.appzeto.lms.manager.listener.ItemCallback
import com.appzeto.lms.manager.listener.ItemClickListener
import com.appzeto.lms.manager.listener.OnItemClickListener
import com.appzeto.lms.model.Category
import com.appzeto.lms.presenterImpl.BlogCategoriesPresenterImpl

class BlogCategoriesDialog : BottomSheetDialogFragment(), View.OnClickListener,
    OnItemClickListener {

    private lateinit var mBinding: DialogBlogCategoriesBinding
    private var mCallback: ItemCallback<Category>? = null

    override fun onStart() {
        super.onStart()
        WidgetHelper.removeBottomSheetDialogHalfExpand(dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DialogBlogCategoriesBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun setCallback(callback: ItemCallback<Category>) {
        mCallback = callback
    }

    private fun init() {
        mBinding.blogsCategoriesCancelBtn.setOnClickListener(this)
        val presenter = BlogCategoriesPresenterImpl(this)
        presenter.getBlogCategories()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.blogs_categories_cancel_btn -> {
                dismiss()
            }
        }
    }

    fun onBlogCatsReceived(cats: List<Category>) {
        mBinding.blogsCategoriesRvProgressBar.visibility = View.GONE
        mBinding.blogsCategoriesRv.adapter = BlogCategoriesRvAdapter(cats)
        mBinding.blogsCategoriesRv.addOnItemTouchListener(
            ItemClickListener(
                mBinding.blogsCategoriesRv,
                this
            )
        )
    }

    override fun onClick(view: View?, position: Int, id: Int) {
        val category =
            (mBinding.blogsCategoriesRv.adapter as BlogCategoriesRvAdapter).items[position]
        mCallback?.onItem(category)
        dismiss()
    }

    override fun onLongClick(view: View?, position: Int, id: Int) {
    }
}