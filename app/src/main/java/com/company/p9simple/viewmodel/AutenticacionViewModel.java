package com.company.p9simple.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.company.p9simple.db.AppDao;
import com.company.p9simple.db.AppDatabase;
import com.company.p9simple.model.Usuario;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

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
    public MutableLiveData<Usuario> autenticar = new MutableLiveData<>();
    public LiveData<Usuario> validarUsuario = Transformations.switchMap(autenticar, new Function<Usuario, LiveData<Usuario>>() {
        @Override
        public LiveData<Usuario> apply(Usuario input) {
            Log.e("ABCD", "validarUsuario " + input);
            return appDao.autenticar(input.nombre, input.contrasenya);
        }
    });
    public LiveData<Boolean> usuarioValidado = Transformations.switchMap(validarUsuario, new Function<Usuario, LiveData<Boolean>>() {
        @Override
        public LiveData<Boolean> apply(Usuario input) {
            Log.e("ABCD", "usuarioValidado " + input);
            if(input != null){
                estadoDeLaAutenticacion.setValue(EstadoDeLaAutenticacion.AUTENTICADO);
            }else{
                estadoDeLaAutenticacion.setValue(EstadoDeLaAutenticacion.NO_AUTENTICADO);
            }
            return null;
        }
    });

    public AutenticacionViewModel(@NonNull Application application) {
        super(application);
        appDao = AppDatabase.getInstance(application).dao();

        estadoDeLaAutenticacion.setValue(EstadoDeLaAutenticacion.NO_AUTENTICADO);
        estadoDelRegistro.setValue(EstadoDelRegistro.INICIO_DEL_REGISTRO);
    }

    public void crearCuentaEIniciarSesion(final String nombre, final String contrasenya, final String biografia) {
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                Usuario usuario = appDao.comprobarNombreDisponible(nombre);
//                if(usuario == null){
//                    AsyncTask.execute(new Runnable() {
//                        @Override
//                        public void run() {
//                            Usuario newUsuario = new Usuario(nombre, contrasenya, biografia);
//                            appDao.insertarUsuario(newUsuario);
//                            usuarioRegistrado = newUsuario;
//                            estadoDelRegistro.postValue(EstadoDelRegistro.REGISTRO_COMPLETADO);
//                            iniciarSesion(nombre, contrasenya);
//                        }
//                    });
//                } else {
//                    estadoDelRegistro.postValue(EstadoDelRegistro.NOMBRE_NO_DISPONIBLE);
//                }
//            }
//        });
    }

    public void iniciarSesion(final String nombre, final String contrasenya) {
        autenticar.setValue(new Usuario(nombre, contrasenya, ""));
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                Usuario usuario = appDao.autenticar(nombre, contrasenya);
//                if(usuario != null){
//                    usuarioLogeado = usuario;
//                    estadoDeLaAutenticacion.postValue(EstadoDeLaAutenticacion.AUTENTICADO);
//                } else {
//                    estadoDeLaAutenticacion.postValue(EstadoDeLaAutenticacion.AUTENTICACION_INVALIDA);
//                }
//            }
//        });
    }

    public void cerrarSesion() {
        usuarioLogeado = null;
        estadoDeLaAutenticacion.setValue(EstadoDeLaAutenticacion.NO_AUTENTICADO);
    }
}
