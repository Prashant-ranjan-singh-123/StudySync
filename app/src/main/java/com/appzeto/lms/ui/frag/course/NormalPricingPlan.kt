package com.appzeto.lms.ui.frag.course

import com.appzeto.lms.model.AddToCart
import com.appzeto.lms.model.Course
import com.appzeto.lms.model.PricingPlan

class NormalPricingPlan(val course: Course) : BasePricingPlan() {

    override fun getAddToCartItem(plan: PricingPlan?): AddToCart {
        val addToCart = AddToCart()
        plan?.let { addToCart.pricingPlanId = it.id }
        addToCart.itemId = course.id
        addToCart.itemName = AddToCart.ItemType.WEBINAR.value
        return addToCart
    }

}