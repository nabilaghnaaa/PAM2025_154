package com.example.saveby.view.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.saveby.modeldata.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanDashboard(
    userName: String,
    products: List<Product>,
    consumedCount: Int,
    wastedCount: Int,
    onAddClick: () -> Unit,
    onProductClick: (Product) -> Unit,
    onConsumedClick: () -> Unit,
    onWastedClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSearch: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }

    val categories = remember(products) {
        listOf("Semua") + products.map { it.category }.distinct().sorted()
    }

    val filteredProducts = remember(products, searchQuery, selectedCategory) {
        products.filter {
            val matchesSearch = it.productName.contains(searchQuery, true)
            val matchesCategory =
                if (selectedCategory == "Semua") true else it.category == selectedCategory
            matchesSearch && matchesCategory
        }.sortedWith(compareBy({ it.priority }, { it.daysLeft }))
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = Color(0xFF5E72E4),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.shadow(12.dp, CircleShape)
            ) {
                Icon(Icons.Default.Add, null, modifier = Modifier.size(30.dp))
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F7FE))
                .padding(padding)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .background(Color(0xFFF4F7FE))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(bottomStart = 22.dp, bottomEnd = 22.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF5E72E4), Color(0xFF825EE4))
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Halo, $userName 👋",
                                fontWeight = FontWeight.Black,
                                fontSize = 24.sp,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                "Ayo cegah makanan terbuang!",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 14.sp
                            )
                        }

                        IconButton(
                            onClick = onProfileClick,
                            modifier = Modifier
                                .size(46.dp)
                                .background(Color.White, CircleShape)
                        ) {
                            Icon(Icons.Default.Person, null, tint = Color(0xFF5E72E4))
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = 12.dp)
                            .shadow(10.dp, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White
                    ) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it; onSearch(it) },
                            placeholder = { Text("Cari stok makananmu...", color = Color.LightGray) },
                            leadingIcon = {
                                Icon(Icons.Default.Search, null, tint = Color(0xFF5E72E4))
                            },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatsPro(
                    "Terpakai",
                    consumedCount,
                    Icons.Default.CheckCircle,
                    Brush.linearGradient(listOf(Color(0xFF2DCE89), Color(0xFF2DCECC))),
                    onConsumedClick
                )

                StatsPro(
                    "Terbuang",
                    wastedCount,
                    Icons.Default.Delete,
                    Brush.linearGradient(listOf(Color(0xFFF5365C), Color(0xFFFB6340))),
                    onWastedClick
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "Pilih Kategori",
                modifier = Modifier.padding(horizontal = 24.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(Modifier.height(10.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(categories) { category ->
                    CategoryChip(category, selectedCategory == category) {
                        selectedCategory = category
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                if (filteredProducts.isNotEmpty()) {
                    Surface(
                        color = Color(0xFF5E72E4).copy(alpha = 0.08f),
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text(
                            text = "Menampilkan ${filteredProducts.size} Produk",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5E72E4),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            if (filteredProducts.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tidak ada produk ditemukan", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredProducts) { product ->
                        ProductItem(product = product) { onProductClick(product) }
                    }
                }
            }
        }
    }
}


@Composable
fun CategoryChip(name: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Color(0xFF5E72E4) else Color.White,
        shadowElevation = if (isSelected) 8.dp else 2.dp
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            color = if (isSelected) Color.White else Color(0xFF5E72E4),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun RowScope.StatsPro(
    title: String,
    value: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradient: Brush,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(110.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(gradient)
            .clickable { onClick() }
    ) {
        Icon(icon, null, modifier = Modifier.align(Alignment.TopEnd).padding(12.dp), tint = Color.White.copy(0.4f))
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value.toString(), color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Black)
            Text(title, color = Color.White.copy(0.9f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}