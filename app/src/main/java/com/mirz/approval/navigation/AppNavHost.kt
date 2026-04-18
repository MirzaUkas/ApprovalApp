package com.mirz.approval.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.mirz.navigation.base.BaseNavGraph
import com.mirz.navigation.helper.RequestGraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AppNavHost(navGraphs: Set<@JvmSuppressWildcards BaseNavGraph>) {
    val navController = rememberNavController()

    Box(contentAlignment = BottomCenter) {
        NavHost(
            modifier = Modifier
                .background(colorScheme.background)
                .fillMaxSize()
                .systemBarsPadding(),
            navController = navController,
            startDestination = RequestGraph.RequestLandingRoute::class
        ) {
            navGraphs.forEach { graph ->
                with(graph) { createGraph(navController) }
            }
        }
    }
}