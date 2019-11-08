package com.company.p9simple.db;

import com.company.p9simple.model.Usuario;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public abstract class AppDao {
    @Insert
    public abstract void insertarUsuario(Usuario usuario);

    @Query("SELECT * FROM Usuario WHERE nombre = :nombre AND contrasenya = :contrasenya")
    public abstract LiveData<Usuario> autenticar(String nombre, String contrasenya);

    @Query("SELECT * FROM Usuario WHERE nombre = :nombre")
    public abstract LiveData<Usuario> comprobarNombreDiponible(String nombre);
}
