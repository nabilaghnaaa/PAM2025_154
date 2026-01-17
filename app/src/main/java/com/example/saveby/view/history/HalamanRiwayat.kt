package com.example.saveby.view.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.saveby.modeldata.ProductHistory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanRiwayat(
    title: String,
    historyList: List<ProductHistory>,
    onBack: () -> Unit,
    onDeleteOne: (Int) -> Unit,
    onDeleteAll: () -> Unit
) {
    var showDeleteAllDialog by remember { mutableStateOf(false) }

    if (showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAllDialog = false },
            shape = RoundedCornerShape(24.dp),
            title = { Text("Reset Riwayat", fontWeight = FontWeight.Bold) },
            text = { Text("Semua catatan dalam kategori ini akan dihapus selamanya.") },
            confirmButton = {
                Button(
                    onClick = { onDeleteAll(); showDeleteAllDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5365C))
                ) { Text("Hapus Semua") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAllDialog = false }) { Text("Batal") }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.padding(8.dp).background(Color.White, CircleShape)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color(0xFF5E72E4))
                    }
                },
                actions = {
                    if (historyList.isNotEmpty()) {
                        IconButton(
                            onClick = { showDeleteAllDialog = true },
                            modifier = Modifier.padding(8.dp).background(Color(0xFFFFF1F1), CircleShape)
                        ) {
                            Icon(Icons.Default.Delete, null, tint = Color(0xFFF5365C))
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFF4F7FE))
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF4F7FE)).padding(padding)
        ) {
            if (historyList.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Refresh, null, modifier = Modifier.size(80.dp), tint = Color.LightGray.copy(0.4f))
                    Spacer(Modifier.height(12.dp))
                    Text("Riwayat masih kosong", color = Color.LightGray)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text(
                            text = "${historyList.size} Produk tersimpan dalam riwayat",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                        )
                    }
                    items(historyList) { item ->
                        RiwayatCard(history = item, onDelete = { onDeleteOne(it) })
                    }
                }
            }
        }
    }
}