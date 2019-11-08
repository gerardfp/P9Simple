package com.company.p9simple.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.company.p9simple.R;
import com.company.p9simple.viewmodel.AutenticacionViewModel;


public class EntrarFragment extends Fragment {

    private EditText usuarioCampoTexto, contrasenyaCampoTexto;
    private AutenticacionViewModel autenticacionViewModel;

    public EntrarFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_entrar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        autenticacionViewModel = ViewModelProviders.of(requireActivity()).get(AutenticacionViewModel.class);

        usuarioCampoTexto = view.findViewById(R.id.nombre);
        contrasenyaCampoTexto = view.findViewById(R.id.contrasenya);

        view.findViewById(R.id.goto_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.registroFragment);
            }
        });

        view.findViewById(R.id.botonEntrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                autenticacionViewModel.entrar(usuarioCampoTexto.getText().toString(), contrasenyaCampoTexto.getText().toString())
                        .observe(getViewLifecycleOwner(), new Observer<AutenticacionViewModel.EstadoDeLaAutenticacion>() {
                    @Override
                    public void onChanged(AutenticacionViewModel.EstadoDeLaAutenticacion estadoDeLaAutenticacion) {
                                if(estadoDeLaAutenticacion == AutenticacionViewModel.EstadoDeLaAutenticacion.AUTENTICADO){
                                    Navigation.findNavController(view).navigate(R.id.perfilFragment);
                                } else if(estadoDeLaAutenticacion == AutenticacionViewModel.EstadoDeLaAutenticacion.AUTENTICACION_INVALIDA){
                                    Toast.makeText(getContext(), "CREDENCIALES NO VALIDAS", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
