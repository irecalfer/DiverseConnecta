package com.calferinnovate.mediconnecta.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.calferinnovate.mediconnecta.Model.Seguimiento;
import com.calferinnovate.mediconnecta.R;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditaSeguimientoAdapter {

    private TextInputEditText etFecha, etHora, etSeguimiento;
    private final Seguimiento seguimiento;
    private final Context context;

    public EditaSeguimientoAdapter(Seguimiento seguimiento, Context context) {
        this.seguimiento = seguimiento;
        this.context = context;
    }

    public void rellenaUI(View view) {
        enlazaRecursos(view);
        seteaDatos();
    }
    public void enlazaRecursos(View view) {
        etFecha = view.findViewById(R.id.ETFechaSeguimiento);
        etHora = view.findViewById(R.id.ETHoraSeguimiento);
        etSeguimiento = view.findViewById(R.id.ETSeguimientoAlumno);
    }

    public void seteaDatos() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenHeight = displayMetrics.heightPixels;

        // Porcentaje del alto de la pantalla para el tamaño de texto
        float textPercentage = 0.02f; // Puedes ajustar este valor según tus necesidades

        int desiredTextSize = (int) (screenHeight * textPercentage);
        etFecha.setText(obtieneFecha(seguimiento.getFechaHora()));
        etFecha.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);
        etHora.setText(obtieneHora(seguimiento.getFechaHora()));
        etSeguimiento.setText(seguimiento.getDescripcion());

    }

    private String obtieneFecha(String fecha) {
        // Formato de entrada (año-mes-día)
        DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

        // Formato de salida (día-mes-año abreviado)
        DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Parsea la fecha de entrada
        LocalDate fechaEntrada = LocalDate.parse(fecha, formatoEntrada);

        // Formatea la fecha en el formato de salida
        return fechaEntrada.format(formatoSalida);
    }

    private String obtieneHora(String hora) {
        // Formato de entrada (año-mes-día)
        DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

        // Formato de salida (día-mes-año abreviado)
        DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("hh:mm");

        // Parsea la fecha de entrada
        LocalDate fechaEntrada = LocalDate.parse(hora, formatoEntrada);

        // Formatea la fecha en el formato de salida
        return fechaEntrada.format(formatoSalida);
    }
}
