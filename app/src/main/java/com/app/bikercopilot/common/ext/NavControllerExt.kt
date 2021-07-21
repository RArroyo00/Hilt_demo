package com.app.bikercopilot.common.ext

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions

fun NavController.navigateSafe(resId: Int, bundle: Bundle? = null) {
    currentDestination?.getAction(resId)?.let {
        navigate(resId, bundle)
    }
}

fun NavController.navigateSafe(
    resId: Int,
    bundle: Bundle? = null,
    navOptions: NavOptions?,
    action: () -> Unit
) {
    currentDestination?.getAction(resId)?.let {
        action()
        navigate(resId, bundle, navOptions)
    }
}