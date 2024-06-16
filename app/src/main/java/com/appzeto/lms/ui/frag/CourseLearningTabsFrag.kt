package com.appzeto.lms.ui.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.appzeto.lms.R
import com.appzeto.lms.databinding.TabBinding
import com.appzeto.lms.manager.App
import com.appzeto.lms.manager.adapter.ViewPagerAdapter
import com.appzeto.lms.model.Course
import com.appzeto.lms.model.Quiz
import com.appzeto.lms.model.QuizResult
import com.appzeto.lms.model.ToolbarOptions
import com.appzeto.lms.ui.MainActivity
import com.appzeto.lms.ui.SplashScreenActivity

class CourseLearningTabsFrag : Fragment() {

    private lateinit var mBinding: TabBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = TabBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val course = requireArguments().getParcelable<Course>(App.COURSE)!!
        val offlineMode = requireArguments().getBoolean(App.OFFLINE)

        if (offlineMode) {
            (activity as SplashScreenActivity).showToolbar(course.title)
        } else {
            val toolbarOptions = ToolbarOptions()
            toolbarOptions.startIcon = ToolbarOptions.Icon.BACK

            (activity as MainActivity).showToolbar(toolbarOptions, course.title)
        }

        val tabLayout = mBinding.tabLayout
        val viewPager = mBinding.viewPager

        val adapter = ViewPagerAdapter(childFragmentManager)

        val bundle = Bundle()
        bundle.putParcelable(App.COURSE, course)
        bundle.putBoolean(App.OFFLINE, offlineMode)

        val contentFrag: Fragment

        if (offlineMode) {
            val informationFrag = CourseDetailsInformationFrag()
            informationFrag.arguments = bundle
            adapter.add(informationFrag, getString(R.string.information))

            contentFrag = OfflineCourseLearningContentFrag()
        } else {
            contentFrag = CourseLearningContentFrag()
        }

        contentFrag.arguments = bundle
        adapter.add(contentFrag, getString(R.string.content))

        if (!offlineMode) {
            if (course.quizzes.isNotEmpty()) {
                val quizzesFrag = CourseLearningQuizzesFrag()
                quizzesFrag.arguments = bundle
                adapter.add(quizzesFrag, getString(R.string.quizzes))
            }

            val certificates = course.certificates

            if (certificates.isNotEmpty()) {
                val actualCerts = ArrayList<Quiz>()

                for (cert in certificates) {
                    if (cert.authStatus == QuizResult.Result.PASSED.value) {
                        cert.isCertificate = true
                        actualCerts.add(cert)
                    }
                }

                if (actualCerts.isNotEmpty()) {
                    val certficatesFrag = CourseLearningCertificatesFrag()
                    certficatesFrag.arguments = bundle
                    adapter.add(certficatesFrag, getString(R.string.certificates))
                }
            }

            val noticesBundle = Bundle()
            noticesBundle.putInt(App.ID, course.id)

            val noticesFrag = NoticesFrag()
            noticesFrag.arguments = noticesBundle
            adapter.add(noticesFrag, getString(R.string.notices))

            val forumsFrag = ForumsFrag()
            forumsFrag.arguments = bundle
            adapter.add(forumsFrag, getString(R.string.forums))
        }

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }
}