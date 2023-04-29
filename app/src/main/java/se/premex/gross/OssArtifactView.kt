package se.premex.gross

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import se.premex.gross.oss.R
import se.premex.gross.ui.AssetLicenseParser
import se.premex.gross.ui.ErrorView
import se.premex.gross.ui.LoadingView
import se.premex.gross.ui.OssView
import se.premex.gross.ui.OssViewState
import se.premex.gross.ui.State
import java.io.IOException

@Composable
fun AssetsOssView() {
    val assetManager = LocalContext.current.assets

    val licenseParser = remember { AssetLicenseParser(assetManager) }

    val uiState = remember { mutableStateOf(OssViewState()) }

    LaunchedEffect(key1 = assetManager) {
        try {
            uiState.value =
                OssViewState(viewState = State.Success(data = licenseParser.readFromAssets()))
        } catch (ioException: IOException) {
            uiState.value =
                OssViewState(
                    viewState = State.Failed(
                        errorMessage = ioException.localizedMessage ?: ""
                    )
                )
        }
    }

    when (val state = uiState.value.viewState) {
        is State.Failed -> ErrorView(stringResource(id = R.string.error), state.errorMessage)
        is State.Loading -> LoadingView(stringResource(id = R.string.loading))
        is State.Success -> {
            Column {
                Text(text = stringResource(id = se.premex.gross.R.string.assetBased))

                OssView(state.data)
            }
        }
    }
}