package com.example.saveby.modeldata

/**
 * Berdasarkan SRS Bab 2.2: Manajemen Status Produk
 */
enum class ProductStatus {
    ACTIVE,   // Produk belum dibuka
    OPENED,   // Produk sudah dibuka (FR-22)
    CONSUMED, // Produk habis (dipindah ke riwayat - FR-24)
    WASTED    // Produk dibuang (dipindah ke riwayat - FR-25)
}