package com.calferinnovate.mediconnecta.Adaptadores.Registros;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Crisis;
import com.calferinnovate.mediconnecta.R;

import java.util.ArrayList;

public class RegistraCrisisAdapter {

    private ArrayList<String> tipoArrayList, lugaresArrayList;
    private ArrayList<Crisis> crisisArrayList;
    private Alumnos alumno;
    private ClaseGlobal claseGlobal;
    private Context context;
    private AutoCompleteTextView tiposTV, lugaresTV;
    private String tipo, lugar;
    private ItemItemSelectedListener listener;

    public interface ItemItemSelectedListener {
        void onSpinnerItemSelected(String tipo, String lugar);
    }

    // Método para establecer el escuchador de eventos
    public void setSpinnerItemSelectedListener(ItemItemSelectedListener listener) {
        this.listener = listener;
    }

    public RegistraCrisisAdapter(ClaseGlobal claseGlobal, Context context, ArrayList<Crisis> crisisArrayList){
        this.claseGlobal = claseGlobal;
        this.context = context;
        this.crisisArrayList = crisisArrayList;
    }
    private void notifyItemSelectedListener() {
        if (listener != null) {
            listener.onSpinnerItemSelected(tipo, lugar);
        }
    }

    public void rellenaUI(View view) {
        enlazaRecursos(view);
        setupSpinners();
    }

    public void enlazaRecursos(View view){
        tiposTV = view.findViewById(R.id.ATVTipoCrisisOpciones);
        lugaresTV = view.findViewById(R.id.ATVLugaresCrisisOpciones);
    }

    public void setupSpinners() {
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
        tiposTV.setThreshold(1);
        tiposTV.setAdapter(tiposAdapter);
        //Si no se selecciona curso y se deja por defecto


        //Si se selecciona un curso del spinner
        tiposTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tipo = (String) parent.getItemAtPosition(position);
                notifyItemSelectedListener();
            }
        });

        tiposTV.addTextChangedListener(new TextWatcher() {
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
        tipo = tiposTV.getText().toString();
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
        lugaresTV.setThreshold(1);
        lugaresTV.setAdapter(lugaresAdapter);
        //Si no se selecciona curso y se deja por defecto


        //Si se selecciona un curso del spinner
        lugaresTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lugar = (String) parent.getItemAtPosition(position);
                notifyItemSelectedListener();
            }
        });

        lugaresTV.addTextChangedListener(new TextWatcher() {
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
        lugar = lugaresTV.getText().toString();
        notifyItemSelectedListener();

    }

}
