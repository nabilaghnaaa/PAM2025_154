package com.example.saveby.view.controlNavigasi

object PetaNavigasi {

    const val LOGIN = "login"
    const val REGISTER = "register"

    const val DASHBOARD = "dashboard"

    const val ADD_PRODUCT = "add_product"
    const val DETAIL_PRODUCT = "detail_product/{productId}"

    fun detailProduct(productId: Int): String {
        return "detail_product/$productId"
    }

    const val HISTORY_CONSUMED = "history_consumed"
    const val HISTORY_WASTED = "history_wasted"

    const val PROFILE = "profile"
}
