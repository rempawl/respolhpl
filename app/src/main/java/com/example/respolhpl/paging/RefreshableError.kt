package com.example.respolhpl.paging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.respolhpl.R

@Composable
fun LazyItemScope.ListItemRefreshableError(
    modifier: Modifier = Modifier,
    isLoadingVisible: Boolean,
    onRefreshClick: () -> Unit
) {
    RefreshableError(
        modifier = modifier.fillParentMaxHeight(0.8f), // 0.8 Makes it more centered
        showButtonAtBottom = false,
        onRefreshClick = onRefreshClick,
    )
}

@Composable
fun RefreshableError(
    modifier: Modifier = Modifier,
    showButtonAtBottom: Boolean = true,
    onRefreshClick: () -> Unit
) {
    Column(
        Modifier
            .padding(horizontal = 30.dp, vertical = 34.dp)
            .fillMaxSize()
            .then(modifier),
        verticalArrangement = if (showButtonAtBottom) {
            Arrangement.SpaceBetween
        } else {
            Arrangement.Center
        }
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.an_error_occurred),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.error_no_internet_connection_refresh),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = onRefreshClick,
            colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
        ) {
            Text(text = stringResource(R.string.retry))
        }
    }
}