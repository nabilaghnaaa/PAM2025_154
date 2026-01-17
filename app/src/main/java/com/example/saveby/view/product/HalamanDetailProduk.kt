package com.example.saveby.view.product

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.saveby.R
import com.example.saveby.modeldata.Product
import com.example.saveby.modeldata.ProductStatus
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanDetailProduk(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onOpened: (Date, String, String) -> Unit,
    onFinalStatus: (ProductStatus) -> Unit,
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))

    var showSheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showConfirmFinal by remember { mutableStateOf(false) }
    var selectedFinalStatus by remember { mutableStateOf<ProductStatus?>(null) }

    var showOpenedDialog by remember { mutableStateOf(false) }
    var openedDate by remember { mutableStateOf<Long?>(null) }
    var selectedStorage by remember { mutableStateOf("Suhu Ruangan") }
    var newLocation by remember { mutableStateOf(product.location) }
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = { Icon(Icons.Default.Delete, null, tint = Color.Red) },
            title = { Text("Hapus Produk?") },
            text = { Text("Produk akan dihapus permanen dari inventaris.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) { Text("Hapus", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Batal") }
            }
        )
    }

    if (showConfirmFinal && selectedFinalStatus != null) {
        AlertDialog(
            onDismissRequest = { showConfirmFinal = false },
            title = { Text("Konfirmasi") },
            text = { Text("Produk akan dipindahkan ke riwayat stok.") },
            confirmButton = {
                Button(onClick = {
                    showConfirmFinal = false
                    onFinalStatus(selectedFinalStatus!!)
                }) { Text("Ya, Simpan") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmFinal = false }) { Text("Batal") }
            }
        )
    }

    if (showOpenedDialog) {
        AlertDialog(
            onDismissRequest = { showOpenedDialog = false },
            shape = RoundedCornerShape(28.dp),
            containerColor = Color.White,
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Refresh, null, tint = Color(0xFF5E72E4), modifier = Modifier.size(40.dp))
                    Spacer(Modifier.height(8.dp))
                    Text("Buka Kemasan", fontWeight = FontWeight.Black, fontSize = 20.sp)
                    Text("Hitung ulang masa simpan produk", fontSize = 14.sp, color = Color.Gray)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {

                    Text("Lokasi suhu penyimpanan:", fontWeight = FontWeight.Bold)

                    val options = listOf(
                        "Suhu Ruangan" to Icons.Default.Home,
                        "Kulkas" to Icons.Default.ShoppingCart,
                        "Freezer" to Icons.Default.Warning
                    )

                    options.forEach { (name, icon) ->
                        val isSelected = selectedStorage == name
                        Surface(
                            onClick = { selectedStorage = name },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) Color(0xFF5E72E4).copy(alpha = 0.1f) else Color.Transparent,
                            border = BorderStroke(1.dp, if (isSelected) Color(0xFF5E72E4) else Color.LightGray.copy(0.5f)),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(icon, null, tint = if (isSelected) Color(0xFF5E72E4) else Color.Gray)
                                Spacer(Modifier.width(12.dp))
                                Text(name, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                Spacer(Modifier.weight(1f))
                                RadioButton(selected = isSelected, onClick = { selectedStorage = name })
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                    Text("Lokasi fisik terbaru:", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = newLocation,
                        onValueChange = { newLocation = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("contoh: freezer pintu atas") },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(Modifier.height(12.dp))
                    Text("Kapan produk dibuka?", fontWeight = FontWeight.Bold)

                    OutlinedButton(
                        onClick = { showDatePicker = true },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.DateRange, null)
                        Spacer(Modifier.width(8.dp))
                        Text(if (openedDate != null) sdf.format(Date(openedDate!!)) else "Pilih Tanggal")
                    }
                }
            },
            confirmButton = {
                Button(
                    enabled = openedDate != null && newLocation.isNotBlank(),
                    onClick = {
                        onOpened(Date(openedDate!!), selectedStorage, newLocation)
                        showOpenedDialog = false
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E72E4))
                ) { Text("Update Produk", fontWeight = FontWeight.Bold) }
            }
        )
    }

    if (showDatePicker) {
        val state = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    openedDate = state.selectedDateMillis
                    showDatePicker = false
                }) { Text("OK") }
            }
        ) { DatePicker(state = state) }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FF))) {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {

            Box(Modifier.fillMaxWidth().height(360.dp)) {
                if (!product.photoUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context).data(File(product.photoUrl!!)).crossfade(true).build(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        error = painterResource(R.drawable.defaultproduct)
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.defaultproduct),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Black.copy(.4f), Color.Transparent))))

                Row(Modifier.fillMaxWidth().padding(48.dp, 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    IconButton(onClick = onBack, modifier = Modifier.background(Color.White.copy(.3f), CircleShape)) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                    IconButton(onClick = onEdit, modifier = Modifier.background(Color.White.copy(.3f), CircleShape)) {
                        Icon(Icons.Default.Edit, null, tint = Color.White)
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth().offset(y = (-30).dp),
                shape = RoundedCornerShape(32.dp),
                color = Color.White
            ) {
                Column(Modifier.padding(24.dp)) {
                    Text(product.productName, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)

                    Surface(
                        modifier = Modifier.padding(top = 8.dp),
                        color = Color(0xFF5E72E4).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = product.category,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            color = Color(0xFF5E72E4),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    DetailItem(Icons.Default.List, "Kategori Produk", product.category)
                    DetailItem(Icons.Default.DateRange, "Tanggal Kedaluwarsa", product.expiredDate?.let { sdf.format(it) } ?: "-")
                    DetailItem(Icons.Default.Place, "Lokasi Penyimpanan", product.location)
                    DetailItem(Icons.Default.ShoppingCart, "Jumlah Stok", "${product.quantity} ${product.unit}")
                    DetailItem(Icons.Default.Info, "Status Saat Ini", product.status.toString())

                    Spacer(Modifier.height(160.dp))
                }
            }
        }

        Column(
            modifier = Modifier.align(Alignment.BottomCenter).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Button(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE53935),
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Default.Delete, null)
                Spacer(Modifier.width(8.dp))
                Text("Hapus Produk", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { showSheet = true },
                modifier = Modifier.fillMaxWidth().height(60.dp).shadow(8.dp, RoundedCornerShape(18.dp)),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E72E4))
            ) {
                Icon(Icons.Default.Settings, null)
                Spacer(Modifier.width(10.dp))
                Text("UBAH STATUS PRODUK", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(onDismissRequest = { showSheet = false }) {
            Column(Modifier.padding(24.dp)) {
                Text("Update Progress Produk", fontWeight = FontWeight.Black, fontSize = 20.sp)
                Spacer(Modifier.height(16.dp))

                StatusCard("Telah Dibuka","Hitung ulang masa simpan (Open Date)",Icons.Default.Refresh,Color(0xFF5E72E4)) {
                    showSheet = false
                    showOpenedDialog = true
                }

                StatusCard("Telah Habis","Produk sudah dikonsumsi sepenuhnya",Icons.Default.CheckCircle,Color(0xFF2DCE89)) {
                    selectedFinalStatus = ProductStatus.CONSUMED
                    showSheet = false
                    showConfirmFinal = true
                }

                StatusCard("Dibuang / Rusak","Produk sudah tidak layak dan dibuang",Icons.Default.Clear,Color(0xFFF5365C)) {
                    selectedFinalStatus = ProductStatus.WASTED
                    showSheet = false
                    showConfirmFinal = true
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}


@Composable
fun DetailItem(icon: ImageVector, title: String, content: String) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(42.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFF0F3FF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Color(0xFF5E72E4), modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, fontSize = 11.sp, color = Color.Gray)
            Text(content, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun StatusCard(title: String, desc: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = .1f),
        border = BorderStroke(1.dp, color.copy(.2f))
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).background(color.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = color)
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, color = color)
                Text(desc, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}
