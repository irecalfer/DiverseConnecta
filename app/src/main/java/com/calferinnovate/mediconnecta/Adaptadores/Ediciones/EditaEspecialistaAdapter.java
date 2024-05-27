package com.calferinnovate.mediconnecta.Adaptadores.Ediciones;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.calferinnovate.mediconnecta.Model.Especialista;
import com.calferinnovate.mediconnecta.R;
import com.google.android.material.textfield.TextInputEditText;

public class EditaEspecialistaAdapter {

    private TextInputEditText tietEspecialista, tietNombre, tietCentro, tietTelefono, tietEmail;
    private final Especialista especialista;
    private final Context context;

    public EditaEspecialistaAdapter(Especialista especialista, Context context){
        this.especialista = especialista;
        this.context = context;
    }

    public void rellenaUI(View view) {
        enlazaRecursos(view);
        seteaDatos();
    }

    public void enlazaRecursos(View view){
        tietEspecialista = view.findViewById(R.id.etEspecialidad);
        tietNombre = view.findViewById(R.id.etNombre);
        tietCentro = view.findViewById(R.id.etCentro);
        tietTelefono = view.findViewById(R.id.etTelefono);
        tietEmail = view.findViewById(R.id.etEmail);
    }

    public void seteaDatos(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenHeight = displayMetrics.heightPixels;

        // Porcentaje del alto de la pantalla para el tamaño de texto
        float textPercentage = 0.02f; // Puedes ajustar este valor según tus necesidades

        int desiredTextSize = (int) (screenHeight * textPercentage);
        tietEspecialista.setText(especialista.getEspecialidad());
        tietEspecialista.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        tietNombre.setText(especialista.getNombre());
        tietNombre.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        tietCentro.setText(especialista.getCentro());
        tietCentro.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        tietTelefono.setText(especialista.getTelefono());
        tietTelefono.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        tietEmail.setText(especialista.getEmail());
        tietEmail.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);
    }
}
