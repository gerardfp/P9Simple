package com.company.p9simple.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.company.p9simple.NavigationBackStackLogger;
import com.company.p9simple.R;
import com.company.p9simple.viewmodel.AutenticacionViewModel;

import static com.company.p9simple.viewmodel.AutenticacionViewModel.*;


public class InicioFragment extends Fragment {

    private AutenticacionViewModel autenticacionViewModel;

    private Button cerrarSesionButton, irAlPerfilButton;

    public InicioFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        autenticacionViewModel = ViewModelProviders.of(requireActivity()).get(AutenticacionViewModel.class);

        cerrarSesionButton = view.findViewById(R.id.button_cerrarSesion);
        irAlPerfilButton = view.findViewById(R.id.button_irAlPerfil);


        irAlPerfilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.perfilFragment);
            }
        });

        cerrarSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autenticacionViewModel.cerrarSesion();
            }
        });

        autenticacionViewModel.estadoDeLaAutenticacion.observe(getViewLifecycleOwner(), new Observer<EstadoDeLaAutenticacion>() {
            @Override
            public void onChanged(EstadoDeLaAutenticacion estadoDeLaAutenticacion) {
                switch (estadoDeLaAutenticacion){
                    case AUTENTICADO:
                        cerrarSesionButton.setVisibility(View.VISIBLE);
                        break;

                    default:
                        cerrarSesionButton.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }
}
