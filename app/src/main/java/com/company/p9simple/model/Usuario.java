package com.company.p9simple.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Usuario {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
    public String contrasenya;
    public String biografia;

    public Usuario(String nombre, String contrasenya, String biografia) {
        this.nombre = nombre;
        this.contrasenya = contrasenya;
        this.biografia = biografia;
    }

    @NonNull
    @Override
    public String toString() {
        return "USER = " + nombre + " " + contrasenya + " " + biografia;
    }
}
