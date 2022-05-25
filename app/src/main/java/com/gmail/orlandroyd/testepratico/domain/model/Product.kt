package com.gmail.orlandroyd.testepratico.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val title: String = "",
    val subtitle: String = "",
    val thumbnail: Int = 0
) : Parcelable