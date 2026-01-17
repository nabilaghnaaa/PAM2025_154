package com.example.saveby.view.product

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.vector.ImageVector
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
fun HalamanTambahProduk(
    onBack: () -> Unit,
    onSimpan: (Product) -> Unit,
    isLoading: Boolean = false
) {
    val context = LocalContext.current

    var nama by remember { mutableStateOf("") }
    var jumlah by remember { mutableStateOf("") }
    var satuan by remember { mutableStateOf("") }
    var lokasi by remember { mutableStateOf("") }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Minuman Kemasan") }

    val categories = listOf(
        "Minuman Kemasan",
        "Snack & Produk Kering",
        "Makanan Instan & Kaleng",
        "Frozen Kemasan",
        "Produk Sensitif",
        "Bumbu dan Bahan"
    )

    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    var showDatePicker by remember { mutableStateOf(false) }
    if (showDatePicker) {
        val state = rememberDatePickerState()
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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tambah ke Inventaris", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clickable { galleryLauncher.launch("image/*") },
                shape = RoundedCornerShape(28.dp)
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Add, null, tint = Color.Gray, modifier = Modifier.size(40.dp))
                            Text("Upload Foto (Opsional)", color = Color.Gray)
                        }
                    }
                }
            }

            Text("Kategori Produk", fontSize = 12.sp, color = Color.Gray)

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    leadingIcon = { Icon(Icons.Default.List, null) },
                    shape = RoundedCornerShape(14.dp)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                selectedCategory = item
                                expanded = false
                            }
                        )
                    }
                }
            }

            CustomField("Nama Produk", nama, Icons.Default.Edit) { nama = it }
            CustomField("Lokasi Penyimpanan", lokasi, Icons.Default.Place) { lokasi = it }

            OutlinedTextField(
                value = jumlah,
                onValueChange = { jumlah = it.filter { c -> c.isDigit() } },
                label = { Text("Jumlah") },
                leadingIcon = { Icon(Icons.Default.ShoppingCart, null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = satuan,
                onValueChange = { satuan = it },
                label = { Text("Satuan (botol / pcs / gram)") },
                leadingIcon = { Icon(Icons.Default.Info, null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true
            )
            Surface(
                modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color.LightGray.copy(0.3f))
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, null)
                    Spacer(Modifier.width(12.dp))
                    Text(
                        if (selectedDateMillis != null)
                            sdf.format(Date(selectedDateMillis!!))
                        else "Pilih tanggal kedaluwarsa"
                    )
                }
            }

            Button(
                onClick = {
                    onSimpan(
                        Product(
                            productName = nama,
                            expiredDate = selectedDateMillis?.let { Date(it) },
                            quantity = jumlah.toIntOrNull() ?: 0,
                            unit = satuan,
                            location = lokasi,
                            photoUrl = imageUri?.let { saveToInternal(context, it) },
                            category = selectedCategory
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(8.dp, RoundedCornerShape(18.dp)),
                enabled = nama.isNotBlank()
                        && jumlah.isNotBlank()
                        && satuan.isNotBlank()
                        && selectedDateMillis != null,
                shape = RoundedCornerShape(18.dp)
            ) {
                if (isLoading) CircularProgressIndicator()
                else Text("SIMPAN PRODUK", fontWeight = FontWeight.Bold)
            }
        }
    }
}


fun saveToInternal(context: Context, uri: Uri): String? {
    return try {
        val file = File(context.filesDir, "img_${System.currentTimeMillis()}.jpg")
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output -> input.copyTo(output) }
        }
        file.absolutePath
    } catch (e: Exception) {
        null
    }
}

@Composable
fun CustomField(label: String, value: String, icon: ImageVector, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, null) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        singleLine = true
    )
}
