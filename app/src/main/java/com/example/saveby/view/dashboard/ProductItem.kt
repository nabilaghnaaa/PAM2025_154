package com.example.saveby.view.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import com.example.saveby.modeldata.Product
import java.io.File

@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {
    val sisaHari = product.daysLeft
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
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
                    .background(product.indicatorColor)
            )

            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(75.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF8F9FA))
                ) {
                    if (!product.photoUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(File(product.photoUrl))
                                .crossfade(true)
                                .build(),
                            contentDescription = product.productName,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            error = painterResource(R.drawable.defaultproduct2),
                            placeholder = painterResource(R.drawable.defaultproduct2)
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

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.productName,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = product.category,
                        fontSize = 12.sp,
                        color = Color(0xFF5E72E4),
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = product.location,
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        color = product.indicatorColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (sisaHari <= 0) "Expired" else "H-$sisaHari Hari",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            color = product.indicatorColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFFD1D1D1)
                )
            }
        }
    }
}