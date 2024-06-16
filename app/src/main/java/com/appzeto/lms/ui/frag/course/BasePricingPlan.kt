package com.appzeto.lms.ui.frag.course

import com.appzeto.lms.model.AddToCart
import com.appzeto.lms.model.PricingPlan

abstract class BasePricingPlan {
    abstract fun getAddToCartItem(plan: PricingPlan?) : AddToCart
}