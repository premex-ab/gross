@file:OptIn(ExperimentalFoundationApi::class)

package se.premex.gross.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import se.premex.gross.oss.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Suppress("FunctionNaming")
fun OssView(groupedArtifacts: ImmutableMap<String, ImmutableList<ViewArtifact>>, modifier: Modifier = Modifier) {
    var viewArtifact: ViewArtifact? by remember { mutableStateOf(null) }
    viewArtifact?.let {
        LicenseSelector(it) {
            viewArtifact = null
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 40.dp),
                    text = stringResource(id = R.string.oss_description)
                )
            }
        }

        groupedArtifacts.forEach { (title, list) ->
            stickyHeader {
                CharacterHeader(title)
            }
            items(list) { artifact ->
                ListItem(
                    headlineContent = {
                        Text(text = artifact.name)
                    },
                    modifier = Modifier.clickable {
                        viewArtifact = artifact
                    }
                )
            }
        }
    }
}

@Composable
@Suppress("FunctionNaming")
fun CharacterHeader(initial: String) {
    Text(
        modifier = Modifier.padding(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = 4.dp
        ),
        text = initial
    )
}

@Suppress("FunctionNaming", "Unused")
@Preview(showSystemUi = true)
@Composable
fun LicenseSelectorPreview() {
    Column(Modifier.fillMaxSize()) {
        val viewLicenseList =
            persistentListOf(ViewLicense("aaa", "se.coordinate:easypark:1.0.0", "http://google.se"))
        LicenseSelector(
            viewArtifact = ViewArtifact(
                "View",
                "Coordinate",
                viewLicenseList
            )
        ) {}
    }
}

@Composable
@Suppress("FunctionNaming")
fun LicenseSelector(viewArtifact: ViewArtifact, close: () -> Unit) {
    val uriHandler = LocalUriHandler.current

    if (viewArtifact.licenses.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = {
                close()
            },
            title = {
                Text(text = viewArtifact.name)
            },
            text = {
                Column {
                    viewArtifact.licenses.forEach { license ->
                        ListItem(
                            headlineContent = {
                                Text(text = license.title)
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Filled.Link,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.clickable {
                                uriHandler.openUri(license.url)
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = close
                ) {
                    Text(stringResource(id = R.string.close))
                }
            },
        )
    }
}
