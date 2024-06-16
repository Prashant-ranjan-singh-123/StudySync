package com.appzeto.lms.ui.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.appzeto.lms.R
import com.appzeto.lms.databinding.EmptyStateBinding
import com.appzeto.lms.databinding.FragRewardPointsBinding
import com.appzeto.lms.manager.adapter.CommonRvAdapter
import com.appzeto.lms.manager.net.observer.NetworkObserverFragment
import com.appzeto.lms.model.Points
import com.appzeto.lms.model.ToolbarOptions
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.presenterImpl.RewardPointsPresenterImpl
import com.appzeto.lms.ui.MainActivity
import com.appzeto.lms.ui.frag.abs.EmptyState

class RewardPointsFrag : NetworkObserverFragment(), EmptyState {

    private lateinit var mBinding: FragRewardPointsBinding
    private lateinit var mPresenter: Presenter.RewardPointsPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragRewardPointsBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val toolbarOptions = ToolbarOptions()
        toolbarOptions.startIcon = ToolbarOptions.Icon.BACK

        (activity as MainActivity).showToolbar(toolbarOptions, R.string.reward_points)

        mPresenter = RewardPointsPresenterImpl(this)
        mPresenter.getPoints()
    }

    fun showEmptyState() {
        showEmptyState(R.drawable.no_sales, R.string.no_points, R.string.no_points_desc)
    }

    override fun emptyViewBinding(): EmptyStateBinding {
        return mBinding.rewardPointsHistoryEmptyState
    }

    override fun getVisibleFrag(): Fragment {
        return this
    }

    fun onPointsReceived(points: Points) {
        mBinding.rewardPointsHistoryRvProgressBar.visibility = View.GONE
        mBinding.rewardPointsTotalCountTv.text = points.totalPoints.toString()
        mBinding.rewardPointsSpentCountTv.text = points.spentPoints.toString()
        mBinding.rewardPointsRemainedCountTv.text = points.availablePoints.toString()

        if (points.rewards.isNotEmpty()) {
            val adapter = CommonRvAdapter(points.rewards)
            mBinding.rewardPointsHistoryRv.adapter = adapter
        } else {
            showEmptyState()
        }
    }

}