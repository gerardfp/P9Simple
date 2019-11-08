package com.company.p9simple.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

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

    public static class Autenticacion {
        public enum Estado {
            NO_AUTENTICADO,
            AUTENTICADO,
            AUTENTICACION_INVALIDA
        }
        public Estado estado;
        public Usuario usuario;

        Autenticacion(Estado estado) {
            this.estado = estado;
        }

        Autenticacion(Usuario usuario) {
            this.usuario = usuario;
        }

        Autenticacion(Estado estado, Usuario usuario) {
            this.estado = estado;
            this.usuario = usuario;
        }
    }

    public enum EstadoDelRegistro {
        INICIO_DEL_REGISTRO,
        NOMBRE_NO_DISPONIBLE,
        REGISTRO_COMPLETADO
    }

    private AppDao appDao;

    public MutableLiveData<EstadoDelRegistro> estadoDelRegistro = new MutableLiveData<>(EstadoDelRegistro.INICIO_DEL_REGISTRO);

    private MutableLiveData<Autenticacion> autenticar = new MutableLiveData<>(new Autenticacion(Autenticacion.Estado.NO_AUTENTICADO));

    private LiveData<Usuario> validarUsuario = Transformations.switchMap(autenticar, new Function<Autenticacion, LiveData<Usuario>>() {
        @Override
        public LiveData<Usuario> apply(Autenticacion autenticacion) {
            if(autenticacion.usuario == null){
                return new MutableLiveData<>(null);
            } else {
                return appDao.autenticar(autenticacion.usuario.nombre, autenticacion.usuario.contrasenya);
            }
        }
    });

    public LiveData<Autenticacion> autenticacion = Transformations.switchMap(validarUsuario, new Function<Usuario, LiveData<Autenticacion>>() {
        @Override
        public LiveData<Autenticacion> apply(Usuario usuario) {
            // TODO: MediatorLiveData?
            if(autenticar.getValue().estado == Autenticacion.Estado.NO_AUTENTICADO){
                return new MutableLiveData<>(new Autenticacion(Autenticacion.Estado.NO_AUTENTICADO));
            } else if(usuario != null){
                return new MutableLiveData<>(new Autenticacion(Autenticacion.Estado.AUTENTICADO, usuario));
            }
            return new MutableLiveData<>(new Autenticacion(Autenticacion.Estado.AUTENTICACION_INVALIDA));
        }
    });

    public AutenticacionViewModel(@NonNull Application application) {
        super(application);
        appDao = AppDatabase.getInstance(application).dao();
    }

    public void crearCuentaEIniciarSesion(final String nombre, final String contrasenya, final String biografia) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Usuario usuario = appDao.comprobarNombreDisponible(nombre);
                if(usuario == null){
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            appDao.insertarUsuario(new Usuario(nombre, contrasenya, biografia));
                            estadoDelRegistro.postValue(EstadoDelRegistro.REGISTRO_COMPLETADO);
                            iniciarSesion(nombre, contrasenya);
                        }
                    });
                } else {
                    estadoDelRegistro.postValue(EstadoDelRegistro.NOMBRE_NO_DISPONIBLE);
                }
            }
        });
    }

    public void iniciarSesion(final String nombre, final String contrasenya) {
        autenticar.postValue(new Autenticacion(new Usuario(nombre, contrasenya)));
    }

    public void cerrarSesion() {
        autenticar.setValue(new Autenticacion(Autenticacion.Estado.NO_AUTENTICADO));
    }
}
