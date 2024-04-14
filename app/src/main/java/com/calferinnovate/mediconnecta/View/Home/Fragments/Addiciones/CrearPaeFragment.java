package com.calferinnovate.mediconnecta.View.Home.Fragments.Addiciones;

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

import com.calferinnovate.mediconnecta.Adaptadores.CreaPaeAdapter;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.ControlSomatometrico;
import com.calferinnovate.mediconnecta.Model.Curso;
import com.calferinnovate.mediconnecta.Model.Pae;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Alumnos.GeneralAlumnosFragment;
import com.calferinnovate.mediconnecta.View.Home.Fragments.AlumnosFragment;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.ViewModel.SharedAlumnosViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;

import java.util.ArrayList;


public class CrearPaeFragment extends Fragment implements IOnBackPressed {


    private  String[] propiedades = {"Peso", "Talla", "IMC", "Percentil", "Tª", "T.A", "F.C", "Sat. O2"};
    private String[] header = {"1er Trimestre", "2º Trimestre", "3er Trimestre"};
    private MenuHost menuHost;
    private ClaseGlobal claseGlobal;
    private TableLayout tablaPae;
    private SharedAlumnosViewModel sharedAlumnosViewModel;
    private PeticionesJson peticionesJson;
    private CreaPaeAdapter creaPaeAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crear_pae, container, false);
        getActivity().setTitle("Crea PAE");
        menuHost = requireActivity();

        inicializaViewModel();
        cambiarToolbar();
        inicializaVariables(view);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rellenaDatosConocidos(view);
        creaTablaSeguimiento(view);
    }

    public void inicializaVariables(View view){
        claseGlobal = ClaseGlobal.getInstance();
        tablaPae = view.findViewById(R.id.tablaControlPae);
    }

    public void cambiarToolbar(){
        MenuProvider menuProvider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.app_menu_opciones_pae_editar, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.action_confirmar_pae){
                    //subePAE();
                    return true;
                }

                return false;
            }
        };

        requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
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

    public void rellenaDatosConocidos(View view){
        sharedAlumnosViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Alumnos>() {
            @Override
            public void onChanged(Alumnos alumnos) {
                // Limpia los datos del PAE al cambiar de alumno
                seteaDatos(alumnos, view);
            }
        });

    }

    public void seteaDatos(Alumnos alumno, View view){
        sharedAlumnosViewModel.obtieneListaCursos().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> cursos) {
                creaPaeAdapter = new CreaPaeAdapter(alumno, cursos, claseGlobal, requireActivity());
                creaPaeAdapter.rellenaUI(view);
            }
        });


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
        tv.setTextSize(28);
        tv.setTextColor(Color.WHITE); // Texto en blanco

        fila.addView(tv);

// Ahora agregar las tallas
        for (int x = 0; x < header.length; x++) {
            TextView tvTalla = new TextView(getActivity());
            tvTalla.setText(header[x]);
            tv.setTextSize(28);

            tvTalla.setTextColor(Color.WHITE); // Texto en blanco

            fila.addView(tvTalla);
        }
// Finalmente agregar la fila en la primera posición
        tablaPae.addView(fila, 0);
        // Rellenar la tabla con los datos de cada propiedad para cada trimestre
        rellenaFilas(view, lp);

    }

    public void rellenaFilas(View view, TableRow.LayoutParams lp){
        // Crear una fila para cada propiedad
        for (String propiedad : propiedades) {
            TableRow filaPropiedad = new TableRow(getActivity());
            filaPropiedad.setLayoutParams(lp);
            // Establecer el fondo de color de la fila


            // Agregar el nombre de la propiedad como encabezado
            TextView textViewPropiedad = new TextView(getActivity());
            textViewPropiedad.setText(propiedad);
            textViewPropiedad.setTypeface(null, Typeface.BOLD);
            textViewPropiedad.setTextSize(24); // Tamaño de letra grande
            textViewPropiedad.setTextColor(Color.WHITE); // Texto en blanco
            textViewPropiedad.setBackgroundColor(Color.parseColor("#006B58")); // Fondo verde oscuro

            filaPropiedad.addView(textViewPropiedad);

            // Agregar los datos correspondientes para cada trimestre
            for (int i =0; i<3; i++) {
                EditText editText = new EditText(getActivity());
                //String dato = obtenerDatoPropiedad(control, propiedad);
                //editText.setText(dato);


                filaPropiedad.addView(editText);
            }

            // Agregar la fila a la tabla
            tablaPae.addView(filaPropiedad);
        }


    }


    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AlumnosFragment()).commit();
        return true;
    }
}