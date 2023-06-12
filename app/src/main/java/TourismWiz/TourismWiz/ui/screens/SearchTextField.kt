package TourismWiz.TourismWiz.ui.screens

import TourismWiz.TourismWiz.R
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun SearchTextField(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClearSearchQuery: () -> Unit
) {
    TextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        label = { Text(stringResource(id = R.string.keyword)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = onClearSearchQuery) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                    )
                }
            }
        }
    )
}