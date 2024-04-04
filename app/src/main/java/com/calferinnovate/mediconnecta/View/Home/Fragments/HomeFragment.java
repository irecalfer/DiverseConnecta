package com.calferinnovate.mediconnecta.View.Home.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.HomeFragments.RutinasAvisos.AvisosListViewFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * HomeFragment es el fragmento por defecto cuando se inicia HomeActivity.
 * Proporciona un CalendarView donde los empleados seleccionarán una fecha y botones que les redirigirán
 * a los avisos o rutinas de los pacientes del día seleccionado.
 */
public class HomeFragment extends Fragment {

    private CalendarView calendario;
    private Button avisosBtn;
    private Button rutinasBtn;
    private Calendar calendar;
    private String fechaActual;


    /**
     * Método llamado cuando se crea la vista del fragmento.
     * Infla el diseño de la UI desde el archivo XML fragment_home.xml.
     *
     * @param inflater           inflador utilizado para inflar el diseño de la UI.
     * @param container          Contenedor que contiene la vista del fragmento
     * @param savedInstanceState Estado de guardado de la instancia del fragmento
     * @return vista Es la vista inflada del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("Home");

        enlazaRecursos(vista);

        return vista;

    }

    /**
     * Método que enlaza los recursos de la UI con las variables miembros del fragmento.
     *
     * @param view La vista inflada.
     */
    public void enlazaRecursos(View view) {
        calendario = view.findViewById(R.id.calendarView);
        avisosBtn = view.findViewById(R.id.abrirAvisos);
        rutinasBtn = view.findViewById(R.id.abrirRutinas);
    }

    /**
     * Método llamado cuando la vista ya ha sido creada.
     * Obtiene una instancia del Calendario.
     *
     * @param view               Vista retornada por el inflador.
     * @param savedInstanceState Si no es nulo, este fragmento será reconstruido a partir de un
     *                           estado anterior guardado.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendar = Calendar.getInstance();
        setearFechaSeleccionada();
    }


    /**
     * Método que establece un listener al CalendarView para manejar la fecha seleccionada.
     * La fecha la formatearemos a yyyy-MM-dd para poder pasarla en las consultas a la base de datos.
     * Una vez seleccionada la fecha se llama a abrirFragmentoAvisos o listenerButtonRutinas y les pasa
     * la fecha seleccionada.
     */
    public void setearFechaSeleccionada() {
        calendario.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            abrirFragmentoAvisos(fechaActual);
            //listenerButtonRutinas(fechaActual);
        });
    }

    /**
     * Método con escucha del botón avisosBtn. Cuando sea clickado le pasará al fragmento
     * la fecha y reemplazará el fragment container fragmentContainerViewLIstView con el fragmento
     * avisosListViewFragment.
     *
     * @param fechaActual Fecha seleccionada.
     */
    public void abrirFragmentoAvisos(String fechaActual) {
        avisosBtn.setOnClickListener(v -> {
            AvisosListViewFragment avisosListViewFragment = new AvisosListViewFragment();

            Bundle bundleFecha = new Bundle();
            bundleFecha.putString("fecha", fechaActual);
            avisosListViewFragment.setArguments(bundleFecha);

            FragmentTransaction transaction = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerViewLIstView, avisosListViewFragment);
            transaction.setReorderingAllowed(true);
            transaction.addToBackStack(null);
            transaction.commit();

        });
    }

    /**
     * Método con escucha del botón rutinasBtn. Cuando sea clickado le pasará al fragmento
     * la fecha y reemplazará el fragment container fragment_container con el fragmento
     * consultasYRutinasDiariasFragment.
     *
     * @param fechaActual Fecha seleccionada.
     */
    /*public void listenerButtonRutinas(String fechaActual) {
        rutinasBtn.setOnClickListener(v -> {
            ConsultasYRutinasDiariasFragment consultasYRutinasDiariasFragment = new ConsultasYRutinasDiariasFragment();

            Bundle bundleFechaRutina = new Bundle();
            bundleFechaRutina.putString("fecha", fechaActual);
            consultasYRutinasDiariasFragment.setArguments(bundleFechaRutina);

            FragmentTransaction transaction = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, consultasYRutinasDiariasFragment);
            transaction.setReorderingAllowed(true);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }*/

}