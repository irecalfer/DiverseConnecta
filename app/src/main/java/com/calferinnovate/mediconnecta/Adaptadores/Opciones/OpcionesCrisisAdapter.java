package com.calferinnovate.mediconnecta.Adaptadores.Opciones;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.calferinnovate.mediconnecta.Model.Crisis;
import com.calferinnovate.mediconnecta.R;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OpcionesCrisisAdapter {

    private TextInputEditText etFecha, etHora, etIntensidad, etPatrones, etDuracion, etRecuperacion, etDescipcion;
    private AutoCompleteTextView atvLugar, atvTipo;
    private final Crisis crisis;
    private final Context context;

    public OpcionesCrisisAdapter(Crisis crisis, Context context){
        this.crisis = crisis;
        this.context = context;
    }

    public void rellenaUI(View view) {
        enlazaRecursos(view);
        seteaDatos();
    }

    public void enlazaRecursos(View view){
        etFecha = view.findViewById(R.id.ETFechaCrisisOpciones);
        etHora = view.findViewById(R.id.ETHoraCrisisOpciones);
        etIntensidad = view.findViewById(R.id.ETIntensidadCrisisOpciones);
        etPatrones = view.findViewById(R.id.ETPatronesOpciones);
        etDuracion = view.findViewById(R.id.etDuracionOpciones);
        etRecuperacion = view.findViewById(R.id.etRecuperacionOpciones);
        etDescipcion = view.findViewById(R.id.etDescripcionOpciones);
        atvLugar = view.findViewById(R.id.ATVLugaresCrisisOpciones);
        atvTipo = view.findViewById(R.id.ATVTipoCrisisOpciones);
    }

    public void seteaDatos(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenHeight = displayMetrics.heightPixels;

        // Porcentaje del alto de la pantalla para el tamaño de texto
        float textPercentage = 0.02f; // Puedes ajustar este valor según tus necesidades

        int desiredTextSize = (int) (screenHeight * textPercentage);
        etFecha.setText(obtieneFecha(crisis.getFecha()));
        etHora.setText(obtieneHora(crisis.getFecha()));
        etIntensidad.setText(crisis.getIntensidad());
        etPatrones.setText(crisis.getPatron());
        etDuracion.setText(crisis.getDuracion());
        etRecuperacion.setText(crisis.getRecuperacion());
        etDescipcion.setText(crisis.getDescripcion());
        atvLugar.setText(crisis.getLugar());
        atvTipo.setText(crisis.getTipo());
    }

    private String obtieneFecha(String fecha) {
        // Formato de entrada (año-mes-día)
        DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Formato de salida (día-mes-año abreviado)
        DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Parsea la fecha de entrada
        LocalDateTime fechaEntrada = LocalDateTime.parse(fecha, formatoEntrada);

        // Formatea la fecha en el formato de salida
        return fechaEntrada.format(formatoSalida);
    }

    private String obtieneHora(String hora) {
        // Formato de entrada (año-mes-día)
        DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Formato de salida (día-mes-año abreviado)
        DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("HH:mm");

        // Parsea la fecha de entrada
        LocalDateTime fechaEntrada = LocalDateTime.parse(hora, formatoEntrada);

        // Formatea la fecha en el formato de salida
        return fechaEntrada.format(formatoSalida);
    }
}
