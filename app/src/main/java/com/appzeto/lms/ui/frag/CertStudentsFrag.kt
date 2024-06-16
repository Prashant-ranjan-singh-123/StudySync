package com.appzeto.lms.ui.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.appzeto.lms.R
import com.appzeto.lms.databinding.RvBinding
import com.appzeto.lms.manager.App
import com.appzeto.lms.manager.adapter.QuizResultRvAdapter
import com.appzeto.lms.model.QuizResult
import com.appzeto.lms.model.ToolbarOptions
import com.appzeto.lms.ui.MainActivity

class CertStudentsFrag : Fragment() {

    private lateinit var mBinding: RvBinding

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
        val toolbarOptions = ToolbarOptions()
        toolbarOptions.startIcon = ToolbarOptions.Icon.BACK

        (activity as MainActivity).showToolbar(toolbarOptions, R.string.certificate_students)

        mBinding.rvProgressBar.visibility = View.GONE
        val results = requireArguments().getParcelableArrayList<QuizResult>(App.RESULT)!!
        val adapter = QuizResultRvAdapter(results, false)
        mBinding.rv.layoutManager = LinearLayoutManager(requireContext())
        mBinding.rv.adapter = adapter
    }
}