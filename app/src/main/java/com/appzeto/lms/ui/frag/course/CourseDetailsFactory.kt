package com.appzeto.lms.ui.frag.course

import com.appzeto.lms.model.Course

class CourseDetailsFactory {
    companion object {
        fun getDetails(course: Course): BaseCourseDetails {
            if (course.isBundle()) {
                return BundleCourseDetails(course)
            }

            return NormalCourseDetails(course)
        }
    }
}