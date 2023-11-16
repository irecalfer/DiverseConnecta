package com.calferinnovate.mediconnecta.Sesion;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.PeticionesHTTP.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.Empleado;
import com.calferinnovate.mediconnecta.ViewModel.NormasViewModel;
import com.calferinnovate.mediconnecta.ViewModel.SeleccionUnidadViewModel;
import com.calferinnovate.mediconnecta.ViewModel.SesionViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fragmento donde se realiza el autenticación del inicio de sesión del empleado.
 * @author Irene Caldelas Fernánde
 */

public class SesionFragment extends Fragment{

    private TextInputEditText username;
    private TextInputEditText password;
    private Button btnAcceso;
    private NavController navController;
    private ClaseGlobal claseGlobal;
    private ViewModelArgs viewModelArgs;
    private SesionViewModel sesionViewModel;
    private PeticionesJson peticionesJson;


    /**
     * Método llamado cuando se crea la vista del fragmento.
     * Infla el diseño de la UI desde el archivo XML fragment_sesion.xml.
     * Llama a llamadaAClaseGlobal() para obtener la instancia de ClaseGlobal.
     * Configura el ViewModel SesionViewModel mediante la creación de un ViewModelFactory que proporciona
     * instancias de Peticiones Json y ClaseGloabl al ViewModel.
     *
     * @param inflater inflador utilizado para inflar el diseño de la UI.
     * @param container Contenedor que contiene la vista del fragmento
     * @param savedInstanceState Estado de guardado de la instancia del fragmento
     *
     *
     * @return vista Es la vista inflada del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Necesitamos un objeto de tipo View
        View vista = inflater.inflate(R.layout.fragment_sesion, container, false);
        llamadaAClaseGlobal();

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

        ViewModelFactory<SesionViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        // Inicializa el ViewModel
        sesionViewModel = new ViewModelProvider(requireActivity(), factory).get(SesionViewModel.class);


        return vista;
    }

    /**
     * Método que obtiene la instancia de ClaseGlobal.
     */
    public void llamadaAClaseGlobal() {
        claseGlobal = ClaseGlobal.getInstance();
    }

    /**
     * Método llamado cuando la vista ya ha sido creada.
     *
     * Se asigna a la variable navController el controlador de navegación correspondiente al fragmento actual.
     * Llama a asociacionVariablesComponentes(View) para asociar las variables declaradas a los elementos
     * de la UI.
     * Llama a vertificaEstadoInicioSesion() para verificar el estado del inicio de sesión.
     * Si el botón de inicio de sesión es pulsado llama a inicioSesion()
     * @param view Vista retornada por el inflador.
     * @param savedInstanceState Si no es nulo, este fragmento será reconstruido a partir de un
     *                           estado anterior guardado.
     */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        asociacionVariableComponente(view);

        verificaEstadoInicioSesion();

        //Relaccionamos el botón de Acceso con el Listener para que actúe cuando sea presionado
        btnAcceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });
    }

    /**
     * Método que asocia las variables declaradas en el fragmento con los elementos de la UI de nuestro
     * layout.
     * @param view Es la vista inflada
     */
    public void asociacionVariableComponente(View view) {
        // Vamos a referenciar estos objetos con el XML
        username = view.findViewById(R.id.user);
        password = view.findViewById(R.id.pass);
        btnAcceso = view.findViewById(R.id.btnAcceso);

    }


    /**
     * Método que se llama en el onViewCreate para verificar el estado del inicio de sesión.
     * Llama al método getEmpleadoIniciaSesion del ViewModel SesionViewModel y comprueba si los datos
     * de inicio son correctos o no.
     * Si los datos de inicio de sesión son correctos muestra un Toast que da la bienvenida al empleado
     * y pasa al siguiente fragmento con el NavController.
     * En caso de que los datos hayan sido incorrectos muestra un mensaje de error.
     */
    public void verificaEstadoInicioSesion(){
        sesionViewModel.getEmpleadoIniciaSesion().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean sesion) {
                if(sesion){
                    Toast.makeText(getContext(), "Permitiendo el acceso al usuario " + username.getText().toString(), Toast.LENGTH_SHORT).show();
                    navController.navigate(R.id.action_sesionFragment_to_seleccionUnidadFragment);
                }else{
                    Toast.makeText(getContext(), "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Método que se llama cuadno el usuario presiona el botón de inicio de sesión.
     * Llama al método inicioSesión del ViewModel SesionViewModel para realizar la autenticación
     * del usuario
     */
    private void iniciarSesion() {
        sesionViewModel.inicioSesion( username.getText().toString(), password.getText().toString());
    }

}