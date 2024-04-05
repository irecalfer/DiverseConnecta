package com.calferinnovate.mediconnecta.View.Home.Fragments.Addiciones;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.calferinnovate.mediconnecta.Adaptadores.PaeAdapter;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.ControlSomatometrico;
import com.calferinnovate.mediconnecta.Model.Pae;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.ViewModel.SharedAlumnosViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;

import java.util.ArrayList;


public class EditarPaeFragment extends Fragment implements IOnBackPressed {

    private ClaseGlobal claseGlobal;
    private TableLayout tablaPae;
    private PeticionesJson peticionesJson;
    private SharedAlumnosViewModel sharedAlumnosViewModel;
    private Alumnos alumno;
    private  String[] propiedades = {"Peso", "Talla", "IMC", "Percentil", "Tª", "T.A", "F.C", "Sat. O2"};
    private String[] header = {"1er Trimestre", "2º Trimestre", "3er Trimestre"};
    private MenuHost menuHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editar_pae, container, false);
        getActivity().setTitle("PAE");
        menuHost = requireActivity();
        cambiarToolbar();
        inicializaVariables(view);
        inicializaViewModel();
        creaTablaSeguimiento(view);
        return view;
    }

    public void inicializaVariables(View view){
        claseGlobal = ClaseGlobal.getInstance();
        tablaPae = view.findViewById(R.id.tablaControlPae);
    }

    public void inicializaViewModel() {
        ViewModelArgs viewModelArgs = new ViewModelArgs() {
            @Override
            public PeticionesJson getPeticionesJson() {
                return peticionesJson = new PeticionesJson(requireContext());
            }

            @Override
            public ClaseGlobal getClaseGlobal() {
                return claseGlobal;
            }
        };

        ViewModelFactory<SharedAlumnosViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        sharedAlumnosViewModel = new ViewModelProvider(requireActivity(), factory).get(SharedAlumnosViewModel.class);
    }

    public void creaTablaSeguimiento(View view){
        // Primero dibujar el encabezado; esto es poner "TALLAS" y a la derecha todas las tallas
        TableRow fila = new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        fila.setLayoutParams(lp);

        // Establecer el fondo de color del encabezado
        fila.setBackgroundColor(Color.parseColor("#006B58")); // Cambia el color según tus preferencias
// Borde
        //fila.setBackgroundResource(R.drawable.borde_tabla);
//El elemento de la izquierda
        TextView tv = new TextView(getActivity());
        tv.setTypeface(null, Typeface.BOLD);
        tv.setText("Control");
        tv.setTextSize(24);
        tv.setTextColor(Color.WHITE); // Texto en blanco

        fila.addView(tv);

// Ahora agregar las tallas
        for (int x = 0; x < header.length; x++) {
            TextView tvTalla = new TextView(getActivity());
            tvTalla.setText(header[x]);
            tvTalla.setTextSize(24);

            tvTalla.setTextColor(Color.WHITE); // Texto en blanco

            fila.addView(tvTalla);
        }
// Finalmente agregar la fila en la primera posición
        tablaPae.addView(fila, 0);
        // Rellenar la tabla con los datos de cada propiedad para cada trimestre
        obtieneDatosAlumno(view, lp);

    }



    public void obtieneDatosAlumno(View view, TableRow.LayoutParams lp){
        sharedAlumnosViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Alumnos>() {
            @Override
            public void onChanged(Alumnos alumnos) {
                obtienePae(alumnos, view, lp);

            }
        });
    }

    public void obtienePae(Alumnos alumno, View view, TableRow.LayoutParams lp){
        sharedAlumnosViewModel.obtienePae(alumno).observe(getViewLifecycleOwner(), new Observer<ArrayList<Pae>>() {
            @Override
            public void onChanged(ArrayList<Pae> paes) {
                PaeAdapter paeAdapter = new PaeAdapter(alumno, paes.get(0), requireContext());
                paeAdapter.rellenaUI(view);
                rellenaFilasControl(paes.get(0), lp);
            }
        });
    }

    public void rellenaFilasControl(Pae pae, TableRow.LayoutParams lp){
        sharedAlumnosViewModel.obtieneControlSomatometrico(pae).observe(getViewLifecycleOwner(), new Observer<ArrayList<ControlSomatometrico>>() {
            @Override
            public void onChanged(ArrayList<ControlSomatometrico> controlSomatometricos) {

                // Crear una fila para cada propiedad
                for (String propiedad : propiedades) {
                    TableRow filaPropiedad = new TableRow(getActivity());
                    filaPropiedad.setLayoutParams(lp);
                    // Establecer el fondo de color de la fila


                    // Agregar el nombre de la propiedad como encabezado
                    TextView textViewPropiedad = new TextView(getActivity());
                    textViewPropiedad.setText(propiedad);
                    textViewPropiedad.setTypeface(null, Typeface.BOLD);
                    textViewPropiedad.setTextSize(20); // Tamaño de letra grande
                    textViewPropiedad.setTextColor(Color.WHITE); // Texto en blanco
                    textViewPropiedad.setBackgroundColor(Color.parseColor("#006B58")); // Fondo verde oscuro

                    filaPropiedad.addView(textViewPropiedad);

                    // Agregar los datos correspondientes para cada trimestre
                    for (ControlSomatometrico control : controlSomatometricos) {
                        EditText editText = new EditText(getActivity());
                        String dato = obtenerDatoPropiedad(control, propiedad);
                        editText.setText(dato);


                        filaPropiedad.addView(editText);
                    }

                    // Agregar la fila a la tabla
                    tablaPae.addView(filaPropiedad);
                }

            }

        });
    }



    private String obtenerDatoPropiedad(ControlSomatometrico control, String propiedad) {
        switch (propiedad) {
            case "Peso":
                return String.valueOf(control.getPeso());
            case "Talla":
                return control.getTalla();
            case "IMC":
                return String.valueOf(control.getImc());
            case "Percentil":
                return String.valueOf(control.getPercentil());
            case "Tª":
                return String.valueOf(control.getTemperatura());
            case "T.A":
                return control.getTensionArterial();
            case "F.C":
                return String.valueOf(control.getFrecuenciaCardiaca());
            case "Sat. O2":
                return String.valueOf(control.getSaturacionO2());
            default:
                return "";
        }
    }public void cambiarToolbar(){
        MenuProvider menuProvider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.app_menu_opciones_pae_editar, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.action_confirmar_pae){
                    return true;
                }

                return false;
            }
        };

        requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }



    @Override
    public boolean onBackPressed() {
        return false;
    }
}