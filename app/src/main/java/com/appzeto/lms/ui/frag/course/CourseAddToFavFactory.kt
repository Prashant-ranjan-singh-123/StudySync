package com.appzeto.lms.ui.frag.course

import com.appzeto.lms.model.Course

class CourseAddToFavFactory {
    companion object {
        fun getAddToFavObj(course: Course): BaseCourseAddToFav {
            if (course.isBundle()) {
                return BundleCourseAddToFav(course)
            }

            return NormalCourseAddToFav(course)
        }
    }
}