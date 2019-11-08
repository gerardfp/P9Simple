package com.company.p9simple.view;


import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.p9simple.R;
import com.company.p9simple.model.Usuario;
import com.company.p9simple.viewmodel.AutenticacionViewModel;


public class PerfilFragment extends Fragment {

    TextView nombreCampoTexto, biografiaCampoTexto;

    private AutenticacionViewModel autenticacionViewModel;

    public PerfilFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        autenticacionViewModel = ViewModelProviders.of(requireActivity()).get(AutenticacionViewModel.class);

        nombreCampoTexto = view.findViewById(R.id.nombre);
        biografiaCampoTexto = view.findViewById(R.id.biografia);

        autenticacionViewModel.estadoDeLaAutenticacion.observe(getViewLifecycleOwner(), new Observer<AutenticacionViewModel.EstadoDeLaAutenticacion>() {
            @Override
            public void onChanged(AutenticacionViewModel.EstadoDeLaAutenticacion estadoDeLaAutenticacion) {
                switch (estadoDeLaAutenticacion) {
                    case AUTENTICADO:
                        mostrarPerfil(autenticacionViewModel.usuarioLogeado);
                        break;
                    case NO_AUTENTICADO:
                        Navigation.findNavController(view).navigate(R.id.entrarFragment);
                        break;
                }
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        Navigation.findNavController(view).popBackStack(R.id.inicioFragment, false);
                    }
                });

    }

    private void mostrarPerfil(Usuario usuario) {
        nombreCampoTexto.setText(usuario.nombre);
        biografiaCampoTexto.setText(usuario.biografia);
    }
}