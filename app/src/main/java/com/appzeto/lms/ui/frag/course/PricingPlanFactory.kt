package com.appzeto.lms.ui.frag.course

import com.appzeto.lms.model.Course

class PricingPlanFactory {
    companion object {
        fun getPlan(course: Course): BasePricingPlan {
            if (course.isBundle()) {
                return BundlePricingPlan(course)
            }

            return NormalPricingPlan(course)
        }
    }
}