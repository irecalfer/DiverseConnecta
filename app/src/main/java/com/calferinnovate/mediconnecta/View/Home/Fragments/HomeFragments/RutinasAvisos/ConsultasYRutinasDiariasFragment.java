package com.calferinnovate.mediconnecta.View.Home.Fragments.HomeFragments.RutinasAvisos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.calferinnovate.mediconnecta.Adaptadores.RutinasAdapter;
import com.calferinnovate.mediconnecta.View.Home.Fragments.HomeFragment;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.Model.PacientesAgrupadosRutinas;
import com.calferinnovate.mediconnecta.Model.Unidades;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.ViewModel.ConsultasYRutinasDiariasViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ConsultasYRutinasDiariasFragment extends Fragment implements IOnBackPressed {

    private RecyclerView rvConsultas;
    private RutinasAdapter rutinasAdapter;
    private TabLayout tabLayout;
    private Unidades unidades;
    private ClaseGlobal claseGlobal;
    private ArrayList<Pacientes> listaPacientes;
    private EditText fechaRutina;
    private ConsultasYRutinasDiariasViewModel consultasYRutinasDiariasViewModel;
    private String tipoRutinaActual;
    private PeticionesJson peticionesJson;
    private ViewModelArgs viewModelArgs;
    private String fecha;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_consultas_y_rutinas_diarias, container, false);

        inicializaVariables(vista);
        getActivity().setTitle("Consultas y rutinas");
        inicializaViewModel();
        obtieneFechaYFormatea();

        return vista;
    }

    public void inicializaVariables(View view) {
        claseGlobal = ClaseGlobal.getInstance();
        unidades = claseGlobal.getUnidades();
        listaPacientes = claseGlobal.getListaPacientes();

        rvConsultas = view.findViewById(R.id.rvRutinas);
        tabLayout = view.findViewById(R.id.tabLayoutRutinas);
        fechaRutina = view.findViewById(R.id.fechaActual);
    }


    public void obtieneFechaYFormatea(){
        Bundle bundle = getArguments();
        if(bundle!= null){
            fecha = bundle.getString("fecha");
            if(fecha != null && !fecha.isEmpty()){
               setearFecha(fecha);
            }
        }
    }
    private void setearFecha(String fecha) {
        //Seteamos la fecha en el EditText
        DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Formato de salida (día-mes-año abreviado)
        DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        // Parsea la fecha de entrada
        LocalDate fechaEntrada = LocalDate.parse(fecha, formatoEntrada);

        // Formatea la fecha en el formato de salida
        String fechaFormateada = fechaEntrada.format(formatoSalida);
        fechaRutina.setText(fechaFormateada); // FORMATEAR FECHA PARA QUE APAREZCA DE FORMA dd/MM/YYYY
    }


    public void inicializaViewModel(){
        //Creas un objeto ViewModelFactory y obtienes una instancia de ConsultasYRutinasDiariasViewModel utilizando este factory.
        //Luego, observas el LiveData del ViewModel para mantener actualizada la lista de programación en el RecyclerView.
        viewModelArgs = new ViewModelArgs() {
            @Override
            public PeticionesJson getPeticionesJson() {
                return peticionesJson = new PeticionesJson(requireContext());
            }

            @Override
            public ClaseGlobal getClaseGlobal() {
                return claseGlobal;
            }
        };

        ViewModelFactory<ConsultasYRutinasDiariasViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        // Inicializa el ViewModel
        consultasYRutinasDiariasViewModel = new ViewModelProvider(this, factory).get(ConsultasYRutinasDiariasViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listenerTabs();


    }

    public void listenerTabs() {
        // Inicialmente, obtén los datos para el tab actual
        if (tabLayout.getTabCount() > 0) {
            tipoRutinaActual = (String) tabLayout.getTabAt(0).getText();
            obtenerDatosRutinas();
        }

        //Estableces un listener en el TabLayout para manejar eventos cuando se seleccionan diferentes pestañas (tabs)
        // y obtienes datos de rutinas correspondientes.
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tipoRutinaActual = (String) tab.getText();
                obtenerDatosRutinas();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No es necesario implementar esto
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No es necesario implementar esto
            }
        });
    }

    private void obtenerDatosRutinas() {
        // Realiza la solicitud HTTP utilizando el ViewModel y pasa la fecha, unidad y tipo de rutina
        consultasYRutinasDiariasViewModel.obtieneDatosRutinasDiaPacientes(fecha,
                unidades.getNombreUnidad(), tipoRutinaActual).observe(getViewLifecycleOwner(), new Observer<ArrayList<PacientesAgrupadosRutinas>>() {
            @Override
            public void onChanged(ArrayList<PacientesAgrupadosRutinas> pacientesAgrupadosRutinas) {
                rutinasAdapter = new RutinasAdapter(pacientesAgrupadosRutinas, listaPacientes, getContext());
                rvConsultas.setAdapter(rutinasAdapter);
                // Notifica al adaptador que los datos han cambiado
                rutinasAdapter.notifyDataSetChanged();
            }
        });


    }


    @Override
    public boolean onBackPressed() {
        // Agrega la lógica específica del fragmento para manejar el retroceso.
        // Devuelve true si el fragmento maneja el retroceso, de lo contrario, devuelve false.
        // Por ejemplo, si deseas que al presionar Atrás en este fragmento vuelva a la pantalla principal:
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        return true;
    }
}