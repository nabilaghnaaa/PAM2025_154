package com.example.saveby.view.dashboard

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    TopAppBar(
        title = { Text("SaveBy") },
        actions = {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Cari produk...") },
                singleLine = true
            )
        }
    )
}
