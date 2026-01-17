package com.example.saveby.view.product

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.saveby.modeldata.Product
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanEditProduk(
    product: Product,
    onUpdate: (Product) -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit,
    isLoading: Boolean = false
) {
    val context = LocalContext.current
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

    var nama by remember { mutableStateOf(product.productName) }
    var jumlah by remember { mutableStateOf(product.quantity.toString()) }
    var lokasi by remember { mutableStateOf(product.location) }
    var selectedDateMillis by remember { mutableStateOf(product.expiredDate?.time) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var currentPhotoPath by remember { mutableStateOf(product.photoUrl) }

    val categories = listOf(
        "Bumbu dan Bahan",
        "Minuman Kemasan",
        "Snack & Produk Kering",
        "Makanan Instan & Kaleng",
        "Frozen Kemasan",
        "Produk Sensitif"
    )
    var expandedCategory by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(product.category) }

    var showConfirmDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    var showDatePicker by remember { mutableStateOf(false) }
    if (showDatePicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDateMillis = state.selectedDateMillis
                    showDatePicker = false
                }) { Text("OK") }
            }
        ) { DatePicker(state = state) }
    }

    /* ================= DIALOG ================= */

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Simpan Perubahan?") },
            text = { Text("Data produk akan diperbarui sesuai dengan input baru Anda.") },
            confirmButton = {
                Button(onClick = {
                    showConfirmDialog = false
                    val finalPath =
                        if (imageUri != null) editSaveToInternal(context, imageUri!!)
                        else currentPhotoPath

                    onUpdate(
                        product.copy(
                            productName = nama,
                            category = selectedCategory,
                            expiredDate = selectedDateMillis?.let { Date(it) },
                            quantity = jumlah.toIntOrNull() ?: 0,
                            location = lokasi,
                            photoUrl = finalPath
                        )
                    )
                }) { Text("Simpan") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) { Text("Batal") }
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Produk?") },
            text = { Text("Tindakan ini tidak bisa dibatalkan. Produk akan dihapus permanen.") },
            confirmButton = {
                Button(
                    onClick = { showDeleteDialog = false; onDelete() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) { Text("Hapus", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Batal") }
            }
        )
    }

    /* ================= UI ================= */

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Produk", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFF8F9FF))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FF))
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(10.dp, RoundedCornerShape(24.dp))
                        .clickable { galleryLauncher.launch("image/*") },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    val displayModel =
                        if (imageUri != null) imageUri
                        else if (!currentPhotoPath.isNullOrBlank()) File(currentPhotoPath!!)
                        else null

                    if (displayModel != null) {
                        AsyncImage(displayModel, null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    } else {
                        Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Add, null, Modifier.size(40.dp), Color.Gray)
                            Text("Ubah Foto", color = Color.Gray)
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

                    Text("Informasi Produk", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF5E72E4))

                    OutlinedTextField(
                        value = nama,
                        onValueChange = { nama = it },
                        label = { Text("Nama Produk") },
                        leadingIcon = { Icon(Icons.Default.Edit, null, tint = Color(0xFF5E72E4)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    ExposedDropdownMenuBox(expanded = expandedCategory, onExpandedChange = { expandedCategory = !expandedCategory }) {
                        OutlinedTextField(
                            value = selectedCategory,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Kategori") },
                            leadingIcon = { Icon(Icons.Default.List, null, tint = Color(0xFF5E72E4)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedCategory) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )
                        ExposedDropdownMenu(expandedCategory, { expandedCategory = false }) {
                            categories.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat) },
                                    onClick = { selectedCategory = cat; expandedCategory = false }
                                )
                            }
                        }
                    }

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = jumlah,
                            onValueChange = { jumlah = it },
                            label = { Text("Jumlah") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        OutlinedTextField(
                            value = lokasi,
                            onValueChange = { lokasi = it },
                            label = { Text("Lokasi") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }

                    OutlinedTextField(
                        value = if (selectedDateMillis != null) sdf.format(Date(selectedDateMillis!!)) else "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tanggal Kedaluwarsa") },
                        leadingIcon = { Icon(Icons.Default.DateRange, null, tint = Color(0xFF5E72E4)) },
                        trailingIcon = { IconButton({ showDatePicker = true }) { Icon(Icons.Default.ArrowDropDown, null) } },
                        modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(Modifier.height(10.dp))

                    Button(
                        onClick = { showConfirmDialog = true },
                        modifier = Modifier.fillMaxWidth().height(56.dp).shadow(8.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E72E4))
                    ) {
                        if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        else Text("SIMPAN PERUBAHAN", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                        border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.5f))
                    ) {
                        Icon(Icons.Default.Delete, null)
                        Spacer(Modifier.width(8.dp))
                        Text("HAPUS PRODUK", fontWeight = FontWeight.Bold)
                    }

                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }
}

fun editSaveToInternal(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.filesDir, "edit_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
