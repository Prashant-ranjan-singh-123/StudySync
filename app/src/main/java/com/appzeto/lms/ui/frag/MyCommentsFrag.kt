package com.appzeto.lms.ui.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.appzeto.lms.R
import com.appzeto.lms.databinding.EmptyStateBinding
import com.appzeto.lms.databinding.RvBinding
import com.appzeto.lms.manager.App
import com.appzeto.lms.manager.adapter.MyCommentRvAdapter
import com.appzeto.lms.manager.listener.ItemCallback
import com.appzeto.lms.manager.listener.ItemClickListener
import com.appzeto.lms.manager.listener.OnItemClickListener
import com.appzeto.lms.model.Comment
import com.appzeto.lms.model.Comments
import com.appzeto.lms.presenterImpl.CommentsPresenterImpl
import com.appzeto.lms.ui.MainActivity
import com.appzeto.lms.ui.frag.abs.EmptyState

class MyCommentsFrag : Fragment(), OnItemClickListener, ItemCallback<Comments>, EmptyState {

    private lateinit var mBinding: RvBinding
    private lateinit var mCommentsFrag: CommentsFrag

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
        mBinding.rvContainer.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )

        mBinding.rvEmptyState.root.visibility = View.INVISIBLE
        val presenter = CommentsPresenterImpl()
        presenter.getComments(this)
    }

    override fun onItem(item: Comments, vararg args: Any) {
        if (context == null) return

        mBinding.rvProgressBar.visibility = View.INVISIBLE

        val comments = ArrayList<Comment>()
        comments.addAll(item.myComments.webinarComments)
        comments.addAll(item.myComments.blogsComments)

        if (comments.isNotEmpty()) {
            val adapter = MyCommentRvAdapter(comments)
            mBinding.rv.layoutManager = LinearLayoutManager(requireContext())
            mBinding.rv.adapter = adapter
            mBinding.rv.addOnItemTouchListener(ItemClickListener(mBinding.rv, this))
        } else {
            showEmptyState()
        }
    }

    override fun onClick(view: View?, position: Int, id: Int) {
        val comment = (mBinding.rv.adapter as MyCommentRvAdapter).items[position]

        val bundle = Bundle()
        bundle.putParcelable(App.COMMENT, comment)

        val frag = CommentDetailsFrag()
        frag.arguments = bundle
        (activity as MainActivity).transact(frag)
    }

    override fun onLongClick(view: View?, position: Int, id: Int) {
    }

    fun showEmptyState() {
        showEmptyState(R.drawable.no_comments, R.string.no_comments, R.string.no_comments_student_desc)
    }

    override fun emptyViewBinding(): EmptyStateBinding {
        return mBinding.rvEmptyState
    }

    override fun getVisibleFrag(): Fragment {
        return this
    }
}