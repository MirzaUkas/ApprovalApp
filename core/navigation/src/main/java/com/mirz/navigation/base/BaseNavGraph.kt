package com.mirz.navigation.base

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface BaseNavGraph {
    fun NavGraphBuilder.createGraph(navController: NavController)
}