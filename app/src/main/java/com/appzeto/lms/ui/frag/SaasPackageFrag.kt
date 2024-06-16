package com.appzeto.lms.ui.frag

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.appzeto.lms.R
import com.appzeto.lms.databinding.EmptyStateBinding
import com.appzeto.lms.databinding.FragSaasPackageBinding
import com.appzeto.lms.manager.ToastMaker
import com.appzeto.lms.manager.Utils
import com.appzeto.lms.manager.adapter.SaasPackageAdapter
import com.appzeto.lms.manager.adapter.CardStatisticsRvAdapter
import com.appzeto.lms.manager.net.observer.NetworkObserverFragment
import com.appzeto.lms.model.*
import com.appzeto.lms.model.view.CommonItem
import com.appzeto.lms.model.view.PaymentRedirection
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.presenterImpl.SaasPackagePresenterImpl
import com.appzeto.lms.ui.MainActivity
import com.appzeto.lms.ui.PaymentStatusActivity
import com.appzeto.lms.ui.frag.abs.EmptyState
import com.appzeto.lms.ui.widget.LoadingDialog

class SaasPackageFrag : NetworkObserverFragment(), EmptyState {

    private lateinit var mBinding: FragSaasPackageBinding
    private lateinit var mPresenter: Presenter.SaasPackagePresenter
    private lateinit var mLoadingDialog: LoadingDialog

    override fun emptyViewBinding(): EmptyStateBinding {
        return mBinding.saasPackageEmptyState
    }

    override fun getVisibleFrag(): Fragment {
        return this@SaasPackageFrag
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragSaasPackageBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        mPresenter = SaasPackagePresenterImpl(this)
        mPresenter.getSaasPackages()
    }


    fun onCheckout(data: Data<Response>) {
        mLoadingDialog.dismiss()

        if (data.isSuccessful && !data.data!!.link.isNullOrEmpty()) {
            val redirection = PaymentRedirection()
            redirection.isNavDrawer = true
            redirection.position = MainActivity.SlideMenuItem.SUBSCRIPTION.value()
            redirection.buttonTitle = getString(R.string.my_package)

            PaymentStatusActivity.paymentRedirection = redirection

            Utils.openLink(requireContext(), data.data!!.link)
//            val bundle = Bundle()
//            bundle.putParcelable(App.ORDER, data.data)
//

//            bundle.putParcelable(App.REDIRECTION, redirection)
//
//            val paymentFrag = ChargeAccountPaymentFrag()
//            paymentFrag.arguments = bundle
//            (activity as MainActivity).transact(paymentFrag)
        } else {
            ToastMaker.show(
                requireContext(),
                getString(R.string.error),
                data.message,
                ToastMaker.Type.ERROR
            )
        }
    }

    fun onRequestFailed() {
        mLoadingDialog.dismiss()
    }

    fun onSaasPackageReceived(sassPackage: SaasPackage) {
        mBinding.saasPackageActivePlanTv.text = sassPackage.activePackage.title
        mBinding.saasPackageActivationDateTv.text =
            Utils.getDateFromTimestamp(sassPackage.activePackage.activationDate)
        mBinding.saasPackageRemainedDaysTv.text = sassPackage.activePackage.daysRemained

        if (sassPackage.packages.isEmpty()) {
            showEmptyState(
                R.drawable.no_subscription,
                getString(R.string.no_package),
                getString(R.string.no_package_desc)
            )

            mBinding.saasPackagePlanTv.visibility = View.GONE
        }

        addAccountStatistics(sassPackage)

        mBinding.saasPackageViewPager.adapter = SaasPackageAdapter(sassPackage.packages, this)
        mBinding.saasPackageIndicator.setViewPager2(mBinding.saasPackageViewPager)
    }

    private fun addAccountStatistics(sassPackage: SaasPackage) {
        val items = ArrayList<CommonItem>()

        if (!sassPackage.accountCoursesCount.isNullOrEmpty()) {
            items.add(object : CommonItem {
                override fun title(context: Context): String {
                    return sassPackage.accountCoursesCount!!
                }

                override fun img(): String? {
                    return null
                }

                override fun desc(context: Context): String {
                    return getString(R.string.new_courses)
                }

                override fun imgResource(): Int {
                    return R.drawable.ic_play_green
                }

                override fun imgBgResource(): Int {
                    return R.drawable.circle_light_green
                }
            })
        }

        if (!sassPackage.accountCoursesCapacity.isNullOrEmpty()) {
            items.add(object : CommonItem {
                override fun title(context: Context): String {
                    return sassPackage.accountCoursesCapacity!!
                }

                override fun img(): String? {
                    return null
                }

                override fun desc(context: Context): String {
                    return getString(R.string.live_class_capacity)
                }

                override fun imgResource(): Int {
                    return R.drawable.ic_video_red
                }

                override fun imgBgResource(): Int {
                    return R.drawable.circle_light_red
                }
            })
        }

        if (!sassPackage.accountMeetingCount.isNullOrEmpty()) {
            items.add(object : CommonItem {
                override fun title(context: Context): String {
                    return sassPackage.accountMeetingCount!!
                }

                override fun img(): String? {
                    return null
                }

                override fun desc(context: Context): String {
                    return getString(R.string.meeting_time_slots)
                }

                override fun imgResource(): Int {
                    return R.drawable.ic_time_blue
                }

                override fun imgBgResource(): Int {
                    return R.drawable.circle_light_blue
                }
            })
        }


        if (!sassPackage.accountStudentsCount.isNullOrEmpty()) {
            items.add(object : CommonItem {
                override fun title(context: Context): String {
                    return sassPackage.accountStudentsCount!!
                }

                override fun img(): String? {
                    return null
                }

                override fun desc(context: Context): String {
                    return getString(R.string.students)
                }

                override fun imgResource(): Int {
                    return R.drawable.ic_user_green
                }

                override fun imgBgResource(): Int {
                    return R.drawable.circle_light_green2
                }
            })
        }


        if (!sassPackage.accountInstructorsCount.isNullOrEmpty()) {
            items.add(object : CommonItem {
                override fun title(context: Context): String {
                    return sassPackage.accountInstructorsCount!!
                }

                override fun img(): String? {
                    return null
                }

                override fun desc(context: Context): String {
                    return getString(R.string.instructors)
                }

                override fun imgResource(): Int {
                    return R.drawable.ic_profile_dark_gray
                }

                override fun imgBgResource(): Int {
                    return R.drawable.circle_light_dark_gray
                }
            })
        }

        mBinding.saasPackageAccountStatisticsRv.adapter = CardStatisticsRvAdapter(items)
    }

    fun onItemSelected(saasPackageItem: SaasPackageItem) {
        mLoadingDialog = LoadingDialog.instance
        mLoadingDialog.show(childFragmentManager, null)

        saasPackageItem.packageId = saasPackageItem.id

        mPresenter.checkoutSubscription(saasPackageItem)
    }
}