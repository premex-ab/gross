package se.premex.gross

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import se.premex.gross.oss.R
import se.premex.gross.ui.ErrorView
import se.premex.gross.ui.LoadingView
import se.premex.gross.ui.OpenSourceLicenseRepository
import se.premex.gross.ui.OssView
import se.premex.gross.ui.OssViewState
import se.premex.gross.ui.State
import java.io.IOException

@Composable
fun AssetsOssView(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val repository = remember { OpenSourceLicenseRepository(context) }

    var uiState by remember { mutableStateOf(OssViewState()) }

    LaunchedEffect(key1 = repository) {
        uiState = try {
            OssViewState(
                viewState = State.Success(
                    data = repository.getGroupedArtifacts()
                )
            )
        } catch (ioException: IOException) {
            OssViewState(
                viewState = State.Failed(
                    errorMessage = ioException.localizedMessage ?: ""
                )
            )
        }
    }

    when (val state = uiState.viewState) {
        is State.Failed -> ErrorView(stringResource(id = R.string.error), state.errorMessage)
        is State.Loading -> LoadingView(stringResource(id = R.string.loading))
        is State.Success -> {
            Column {
                Text(text = stringResource(id = se.premex.gross.R.string.assetBased))

                OssView(state.data, modifier)
            }
        }
    }
}