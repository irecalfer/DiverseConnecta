package com.calferinnovate.mediconnecta.Home.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.calferinnovate.mediconnecta.Home.Fragments.HomeFragments.Rutinas.AvisosListViewFragment;
import com.calferinnovate.mediconnecta.Home.Fragments.HomeFragments.Rutinas.ConsultasYRutinasDiariasFragment;
import com.calferinnovate.mediconnecta.Model.Avisos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Fechas;
import com.calferinnovate.mediconnecta.Model.Rutinas;
import com.calferinnovate.mediconnecta.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private CalendarView calendario;
    private AvisosListViewFragment avisosListViewFragment;
    private Button abrirFragmentoListaAvisos;
    private Button rutinasBtn;

    private Calendar calendar;

    private Avisos avisos;
    private Fechas fechaSeleccionada;


    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue requestQueue;
    private Rutinas rutinas;
    private ClaseGlobal claseGlobal;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Declaramos las variables globales y referenciamos nuestras variables con sus correspondientes
        //componentes xml
        View vista = inflater.inflate(R.layout.fragment_home, container, false);

        asignaObjetosClaseGlobal();
        //Obtenemos la referencia al ListView desde onCreateView
        asociarVariablesConComponentes(vista);

        return vista;

    }

    public void asignaObjetosClaseGlobal() {
        claseGlobal = ClaseGlobal.getInstance();
        avisos = claseGlobal.getAvisos();
        fechaSeleccionada = claseGlobal.getFechas();
    }

    public void asociarVariablesConComponentes(View view) {
        calendario = view.findViewById(R.id.calendarView);
        abrirFragmentoListaAvisos = view.findViewById(R.id.abrirAvisos);
        rutinasBtn = view.findViewById(R.id.abrirRutinas);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Obtenemos una instancia de calendario ya que CalendarView no contiene estos datos.
        calendar = Calendar.getInstance();


        setearFechaSeleccionada();
        abrirFragmentoAvisos();


        //Cuando clcikememos en el botón mostraremos el fragment que contiene las rutinas
        rutinasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConsultasYRutinasDiariasFragment()).commit();
            }
        });
    }


    public void setearFechaSeleccionada() {
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
    }

    public void abrirFragmentoAvisos() {
        //CUando clcikememos en el botón mostraremos en el fragmentContainer el fragment que contiene el
        //ListView
        abrirFragmentoListaAvisos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewLIstView, new AvisosListViewFragment()).commit();
                Log.d("avisos", "Se ha implementado el nuevo fragmento");
            }
        });
    }

}