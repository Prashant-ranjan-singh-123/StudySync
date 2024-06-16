package com.appzeto.lms.ui.frag.course

import com.appzeto.lms.model.AddToFav
import com.appzeto.lms.model.Course

class NormalCourseAddToFav(val course: Course) : BaseCourseAddToFav() {

    override fun getAddToFavItem(): AddToFav {
        val addToFav = AddToFav()
        addToFav.itemId = course.id
        addToFav.itemName = AddToFav.ItemType.WEBINAR.value
        return addToFav
    }
}