package com.company.p9simple.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.company.p9simple.R;
import com.company.p9simple.viewmodel.AutenticacionViewModel;
import com.company.p9simple.viewmodel.AutenticacionViewModel.EstadoDeLaAutenticacion;
import com.company.p9simple.viewmodel.AutenticacionViewModel.EstadoDelRegistro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;


public class RegistroFragment extends Fragment {

    AutenticacionViewModel autenticacionViewModel;

    EditText nombreCampoTexto, contrasenyaCampoTexto, biografiaCampoTexto;


    public RegistroFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        autenticacionViewModel = ViewModelProviders.of(requireActivity()).get(AutenticacionViewModel.class);

        nombreCampoTexto = view.findViewById(R.id.nombre);
        contrasenyaCampoTexto = view.findViewById(R.id.contrasenya);
        biografiaCampoTexto = view.findViewById(R.id.biografia);

        view.findViewById(R.id.botonRegistrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autenticacionViewModel.crearCuentaYLogear(nombreCampoTexto.getText().toString(), contrasenyaCampoTexto.getText().toString(), biografiaCampoTexto.getText().toString());
            }
        });

        autenticacionViewModel.estadoDelRegistro.observe(getViewLifecycleOwner(), new Observer<EstadoDelRegistro>() {
            @Override
            public void onChanged(EstadoDelRegistro estadoDelRegistro) {
                switch (estadoDelRegistro){
                    case REGISTRO_COMPLETADO:

                        autenticacionViewModel.estadoDelRegistro.setValue(EstadoDelRegistro.INICIO_DEL_REGISTRO);

                        autenticacionViewModel.entrar(autenticacionViewModel.usuarioRegistrado.nombre, autenticacionViewModel.usuarioRegistrado.contrasenya)
                                .observe(getViewLifecycleOwner(), new Observer<EstadoDeLaAutenticacion>() {
                                    @Override
                                    public void onChanged(EstadoDeLaAutenticacion estadoDeLaAutenticacion) {
                                        switch (estadoDeLaAutenticacion){
                                            case AUTENTICADO:
                                                Navigation.findNavController(view).navigate(R.id.perfilFragment);
                                                break;
                                        }
                                    }
                                });
                        break;

                    case NOMBRE_NO_DISPONIBLE:
                        Toast.makeText(getContext(), "NOMBRE DE USUARIO NO DISPONIBLE", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
