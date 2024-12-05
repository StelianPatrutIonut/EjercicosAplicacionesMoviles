package com.example.basededatos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

// Anotación que indica que esta interfaz es un DAO (Data Access Object) Room
@Dao
interface UserDao {

    // Consulta para obtener todos los usuarios de la tabla 'user'
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    // Consulta para obtener usuarios por sus IDs
    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    // Consulta para encontrar un usuario por su nombre y apellido
    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    // Anotación para insertar uno o más usuarios en la tabla 'user'
    @Insert
    fun insertAll(vararg users: User)

    // Anotación para eliminar un usuario de la tabla 'user'
    @Delete
    fun delete(user: User)
}
