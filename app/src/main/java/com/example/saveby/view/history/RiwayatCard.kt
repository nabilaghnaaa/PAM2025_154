package com.example.saveby.view.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.saveby.R
import com.example.saveby.modeldata.FinalStatus
import com.example.saveby.modeldata.ProductHistory
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RiwayatCard(
    history: ProductHistory,
    onDelete: (Int) -> Unit
) {
    val context = LocalContext.current
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))

    // HABIS -> Biru Indigo (Selesai), TERBUANG -> Merah (Gagal)
    val colorTheme = if (history.finalStatus == FinalStatus.CONSUMED) Color(0xFF5E72E4) else Color(0xFFF5365C)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(6.dp)
                    .background(colorTheme)
            )

            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // FOTO PRODUK
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF8F9FA))
                ) {
                    if (!history.photoUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(File(history.photoUrl))
                                .crossfade(true)
                                .build(),
                            contentDescription = history.productName,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            error = painterResource(R.drawable.defaultproduct2)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.defaultproduct2),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = history.productName.ifBlank { "Produk" },
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        Surface(
                            color = colorTheme.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (history.finalStatus == FinalStatus.CONSUMED) "HABIS" else "TERBUANG",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                color = colorTheme,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            )
                        }
                    }

                    // FIX: Menggunakan Icons.Default.DateRange (Pasti Ada)
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                        Icon(Icons.Default.DateRange, null, modifier = Modifier.size(12.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Terpakai: ${history.quantity}", fontSize = 12.sp, color = Color.DarkGray)
                    }

                    Text(
                        text = "Dicatat: ${history.actionDate?.let { sdf.format(it) } ?: "-"}",
                        fontSize = 11.sp,
                        color = Color.LightGray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                IconButton(
                    onClick = { onDelete(history.historyId) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.Delete, null, tint = Color.LightGray.copy(0.6f), modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}