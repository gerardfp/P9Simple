package com.company.p9simple.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import com.company.p9simple.db.AppDao;
import com.company.p9simple.db.AppDatabase;
import com.company.p9simple.model.Usuario;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

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
        final LiveData<Usuario> userNameCheck = appDao.comprobarNombreDiponible(nombre);
        userNameCheck.observeForever(new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                if(usuario == null){
                    final Usuario newUsuario = new Usuario(nombre, contrasenya, biografia);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            appDao.insertarUsuario(newUsuario);
                            usuarioRegistrado = newUsuario;
                            estadoDelRegistro.postValue(EstadoDelRegistro.REGISTRO_COMPLETADO);
                        }
                    });
                } else {
                    estadoDelRegistro.setValue(EstadoDelRegistro.NOMBRE_NO_DISPONIBLE);
                }
                userNameCheck.removeObserver(this);
            }
        });
    }

    public MutableLiveData<EstadoDeLaAutenticacion> entrar(String nombre, String contrasenya) {
        final LiveData<Usuario> authenticate = appDao.autenticar(nombre, contrasenya);

        authenticate.observeForever(new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                if(usuario != null){
                    usuarioLogeado = usuario;
                    estadoDeLaAutenticacion.setValue(EstadoDeLaAutenticacion.AUTENTICADO);
                } else {
                    estadoDeLaAutenticacion.setValue(EstadoDeLaAutenticacion.AUTENTICACION_INVALIDA);
                }
                authenticate.removeObserver(this);
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