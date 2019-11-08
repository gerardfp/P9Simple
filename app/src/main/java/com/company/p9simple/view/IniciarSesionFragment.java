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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.company.p9simple.NavigationBackStackLogger;
import com.company.p9simple.R;
import com.company.p9simple.viewmodel.AutenticacionViewModel;

import static com.company.p9simple.viewmodel.AutenticacionViewModel.*;


public class IniciarSesionFragment extends Fragment {

    private AutenticacionViewModel autenticacionViewModel;

    private EditText usuarioEditText, contrasenyaEditText;
    private Button iniciarSesionButton;
    private TextView irAlRegistroTextView;

    public IniciarSesionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_iniciar_sesion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavigationBackStackLogger.log(view);

        autenticacionViewModel = ViewModelProviders.of(requireActivity()).get(AutenticacionViewModel.class);

        usuarioEditText = view.findViewById(R.id.edittext_nombre);
        contrasenyaEditText = view.findViewById(R.id.edittext_contrasenya);
        iniciarSesionButton = view.findViewById(R.id.button_iniciarSesion);
        irAlRegistroTextView = view.findViewById(R.id.textview_irAlRegistro);


        irAlRegistroTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.registroFragment);
            }
        });

        iniciarSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                autenticacionViewModel.iniciarSesion(usuarioEditText.getText().toString(), contrasenyaEditText.getText().toString());
            }
        });

        autenticacionViewModel.estadoDeLaAutenticacion.observe(getViewLifecycleOwner(), new Observer<EstadoDeLaAutenticacion>() {
            @Override
            public void onChanged(EstadoDeLaAutenticacion estadoDeLaAutenticacion) {
                switch (estadoDeLaAutenticacion){
                    case AUTENTICADO:
                        Navigation.findNavController(view).popBackStack();
                        break;

                    case AUTENTICACION_INVALIDA:
                        Toast.makeText(getContext(), "CREDENCIALES NO VALIDAS", Toast.LENGTH_SHORT).show();
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
}
