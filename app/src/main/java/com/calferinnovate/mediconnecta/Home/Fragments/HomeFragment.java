package com.calferinnovate.mediconnecta.Home.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.Home.Fragments.HomeFragments.AvisosListViewFragment;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.Avisos;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Constantes;
import com.calferinnovate.mediconnecta.clases.Fechas;
import com.calferinnovate.mediconnecta.clases.Pacientes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment{

    private CalendarView calendario;
    private AvisosListViewFragment avisosListViewFragment;
    private Button abrirFragmentoListaAvisos;


    private Calendar calendar;

    private Avisos avisos;
    private Fechas fechaSeleccionada;

    private Pacientes pacientes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Declaramos las variables globales y referenciamos nuestras variables con sus correspondientes
        //componentes xml
        View vista = inflater.inflate(R.layout.fragment_home, container, false);


            //Obtenemos la referencia al ListView desde onCreateView
            asociarVariablesConComponentes(vista);
            declaracionObjetosClaseGlobal();
            return vista;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Obtenemos una instancia de calendario ya que CalendarView no contiene estos datos.
        calendar = Calendar.getInstance();


        //Si clickamos en algún día del CalendarView, setearemos en el calendario de java el día clickeado
        //y llamando a fechaSeleccionada guardaremos esa fecha en formato de año/mes/dia para poder pasarla
        //la base de datos
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                fechaSeleccionada.setFechaActual(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
            }
        });

        //CUando clcikememos en el botón mostraremos en el fragmentContainer el fragment que contiene el
        //ListView
        abrirFragmentoListaAvisos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //abrirAvisosDiarios(fechaSeleccionada);
               // getParentFragmentManager() .beginTransaction().replace(R.id.fragmentContainerViewLIstView, new AvisosListViewFragment()).commit();
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewLIstView, new AvisosListViewFragment()).commit();
                Log.d("avisos", "Se ha implementado el nuevo fragmento");
            }
        });
    }

    public void declaracionObjetosClaseGlobal(){
        avisos = ((ClaseGlobal) getActivity().getApplicationContext()).avisos;
        fechaSeleccionada = ((ClaseGlobal) getActivity().getApplicationContext()).fechas;
        pacientes = ((ClaseGlobal) getActivity().getApplicationContext()).pacientes;

    }

    public void asociarVariablesConComponentes(View view){
        calendario = (CalendarView) view.findViewById(R.id.calendarView);
        abrirFragmentoListaAvisos = (Button) view.findViewById(R.id.abrirAvisos);

    }



}