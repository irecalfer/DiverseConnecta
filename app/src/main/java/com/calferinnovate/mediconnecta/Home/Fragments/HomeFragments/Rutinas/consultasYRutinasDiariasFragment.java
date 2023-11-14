package com.calferinnovate.mediconnecta.Home.Fragments.HomeFragments.Rutinas;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.calferinnovate.mediconnecta.Adaptadores.RutinasAdapter;
import com.calferinnovate.mediconnecta.Home.Fragments.HomeFragment;
import com.calferinnovate.mediconnecta.Home.HomeActivity;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Fechas;
import com.calferinnovate.mediconnecta.clases.IOnBackPressed;
import com.calferinnovate.mediconnecta.clases.Pacientes;
import com.calferinnovate.mediconnecta.clases.PacientesAgrupadosRutinas;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.PeticionesJson;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.ConsultasYRutinasDiariasViewModel;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.ViewModelFactory;
import com.calferinnovate.mediconnecta.clases.Rutinas;
import com.calferinnovate.mediconnecta.clases.Unidades;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class consultasYRutinasDiariasFragment extends Fragment implements IOnBackPressed {

    private RecyclerView rvConsultas;
    private String url;
    private RutinasAdapter rutinasAdapter;
    private TabLayout tabLayout;
    private PacientesAgrupadosRutinas pacientesAgrupadosRutinas;
    private Fechas fechas;
    private Unidades unidades;
    private Rutinas rutinas;
    private ClaseGlobal claseGlobal;
    private Pacientes pacientes;
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private ArrayList<Rutinas> listaRutinas = new ArrayList<>();
    private EditText fechaRutina;
    private ConsultasYRutinasDiariasViewModel consultasYRutinasDiariasViewModel;
    String tipoRutinaActual;
    private PeticionesJson peticionesJson;
    private ViewModelArgs viewModelArgs;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_consultas_y_rutinas_diarias, container, false);

        referenciaVariables(vista);
        llamadaObjetosGlobales();

        //Seteamos la fecha en el EditText
        fechaRutina.setText(fechas.getFechaActual()); // FORMATEAR FECHA PARA QUE APAREZCA DE FORMA dd/MM/YYYY
        // Inicializa el adaptador una sola vez
        //Configuras el adaptador rutinasAdapter y lo estableces en el RecyclerView (rvConsultas).
        rutinasAdapter = new RutinasAdapter(claseGlobal.getListaProgramacion(), claseGlobal.getListaPacientes(),getContext());
        rvConsultas.setAdapter(rutinasAdapter);

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

        // Observación de LiveData: Has configurado la observación de un LiveData en el ViewModel utilizando el método observe(). Cuando los datos cambian en el LiveData,
        // el adaptador se actualiza automáticamente para reflejar los cambios en el RecyclerView.
        consultasYRutinasDiariasViewModel.getListaProgramacionLiveData(fechas.getFechaActual(), unidades.getNombreUnidad(), tipoRutinaActual)
                .observe(getViewLifecycleOwner(), new Observer<ArrayList<PacientesAgrupadosRutinas>>() {
                    @Override
                    public void onChanged(ArrayList<PacientesAgrupadosRutinas> newData) {
                        // Actualizar el adaptador con los nuevos datos
                        rutinasAdapter.actualizarDatos(newData);
                    }
                });

        //Estableces un listener en el TabLayout para manejar eventos cuando se seleccionan diferentes pestañas (tabs)
        // y obtienes datos de rutinas correspondientes.
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tipoRutinaActual = (String) tab.getText();
                // Limpia los datos actuales en el adaptador
                claseGlobal.getListaProgramacion().clear();
                // Notifica al adaptador que los datos han cambiado
                rutinasAdapter.notifyDataSetChanged();
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

        // Inicialmente, obtén los datos para el tab actual
        if (tabLayout.getTabCount() > 0) {
            tipoRutinaActual = (String) tabLayout.getTabAt(0).getText();
            obtenerDatosRutinas();
        }

        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    public void referenciaVariables(View view){
        rvConsultas = view.findViewById(R.id.rvRutinas);
        tabLayout = view.findViewById(R.id.tabLAyoutRutinas);
        fechaRutina = view.findViewById(R.id.fechaActual);
    }

    public void llamadaObjetosGlobales(){
        claseGlobal = ClaseGlobal.getInstance();
        fechas = claseGlobal.getFechas();
        unidades = claseGlobal.getUnidades();
        rutinas = claseGlobal.getRutinas();
        pacientesAgrupadosRutinas = claseGlobal.getPacientesAgrupadosRutinas();
        pacientes = claseGlobal.getPacientes();
    }

    private void obtenerDatosRutinas() {
        // Realiza la solicitud HTTP utilizando el ViewModel y pasa la fecha, unidad y tipo de rutina
        consultasYRutinasDiariasViewModel.obtieneDatosRutinasDiaPacientes(fechas.getFechaActual(),
                unidades.getNombreUnidad(), tipoRutinaActual);


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