package com.calferinnovate.mediconnecta.View.Sesion;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.View.Home.HomeActivity;
import com.calferinnovate.mediconnecta.ViewModel.SesionViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Fragmento donde se realiza el autenticación del inicio de sesión del empleado.
 *
 * @author Irene Caldelas Fernánde
 */
public class SesionFragment extends Fragment {

    private TextInputEditText username;

    private TextInputEditText password;
    private ImageView logo;
    private Button btnAcceso;
    private NavController navController;
    private ClaseGlobal claseGlobal;
    private SesionViewModel sesionViewModel;
    private PeticionesJson peticionesJson;
    private TextInputLayout userLayout;
    private TextInputLayout passLayout;


    /**
     * Método llamado cuando se crea la vista del fragmento.
     * Infla el diseño de la UI desde el archivo XML fragment_sesion.xml.
     * Llama a inicializaVariables() para obtener la instancia de ClaseGlobal.
     * Llama a inicializacionViewModel() para configurar el SesionViewModel.
     *
     * @param inflater           inflador utilizado para inflar el diseño de la UI.
     * @param container          Contenedor que contiene la vista del fragmento
     * @param savedInstanceState Estado de guardado de la instancia del fragmento*
     * @return vista Es la vista inflada del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Necesitamos un objeto de tipo View
        View vista = inflater.inflate(R.layout.fragment_sesion, container, false);
        inicializaVariables();
        inicializacionViewModel();


        return vista;
    }

    /**
     * Método que obtiene la instancia de ClaseGlobal.
     */
    public void inicializaVariables() {
        claseGlobal = ClaseGlobal.getInstance();
    }

    /**
     * Método que configura el ViewModel SesionViewModel mediante la creación de un ViewModelFactory que proporciona
     * instancias de Peticiones Json y ClaseGloabl al ViewModel.
     */
    public void inicializacionViewModel() {
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

        ViewModelFactory<SesionViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        // Inicializa el ViewModel
        sesionViewModel = new ViewModelProvider(requireActivity(), factory).get(SesionViewModel.class);
    }

    /**
     * Método llamado cuando la vista ya ha sido creada.
     * Se asigna a la variable navController el controlador de navegación correspondiente al fragmento actual.
     * Llama a asociacionVariablesComponentes(View) para asociar las variables declaradas a los elementos
     * de la UI.
     * Llama a vertificaEstadoInicioSesion() para verificar el estado del inicio de sesión.
     * Si el botón de inicio de sesión es pulsado llama a inicioSesion()
     *
     * @param view               Vista retornada por el inflador.
     * @param savedInstanceState Si no es nulo, este fragmento será reconstruido a partir de un
     *                           estado anterior guardado.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        asociacionVariableComponente(view);
        configuraImagenLogo();
        verificaEstadoInicioSesion();

        //Relaccionamos el botón de Acceso con el Listener para que actúe cuando sea presionado
        btnAcceso.setOnClickListener(v -> iniciarSesion());
    }

    /**
     * Método que asocia las variables declaradas en el fragmento con los elementos de la UI de nuestro
     * layout.
     *
     * @param view Es la vista inflada
     */
    public void asociacionVariableComponente(View view) {
        // Vamos a referenciar estos objetos con el XML
        username = view.findViewById(R.id.user);
        password = view.findViewById(R.id.pass);
        btnAcceso = view.findViewById(R.id.btnAcceso);
        userLayout = view.findViewById(R.id.userLabel);
        passLayout = view.findViewById(R.id.passLabel);
        logo = view.findViewById(R.id.imagenLogo);
    }


    private void configuraImagenLogo() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        int targetWidth = Math.min(screenWidth, 500); // Tamaño máximo
        int targetHeight = Math.min(screenHeight, 500); // Tamaño máximo

        Glide.with(this).load(getImage("logo_mediconnecta")).override(targetWidth, targetHeight).into(logo);
    }

    private int getImage(String imageName) {
        int drawableResourceId = this.getResources().getIdentifier(imageName, "drawable", requireActivity().getPackageName());
        return drawableResourceId;
    }

    /**
     * Método que se llama en el onViewCreate para verificar el estado del inicio de sesión.
     * Llama al método getEmpleadoIniciaSesion del ViewModel SesionViewModel y comprueba si los datos
     * de inicio son correctos o no.
     * Si los datos de inicio de sesión son correctos muestra un Toast que da la bienvenida al empleado
     * y pasa al siguiente fragmento con el NavController.
     * En caso de que los datos hayan sido incorrectos muestra un mensaje de error.
     */
    public void verificaEstadoInicioSesion() {
        sesionViewModel.getEmpleadoIniciaSesion().observe(getViewLifecycleOwner(), sesion -> {
            if (sesion) {
                Toast.makeText(getContext(), "Permitiendo el acceso al usuario " + username.getText().toString(), Toast.LENGTH_SHORT).show();
                obtieneDatos(HomeActivity.class);
            } else {
                Toast.makeText(getContext(), "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Método que se llama cuadno el usuario presiona el botón de inicio de sesión.
     * Llama al método inicioSesión del ViewModel SesionViewModel para realizar la autenticación
     * del usuario
     */
    private void iniciarSesion() {
        validacionesDatos(username.getText().toString(), password.getText().toString());
        sesionViewModel.inicioSesion(username.getText().toString(), password.getText().toString());
    }

    /**
     * Validaciones datos.
     *
     * @param user Nombre de usuario
     * @param pass Contraseña del empleado
     */
    private void validacionesDatos(String user, String pass) {
        if (TextUtils.isEmpty(user)) {
            userLayout.setError("El usuario no puede estar vacío");
            //username.setError("El campo de usuario no puede estar vacío");
        }
        if (TextUtils.isEmpty(pass)) {
            passLayout.setError("La contraseña no puede estar vacío");
        }

        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
            return;
        }
    }

    public void obtieneDatos(Class activityName) {
        sesionViewModel.obtieneDatosEmpleados().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean obtenidos) {

            }
        });
        sesionViewModel.obtieneRelacionEmpleadosAulas().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

            }
        });

        sesionViewModel.obtieneAulas().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

            }
        });

        sesionViewModel.obtieneNivelEscolar().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

            }
        });

        sesionViewModel.obtieneCargos().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

            }
        });
        sesionViewModel.obtieneDatosAlumnos().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean obtenidos) {
                if (obtenidos) {
                    Toast.makeText(getContext(), "Permitiendo el acceso al usuario " + username.getText().toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), activityName);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Error al obtener datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}