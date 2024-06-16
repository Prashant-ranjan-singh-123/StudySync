package com.appzeto.lms.ui.frag.course

import com.appzeto.lms.databinding.FragCourseDetailsInformationBinding
import com.appzeto.lms.model.view.CourseCommonItem

abstract class BaseCourseDetailsInformation() {
    abstract fun getInfoList(): ArrayList<CourseCommonItem>
    abstract fun setMarkInfo(binding: FragCourseDetailsInformationBinding)
}