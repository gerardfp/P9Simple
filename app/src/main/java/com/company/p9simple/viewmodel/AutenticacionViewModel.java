package com.company.p9simple.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import com.company.p9simple.db.AppDao;
import com.company.p9simple.db.AppDatabase;
import com.company.p9simple.model.Usuario;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class AutenticacionViewModel extends AndroidViewModel {

    public enum EstadoDeLaAutenticacion {
        NO_AUTENTICADO,
        AUTENTICADO,
        AUTENTICACION_INVALIDA
    }

    public enum EstadoDelRegistro {
        INICIO_DEL_REGISTRO,
        NOMBRE_NO_DISPONIBLE,
        REGISTRO_COMPLETADO
    }

    private AppDao appDao;

    public MutableLiveData<EstadoDeLaAutenticacion> estadoDeLaAutenticacion = new MutableLiveData<>();
    public MutableLiveData<EstadoDelRegistro> estadoDelRegistro = new MutableLiveData<>();
    public Usuario usuarioLogeado;
    public Usuario usuarioRegistrado;

    public AutenticacionViewModel(@NonNull Application application) {
        super(application);
        appDao = AppDatabase.getInstance(application).dao();

        estadoDeLaAutenticacion.setValue(EstadoDeLaAutenticacion.NO_AUTENTICADO);
        estadoDelRegistro.setValue(EstadoDelRegistro.INICIO_DEL_REGISTRO);
    }

    public void crearCuentaYLogear(final String nombre, final String contrasenya, final String biografia) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Usuario usuario = appDao.comprobarNombreDisponible(nombre);
                if(usuario == null){
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            Usuario newUsuario = new Usuario(nombre, contrasenya, biografia);
                            appDao.insertarUsuario(newUsuario);
                            usuarioRegistrado = newUsuario;
                            estadoDelRegistro.postValue(EstadoDelRegistro.REGISTRO_COMPLETADO);
                        }
                    });
                } else {
                    estadoDelRegistro.postValue(EstadoDelRegistro.NOMBRE_NO_DISPONIBLE);
                }
            }
        });
    }

    public MutableLiveData<EstadoDeLaAutenticacion> entrar(final String nombre, final String contrasenya) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Usuario usuario = appDao.autenticar(nombre, contrasenya);
                if(usuario != null){
                    usuarioLogeado = usuario;
                    estadoDeLaAutenticacion.postValue(EstadoDeLaAutenticacion.AUTENTICADO);
                } else {
                    estadoDeLaAutenticacion.postValue(EstadoDeLaAutenticacion.AUTENTICACION_INVALIDA);
                }
            }
        });

        return estadoDeLaAutenticacion;
    }

    public MutableLiveData<EstadoDeLaAutenticacion> salir() {
        usuarioLogeado = null;
        estadoDeLaAutenticacion.setValue(EstadoDeLaAutenticacion.NO_AUTENTICADO);

        return estadoDeLaAutenticacion;
    }
}