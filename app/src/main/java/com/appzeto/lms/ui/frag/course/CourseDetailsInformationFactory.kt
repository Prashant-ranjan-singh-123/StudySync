package com.appzeto.lms.ui.frag.course

import android.content.Context
import com.appzeto.lms.model.Course

class CourseDetailsInformationFactory {
    companion object {
        fun getInformation(course: Course, context: Context): BaseCourseDetailsInformation {
            if (course.isBundle()) {
                return BundleCourseDetailsInformation(context, course)
            }

            return NormalCourseDetailsInformation(context, course)
        }
    }
}