package com.calferinnovate.mediconnecta.View.Home;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Empleado;
import com.calferinnovate.mediconnecta.Model.Unidades;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.AnadirPacienteFragment;
import com.calferinnovate.mediconnecta.View.Home.Fragments.HomeFragment;
import com.calferinnovate.mediconnecta.View.Home.Fragments.NormasEmpresaFragment;
import com.calferinnovate.mediconnecta.View.Home.Fragments.PacientesFragment;
import com.calferinnovate.mediconnecta.View.Home.Fragments.ParteGeneralFragment;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.View.Sesion.MainActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;

/**
 * HomeActivity proporciona la funcionalidad de Navegación entre los fragmentos del Home a través del
 * Navigation Drawer y diálogos para el cambio de unidad y cierre de sesión.
 * Implementa un retroceso personalizado.
 */
public class HomeActivityAdministrativos extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnBackPressedDispatcherOwner {

    private DrawerLayout drawerLayout;
    private Empleado empleado;
    private Unidades unidad;
    private Toolbar toolbar;
    private NavigationView navigationView;

    /**
     * Se llama cuando se crea la actividad. Configura la interfaz de usuario, inicializa
     * variables y establece escuchadores.
     *
     * @param savedInstanceState Si no es nulo, este fragmento será reconstruido a partir de un
     *                           estado anterior guardado.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_administrativos);
        FirebaseApp.initializeApp(this);
        inicializaVariables();
        enlazaRecursos();

        estableceBarraDeHerramientas();
        rellenaDatosPersonalesNavigationDrawer(navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        sincronizaNavigationDrawerConToolbar();

        creaRetroceso();

        compruebaEstadoInstancia(savedInstanceState);
    }

    /**
     * Inicializa las variables necesarias para la Activity.
     */
    public void inicializaVariables() {
        ClaseGlobal claseGlobal = ClaseGlobal.getInstance();
        empleado = claseGlobal.getEmpleado();
        unidad = claseGlobal.getUnidades();
    }

    /**
     * Enlaza los recursos con las variables.
     */
    public void enlazaRecursos() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
    }

    /**
     * Establece la barra de herramientas como la barra de acción de la Activity y habilita el botón
     * atrás en la barra de acción.
     */
    private void estableceBarraDeHerramientas() {
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Rellena los datos personales del empleado en la cabecera del Navigation Drawer.
     *
     * @param navigationView Vista del Navigation Drawer.
     */
    public void rellenaDatosPersonalesNavigationDrawer(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        ImageView fotoEmpleadoND = headerView.findViewById(R.id.fotoEmpleadoND);
        TextView nombreEmpleadoND = headerView.findViewById(R.id.nombreEmpleadoND);
        TextView unidadND = headerView.findViewById(R.id.unidadND);

        Glide.with(getApplicationContext()).load(empleado.getFoto()).circleCrop().into(fotoEmpleadoND);

        nombreEmpleadoND.setText(String.format("%s %s", empleado.getNombre(), empleado.getApellidos()));
        unidadND.setText(unidad.getNombreUnidad());
    }

    /**
     * Sincroniza el Navigation Drawer con la barra de herramientas para mostrar el icono de las 3 líneas.
     */
    public void sincronizaNavigationDrawerConToolbar() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * Comprueba si la instancia de la actividad es nula y de ser así realiza la transición a HomeFragment
     * conteniendolo en su fragment container.
     *
     * @param savedInstanceState Información sobre el estado anterior de la actividad.
     */
    public void compruebaEstadoInstancia(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.home_item_id);
        }
    }

    /**
     * Escucha que ítem del Navigation Drawer ha sido seleccionado y en función de ello reemplaza el
     * fragment container con el fragmento correspondiente.
     * En caso de que el ítem sea de Cambio de unidad o Cierre Sesión llama a los métodos que contienen
     * el dialog personalizado.
     * @param item El ítem seleccionado.
     * @return true si el evento se ha manejado correctamente y false en caso contrario.
     */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home_item_id) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        } else if (item.getItemId() == R.id.pacientes_item_id) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PacientesFragment()).addToBackStack(null).commit();
        } else if(item.getItemId() == R.id.añadir_paciente_item_id){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AnadirPacienteFragment()).addToBackStack(null).commit();
        }else if (item.getItemId() == R.id.parte_general_item_id) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ParteGeneralFragment()).addToBackStack(null).commit();
        } else if (item.getItemId() == R.id.normas_empresa_item_id) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NormasEmpresaFragment()).addToBackStack(null).commit();
        } else if (item.getItemId() == R.id.cierre_sesion_item_id) {
            dialogCerrarSesion();
        } else if (item.getItemId() == R.id.cambio_unidad_item_id) {
            dialogCambioUnidad();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Crea el retroceso personalizado para para gestionar la navegación y el retroceso en la actividad y sus
     * correspondientes fragmentos.
     */
    public void creaRetroceso() {

        // Crea un OnBackPressedCallback para manejar el retroceso en la actividad
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Lógica para manejar el retroceso
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

                if (currentFragment instanceof IOnBackPressed) {
                    boolean handled = ((IOnBackPressed) currentFragment).onBackPressed();
                    if (handled) {
                        // El fragmento actual manejó el retroceso, no hagas nada más
                        return;
                    }

                }
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack(); // Retrocede en la pila de fragmentos
                } else {
                    // Manejar el retroceso en la actividad según tu lógica
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).addToBackStack(null).commit();
                }
            }
        };

        // Agrega el callback al OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    /**
     * Muestra un diálogo para confirmar el cierre de sesión.
     * Si se confirma vuelve a MainActivity al fragmento de inicio de sesión, en caso contrario el
     * dialogo se cierra y se mantiene en el fragmento desde el que se le llamó.
     */
    public void dialogCerrarSesion() {
        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialog_cerrar_sesion_y_cambio_unidad);

        TextView texto = dialog.findViewById(R.id.textViewCierreCambio);
        texto.setText(R.string.cierreSesion);
        TextView siSalir = dialog.findViewById(R.id.textViewSi);
        TextView noSalir = dialog.findViewById(R.id.textViewNo);

        noSalir.setOnClickListener(v -> dialog.dismiss());

        siSalir.setOnClickListener(v -> {
            dialog.dismiss();

            Intent intent = new Intent(HomeActivityAdministrativos.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        dialog.show();
    }

    /**
     * Muestra un diálogo para confirmar el cambio de unidad-
     * Si se confirma vuelve al fragmento SeleccionUnidadFragment, en caso contrario el
     * dialogo se cierra y se mantiene en el fragmento desde el que se le llamó.
     */
    public void dialogCambioUnidad() {
        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialog_cerrar_sesion_y_cambio_unidad);

        TextView texto = dialog.findViewById(R.id.textViewCierreCambio);
        texto.setText(R.string.cambioUnidad);
        TextView siSalir = dialog.findViewById(R.id.textViewSi);
        TextView noSalir = dialog.findViewById(R.id.textViewNo);


        noSalir.setOnClickListener(v -> {
            //Cerramos el dialogo
            dialog.dismiss();
        });

        siSalir.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        //mostramos el dialogo de salida
        dialog.show();
    }
}