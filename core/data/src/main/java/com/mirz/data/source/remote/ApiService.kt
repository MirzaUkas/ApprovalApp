package com.mirz.data.source.remote

import com.mirz.data.model.response.ProcessDataResponse
import com.mirz.data.model.response.RequestDataResponse
import kotlinx.coroutines.delay

interface ApiService {
    suspend fun getRequestData(): RequestDataResponse {
        delay(1_500)
        return RequestDataResponse(
            id = 1,
            title = "Heading 1",
            desc = "Lorem ipsum dolor sit amet consectetur. Arcu tincidunt vitae cras amet. " +
                    "Blandit id sed et est gravida. Eu sapien amet et volutpat ultrices sed. " +
                    "Euismod semper mi non vitae egestas sollicitudin aliquam.",
        )
    }

    suspend fun processApproval(id: Int, isApproved: Boolean): ProcessDataResponse {
        // Randomize succes or failed return
        val isFailed = (0..1).random() == 0

        if (isApproved) {
            delay(2_000)
            if (isFailed) throw Exception("Approval failed: server could not process the request at this time.")
            return ProcessDataResponse(id = id, message = "Request approved successfully.", success = true)
        } else {
            return ProcessDataResponse(
                id = id,
                message = if (isFailed) "Rejection failed: unable to reject the request." else "Request rejected successfully.",
                success = !isFailed
            )
        }
    }
}