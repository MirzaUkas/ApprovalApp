package com.mirz.request.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.mirz.approval.feature.request.R
import com.mirz.common.ui.component.AppSlideButton
import com.mirz.common.ui.theme.NavyDark
import com.mirz.common.ui.theme.NavyMedium
import com.mirz.common.ui.theme.TextSecondary
import com.mirz.common.utils.state.collectAsStateValue
import com.mirz.data.model.entity.ProcessEntity
import com.mirz.data.model.entity.RequestEntity
import com.mirz.navigation.helper.setBackPressedWithArgs

@Composable
fun RequestDetailScreen(
    viewModel: RequestDetailViewModel = hiltViewModel(),
    navController: NavController
) = with(viewModel) {
    val uiState = uiState.collectAsStateValue()
    var requestData by remember { mutableStateOf(RequestEntity.default()) }

    LaunchedEffect(Unit) { getRequest(args.data.id) }

    LaunchedEffect(uiState.processState) {
        uiState.processState.handleUiState(
            onSuccess = {
                navController.setBackPressedWithArgs<ProcessEntity>("process_result", it)
            },
            onFailed = {
                navController.setBackPressedWithArgs<ProcessEntity>(
                    "process_result",
                    ProcessEntity(
                        message = it.localizedMessage.orEmpty().ifEmpty { "Unknown Error" }
                    )
                )
            }
        )
    }

    LaunchedEffect(uiState.requestState) {
        uiState.requestState.handleUiState(
            onSuccess = {
                requestData = it
            }
        )
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyDark)
                    .padding(all = 24.dp),
                contentAlignment = Alignment.BottomStart,
            ) {
                Text(
                    text = stringResource(R.string.new_request),
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            // Content card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(NavyMedium)
                    .padding(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    Text(
                        text = requestData.title,
                        color = NavyDark,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = requestData.desc,
                        color = TextSecondary,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                    )
                }
            }

            // Action bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyMedium)
                    .padding(start = 16.dp, end = 16.dp, bottom = 32.dp, top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedButton(
                    onClick = {
                        rejectRequest(requestData.id)
                    },
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    enabled = !uiState.requestState.isLoading(),
                    modifier = Modifier.height(52.dp),
                ) {
                    Text(text = stringResource(R.string.reject), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }

                AppSlideButton(
                    onApproved = {
                        approveRequest(requestData.id)
                    },
                    isEnabled = !uiState.requestState.isLoading(),
                    modifier = Modifier.weight(1f),
                )
            }
        }

        // Full-screen loading overlay — disables all interactions while pending
        if (uiState.requestState.isLoading() || uiState.processState.isLoading()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = Color.White,
                    strokeWidth = 3.dp,
                )
            }
        }
    }
}
