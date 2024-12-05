package com.example.basededatos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Anotación que indica que esta clase es una entidad Room
@Entity
data class User(
    // Anotación que especifica que 'uid' es la clave primaria de la entidad
    @PrimaryKey val uid: Int,

    // Anotación que especifica el nombre de la columna en la base de datos ('first_name')
    @ColumnInfo(name = "first_name") val firstName: String?,

    // Anotación que especifica el nombre de la columna en la base de datos ('last_name')
    @ColumnInfo(name = "last_name") val lastName: String?

)
