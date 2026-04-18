package com.mirz.request.screen.landing

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.mirz.approval.feature.request.R
import com.mirz.common.base.BaseScreen
import com.mirz.common.ui.theme.ErrorContainer
import com.mirz.common.ui.theme.ErrorContent
import com.mirz.common.ui.theme.NavyDark
import com.mirz.common.ui.theme.SuccessContainer
import com.mirz.common.ui.theme.SuccessContent
import com.mirz.data.model.entity.ProcessEntity
import com.mirz.navigation.helper.RequestGraph.RequestDetailRoute
import com.mirz.navigation.helper.getArgsWhenBackPressed
import com.mirz.navigation.helper.navigateTo
import com.mirz.request.screen.detail.RequestDetailViewModel

@Composable
internal fun RequestLandingScreen(
    viewModel: RequestDetailViewModel = hiltViewModel(),
    navController: NavController
) = with(viewModel) {
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(Unit) {
        val result = navController.getArgsWhenBackPressed<ProcessEntity>("process_result")
        result?.let {
            snackbarHostState.showSnackbar(it.message)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                val isError =
                    data.visuals.message.contains("reject", ignoreCase = true) ||
                    data.visuals.message.contains("error", ignoreCase = true) ||
                        data.visuals.message.contains("fail", ignoreCase = true)
                Snackbar(
                    snackbarData = data,
                    containerColor = if (isError) ErrorContainer else SuccessContainer,
                    contentColor = if (isError) ErrorContent else SuccessContent,
                    actionColor = if (isError) ErrorContent else SuccessContent,
                )
            }
        },
    ) { paddingValues ->
        BaseScreen(
            modifier = Modifier.padding(paddingValues),
            showDefaultTopBar = false
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = stringResource(R.string.new_request),
                    color = NavyDark,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 26.dp)
                )
                Image(
                    painter = painterResource(R.drawable.ic_via),
                    contentDescription = stringResource(R.string.via_logo),
                    modifier = Modifier.size(160.dp),
                )
                Button(
                    onClick = {
                        navController.navigateTo(
                            RequestDetailRoute(
                                RequestDetailRoute.RequestArgument(1)
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyDark)
                ) {
                    Text(
                        text = stringResource(R.string.home),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}