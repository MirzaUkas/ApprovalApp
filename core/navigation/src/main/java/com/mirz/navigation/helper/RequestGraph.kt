package com.mirz.navigation.helper

import kotlinx.serialization.Serializable


@Serializable
sealed class RequestGraph {
    @Serializable
    data object RequestLandingRoute : RequestGraph()

    @Serializable
    data class RequestDetailRoute(val data: RequestArgument) : RequestGraph() {
        @Serializable
        data class RequestArgument(
            val id: Int,
        ) : RequestGraph()

        companion object {
            val typeMap = mapOf(generateCustomNavType<RequestArgument>())
        }
    }
}