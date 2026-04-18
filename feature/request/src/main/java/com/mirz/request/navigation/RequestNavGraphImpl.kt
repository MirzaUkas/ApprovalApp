package com.mirz.request.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.mirz.navigation.base.BaseNavGraph
import com.mirz.navigation.helper.RequestGraph.RequestDetailRoute
import com.mirz.navigation.helper.RequestGraph.RequestLandingRoute
import com.mirz.navigation.helper.composableScreen
import com.mirz.request.screen.detail.RequestDetailScreen
import com.mirz.request.screen.landing.RequestLandingScreen
import javax.inject.Inject

class RequestNavGraphImpl @Inject constructor() : BaseNavGraph {

    override fun NavGraphBuilder.createGraph(navController: NavController) {
        composableScreen<RequestLandingRoute> {
            RequestLandingScreen(
                navController = navController
            )
        }
        composableScreen<RequestDetailRoute>(
            customArgs = RequestDetailRoute.typeMap
        ) {
            RequestDetailScreen(
                navController = navController
            )
        }
    }
}