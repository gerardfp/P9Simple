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
import android.widget.TextView;

import com.company.p9simple.NavigationBackStackLogger;
import com.company.p9simple.R;
import com.company.p9simple.model.Usuario;
import com.company.p9simple.viewmodel.AutenticacionViewModel;

import static com.company.p9simple.viewmodel.AutenticacionViewModel.*;

public class PerfilFragment extends Fragment {

    private AutenticacionViewModel autenticacionViewModel;

    private TextView nombreEditText, biografiaEditText;

    public PerfilFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        autenticacionViewModel = ViewModelProviders.of(requireActivity()).get(AutenticacionViewModel.class);

        nombreEditText = view.findViewById(R.id.edittext_nombre);
        biografiaEditText = view.findViewById(R.id.edittext_biografia);


        autenticacionViewModel.estadoDeLaAutenticacion.observe(getViewLifecycleOwner(), new Observer<EstadoDeLaAutenticacion>() {
            @Override
            public void onChanged(EstadoDeLaAutenticacion estadoDeLaAutenticacion) {
                switch (estadoDeLaAutenticacion) {
                    case AUTENTICADO:
                        mostrarPerfil(autenticacionViewModel.usuarioLogeado);
                        break;

                    case NO_AUTENTICADO:
                        Navigation.findNavController(view).navigate(R.id.iniciarSesionFragment);
                        break;
                }
            }
        });
    }

    private void mostrarPerfil(Usuario usuario) {
        nombreEditText.setText(usuario.nombre);
        biografiaEditText.setText(usuario.biografia);
    }
}