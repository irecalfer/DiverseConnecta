package com.calferinnovate.mediconnecta.Adaptadores.Ediciones;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.calferinnovate.mediconnecta.Model.Crisis;
import com.calferinnovate.mediconnecta.R;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EditaCrisisAdapter {

    private TextInputEditText etFecha, etHora, etIntensidad, etPatrones, etDuracion, etRecuperacion, etDescipcion;
    private AutoCompleteTextView atvLugar, atvTipo;
    private final Crisis crisis;
    private final Context context;
    private ArrayList<Crisis> crisisArrayList;
    private ItemItemSelectedListener listener;
    private String tipo, lugar;
    private ArrayList<String> tipoArrayList, lugaresArrayList;

    public interface ItemItemSelectedListener {
        void onSpinnerItemSelected(String tipo, String lugar);
    }

    // Método para establecer el escuchador de eventos
    public void setSpinnerItemSelectedListener(ItemItemSelectedListener listener) {
        this.listener = listener;
    }

    private void notifyItemSelectedListener() {
        if (listener != null) {
            listener.onSpinnerItemSelected(tipo, lugar);
        }
    }

    public EditaCrisisAdapter(Crisis crisis, ArrayList<Crisis> crisisArrayList,  Context context){
        this.crisis = crisis;
        this.crisisArrayList = crisisArrayList;
        this.context = context;
    }

    public void rellenaUI(View view) {
        enlazaRecursos(view);
        seteaDatos();
        setupSpinners();
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
        etFecha.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);
        etHora.setText(obtieneHora(crisis.getFecha()));
        etHora.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);
        etIntensidad.setText(crisis.getIntensidad());
        etIntensidad.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);
        etPatrones.setText(crisis.getPatron());
        etPatrones.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);
        etDuracion.setText(crisis.getDuracion());
        etDuracion.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);
        etRecuperacion.setText(crisis.getRecuperacion());
        etRecuperacion.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);
        etDescipcion.setText(crisis.getDescripcion());
        etDescipcion.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);
        atvLugar.setText(crisis.getLugar());
        atvLugar.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);
        atvTipo.setText(crisis.getTipo());
        atvTipo.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);
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

    public void setupSpinners(){
        setupTiposSpinner();
        setupLugaresSpinner();
    }

    private void setupTiposSpinner() {
        tipoArrayList = new ArrayList<>();
        for(Crisis crisis: crisisArrayList){
            if(!tipoArrayList.contains(crisis.getTipo())){
                tipoArrayList.add(crisis.getTipo());
            }
        }
        ArrayAdapter<String> tiposAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, tipoArrayList);
        atvTipo.setThreshold(1);
        atvTipo.setAdapter(tiposAdapter);
        //Si no se selecciona curso y se deja por defecto


        //Si se selecciona un curso del spinner
        atvTipo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tipo = (String) parent.getItemAtPosition(position);
                notifyItemSelectedListener();
            }
        });

        atvTipo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No es necesario implementar este método
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No es necesario implementar este método
            }

            @Override
            public void afterTextChanged(Editable s) {
                String tipoText = s.toString();
                if (!tipoArrayList.contains(tipoText)) {
                    // El texto ingresado no coincide con ningún elemento de la lista
                    // Considerarlo como un nuevo elemento y guardarlo en la base de datos
                    tipo = tipoText;
                    notifyItemSelectedListener();
                }
            }
        });

        //SI no se ha seleccionado nada
        tipo = atvTipo.getText().toString();
        notifyItemSelectedListener();

    }

    private void setupLugaresSpinner() {
        lugaresArrayList = new ArrayList<>();
        for(Crisis crisis: crisisArrayList){
            if(!lugaresArrayList.contains(crisis.getLugar())){
                lugaresArrayList.add(crisis.getLugar());
            }
        }
        ArrayAdapter<String> lugaresAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, lugaresArrayList);
        atvLugar.setThreshold(1);
        atvLugar.setAdapter(lugaresAdapter);
        //Si no se selecciona curso y se deja por defecto


        //Si se selecciona un curso del spinner
        atvLugar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lugar = (String) parent.getItemAtPosition(position);
                notifyItemSelectedListener();
            }
        });

        atvLugar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No es necesario implementar este método
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No es necesario implementar este método
            }

            @Override
            public void afterTextChanged(Editable s) {
                String lugaresText = s.toString();
                if (!lugaresArrayList.contains(lugaresText)) {
                    // El texto ingresado no coincide con ningún elemento de la lista
                    // Considerarlo como un nuevo elemento y guardarlo en la base de datos
                    lugar = lugaresText;
                    notifyItemSelectedListener();
                }
            }
        });

        //SI no se ha seleccionado nada
        lugar = atvLugar.getText().toString();
        notifyItemSelectedListener();

    }
}
