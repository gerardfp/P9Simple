package com.company.p9simple.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.company.p9simple.NavigationBackStackLogger;
import com.company.p9simple.R;
import com.company.p9simple.viewmodel.AutenticacionViewModel;

import static com.company.p9simple.viewmodel.AutenticacionViewModel.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;


public class RegistroFragment extends Fragment {

    private AutenticacionViewModel autenticacionViewModel;

    private EditText nombreEditText, contrasenyaEditText, biografiaEditText;
    private Button registrarButton;

    public RegistroFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavigationBackStackLogger.log(view);

        autenticacionViewModel = ViewModelProviders.of(requireActivity()).get(AutenticacionViewModel.class);

        nombreEditText = view.findViewById(R.id.edittext_nombre);
        contrasenyaEditText = view.findViewById(R.id.edittext_contrasenya);
        biografiaEditText = view.findViewById(R.id.edittext_biografia);
        registrarButton = view.findViewById(R.id.button_registrar);


        registrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autenticacionViewModel.crearCuentaEIniciarSesion(nombreEditText.getText().toString(), contrasenyaEditText.getText().toString(), biografiaEditText.getText().toString());
            }
        });

        autenticacionViewModel.estadoDelRegistro.observe(getViewLifecycleOwner(), new Observer<EstadoDelRegistro>() {
            @Override
            public void onChanged(EstadoDelRegistro estadoDelRegistro) {
                switch (estadoDelRegistro){
                    case REGISTRO_COMPLETADO:
                        autenticacionViewModel.estadoDelRegistro.setValue(EstadoDelRegistro.INICIO_DEL_REGISTRO);
                        break;

                    case NOMBRE_NO_DISPONIBLE:
                        Toast.makeText(getContext(), "NOMBRE DE USUARIO NO DISPONIBLE", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        autenticacionViewModel.autenticacion.observe(getViewLifecycleOwner(), new Observer<Autenticacion>() {
            @Override
            public void onChanged(Autenticacion autenticacion) {
                switch (autenticacion.estado){
                    case AUTENTICADO:
                        Navigation.findNavController(view).popBackStack();
                        break;
                }
            }
        });
    }
}
