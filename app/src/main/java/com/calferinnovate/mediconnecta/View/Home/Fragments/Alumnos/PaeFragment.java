package com.calferinnovate.mediconnecta.View.Home.Fragments.Alumnos;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
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
import com.calferinnovate.mediconnecta.View.Home.Fragments.Ediciones.EditarPaeFragment;
import com.calferinnovate.mediconnecta.View.Home.Fragments.AlumnosFragment;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.ViewModel.SharedAlumnosViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


public class PaeFragment extends Fragment implements IOnBackPressed {
    private ClaseGlobal claseGlobal;
    private TableLayout tablaPae;
    private PeticionesJson peticionesJson;
    private SharedAlumnosViewModel sharedAlumnosViewModel;
    private Alumnos alumno;
    private  ArrayList<String> propiedades = new ArrayList<>(Arrays.asList("Peso", "Talla", "IMC", "Percentil", "Tª", "T.A", "F.C", "Sat. O2"));
    private String[] header = {"1er Trimestre", "2º Trimestre", "3er Trimestre"};
    private MenuHost menuHost;
    private TableRow.LayoutParams lp;
    private Pae datosPae;
    private Menu menuPae;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pae, container, false);
        getActivity().setTitle("PAE");
        menuHost = requireActivity();
        cambiarToolbar();
        // Observa el estado de carga y actualiza la interfaz de usuario según sea necesario
        inicializaVariables(view);
        inicializaViewModel();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        obtieneDatosAlumno(view);
    }



    public void inicializaVariables(View view){
        claseGlobal = ClaseGlobal.getInstance();
        tablaPae = view.findViewById(R.id.tablaControlPae);
        alumno = new Alumnos();
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




    public void obtieneDatosAlumno(View view){
        sharedAlumnosViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Alumnos>() {
            @Override
            public void onChanged(Alumnos alumnos) {
                alumno = alumnos;
                //sharedAlumnosViewModel.limpiarDatos();
                obtienePae(alumno, view);
            }
        });
    }

    public void obtienePae(Alumnos alumno, View view){
        Log.d("PAE_FRAGMENT", "Obteniendo lista de PAE...");
        sharedAlumnosViewModel.obtienePae(alumno).observe(getViewLifecycleOwner(), new Observer<ArrayList<Pae>>() {
            @Override
            public void onChanged(ArrayList<Pae> paes) {
                if (!paes.isEmpty()) {
                    PaeAdapter paeAdapter = new PaeAdapter(alumno, paes.get(0), requireContext(), menuPae);
                    paeAdapter.rellenaUI(view);
                    creaTablaSeguimiento(view, paes);
                }
            }
        });
    }

    public void creaEncabezadoTabla() {
        TableRow fila = new TableRow(getActivity());
        lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        fila.setLayoutParams(lp);

        // Establecer el fondo de color del encabezado
        fila.setBackgroundColor(Color.parseColor("#006B58")); // Cambia el color según tus preferencias
        // Borde
        // fila.setBackgroundResource(R.drawable.borde_tabla);
        // El elemento de la izquierda
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
    }

    private ArrayList<ControlSomatometrico> ordenarPorTrimestre(ArrayList<ControlSomatometrico> controlSomatometricos) {
        Collections.sort(controlSomatometricos, new Comparator<ControlSomatometrico>() {
            @Override
            public int compare(ControlSomatometrico control1, ControlSomatometrico control2) {
                // Comparar los valores de fktrimestre y devolver el resultado de la comparación
                return Integer.compare(control1.getFkTrimestre(), control2.getFkTrimestre());
            }
        });
        return controlSomatometricos;
    }

    public void creaTablaSeguimiento(View view, ArrayList<Pae> paeArrayList){
        // Limpia la tabla antes de volver a crear las filas y el encabezado
        tablaPae.removeAllViews();

        // Crea el encabezado de la tabla
        creaEncabezadoTabla();
        // Rellenar la tabla con los datos de cada propiedad para cada trimestre
        sharedAlumnosViewModel.obtieneControlSomatometrico(paeArrayList.get(0)).observe(getViewLifecycleOwner(), new Observer<ArrayList<ControlSomatometrico>>() {
            @Override
            public void onChanged(ArrayList<ControlSomatometrico> controlSomatometricos) {
                ArrayList<ControlSomatometrico> controlTemp;
                // Ordenar los datos de control somatométrico por trimestre
                controlTemp = ordenarPorTrimestre(controlSomatometricos);
                // Crear una fila para cada propiedad
                for (String propiedad : propiedades) {
                    TableRow filaPropiedad = new TableRow(getActivity());
                    filaPropiedad.setLayoutParams(lp);

                    // Agregar el nombre de la propiedad como encabezado
                    TextView textViewPropiedad = new TextView(getActivity());
                    textViewPropiedad.setText(propiedad);
                    textViewPropiedad.setTypeface(null, Typeface.BOLD);
                    textViewPropiedad.setTextSize(24); // Tamaño de letra grande
                    textViewPropiedad.setTextColor(Color.WHITE); // Texto en blanco
                    textViewPropiedad.setBackgroundColor(Color.parseColor("#006B58")); // Fondo verde oscuro
                    filaPropiedad.addView(textViewPropiedad);

                    // Agregar los datos correspondientes para cada trimestre
                    for (ControlSomatometrico control : controlTemp) {
                        EditText editText = new EditText(getActivity());
                        String dato = obtenerDatoPropiedad(control, propiedad);
                        editText.setText(dato);

                        // Establecer parámetros de diseño para EditText
                        TableRow.LayoutParams editTextParams = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f); // Peso 1 para que se expanda
                        editText.setLayoutParams(editTextParams);
                        editText.setTextSize(20);
                        filaPropiedad.addView(editText);
                    }

                    // Agregar la fila a la tabla
                    tablaPae.addView(filaPropiedad);
                }

                propiedades.clear();
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
    }

    public void cambiarToolbar(){
        MenuProvider menuProvider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.app_menu_opciones_pae, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.action_editar_pae){
                    getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new EditarPaeFragment()).addToBackStack(null).setReorderingAllowed(true).commit();
                }
                return false;
            }
        };

        requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }



    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AlumnosFragment()).commit();
        return true;
    }



}