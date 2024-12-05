package com.example.appdefinitiva10

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
@Dao
interface UserDao {
    @Query("SELECT * FROM user") fun getAll(): List<User>
    // Consulta para obtener usuarios por sus IDs
    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE username = :userName LIMIT 1")
    fun findByName(userName: String): User?
    @Query("DELETE FROM user")
    fun deleteAllUsers()
    @Query("SELECT * FROM user WHERE username = :username AND password = :oldPassword LIMIT 1")
    fun findUserByUsernameAndPassword(username: String, oldPassword: String): User?

    @Query("UPDATE user SET password = :newPassword WHERE username = :username")
    fun updatePassword(username: String, newPassword: String)

    @Insert
    fun insertAll(vararg users: User)
    @Insert
    fun insert(user: User)


    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM user WHERE username = :userName AND password = :password LIMIT 1")
    fun encontrarUsuario(userName: String, password: String): User?


}