package com.calferinnovate.mediconnecta.Home;

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

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.calferinnovate.mediconnecta.Home.Fragments.HomeFragment;
import com.calferinnovate.mediconnecta.Home.Fragments.NormasEmpresaFragment;
import com.calferinnovate.mediconnecta.Home.Fragments.PacientesFragment;
import com.calferinnovate.mediconnecta.Home.Fragments.ParteGeneralFragment;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Empleado;
import com.calferinnovate.mediconnecta.Interfaces.IOnBackPressed;
import com.calferinnovate.mediconnecta.Model.Unidades;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnBackPressedDispatcherOwner {

    private DrawerLayout drawerLayout;
    private ImageView fotoEmpleadoND;
    private TextView nombreEmpleadoND;
    private TextView unidadND;
    private ClaseGlobal claseGlobal;
    private Empleado empleado;
    private Unidades unidad;
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        llamaAClaseGlobal();
        rellenaDatosPersonalesNavigationDrawer(headerView);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        creaRetroceso();



        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

    }

    public void llamaAClaseGlobal(){
        claseGlobal = ClaseGlobal.getInstance();
        empleado = claseGlobal.getEmpleado();
        unidad = claseGlobal.getUnidades();
    }
    public void rellenaDatosPersonalesNavigationDrawer(View headerView){
        fotoEmpleadoND = headerView.findViewById(R.id.fotoEmpleadoND);
        nombreEmpleadoND = headerView.findViewById(R.id.nombreEmpleadoND);
        unidadND = headerView.findViewById(R.id.unidadND);

        Glide.with(getApplicationContext()).load(empleado.getFoto()).circleCrop().into(fotoEmpleadoND);

        nombreEmpleadoND.setText(empleado.getNombre()+" "+empleado.getApellidos());
        unidadND.setText(unidad.getNombreUnidad());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home_item_id){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }else if(item.getItemId() == R.id.residentes_item_id){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PacientesFragment()).addToBackStack(null).commit();
        }else if(item.getItemId() == R.id.parte_general_item_id){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ParteGeneralFragment()).addToBackStack(null).commit();
        }else if(item.getItemId() == R.id.normas_empresa_item_id){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NormasEmpresaFragment()).addToBackStack(null).commit();
        }else if(item.getItemId() == R.id.cierre_sesion_item_id){
            //Si dan a cerrar sesión mostramos el diálogo de salida
            dialogCerrarSesion();
        }else if(item.getItemId() == R.id.cambio_unidad_item_id){
            dialogCambioUnidad();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void creaRetroceso(){

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

    public void dialogCerrarSesion(){
        //Creamos un Dialog
        final Dialog dialog = new Dialog(this);

        //Asignamos al dialog el layout que hemos creado para cerrar sesión
        dialog.setContentView(R.layout.dialog_cerrar_sesion_y_cambio_unidad);

        //Obtenemos las referencias a los TextView
        TextView texto = dialog.findViewById(R.id.textViewCierreCambio);
        texto.setText("¿Quiere cerrar sesión?");
        TextView siSalir = dialog.findViewById(R.id.textViewSi);
        TextView noSalir = dialog.findViewById(R.id.textViewNo);

        //Click listener para el no
        noSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cerramos el dialogo
                dialog.dismiss();
            }
        });

        //CLick listener para si
        siSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cerramos el dialog y salimos
                dialog.dismiss();
                //finishAndRemoveTask();
                finishAffinity();
            }
        });

        //mostramos el dialogo de salida
        dialog.show();
    }

    public void dialogCambioUnidad(){
        //Creamos un Dialog
        final Dialog dialog = new Dialog(this);

        //Asignamos al dialog el layout que hemos creado para cerrar sesión
        dialog.setContentView(R.layout.dialog_cerrar_sesion_y_cambio_unidad);

        //Obtenemos las referencias a los TextView
        TextView texto = dialog.findViewById(R.id.textViewCierreCambio);
        texto.setText("¿Quiere cambiar de unidad?");
        TextView siSalir = dialog.findViewById(R.id.textViewSi);
        TextView noSalir = dialog.findViewById(R.id.textViewNo);


        //Click listener para el no
        noSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cerramos el dialogo
                dialog.dismiss();
            }
        });

        //CLick listener para si
        siSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cerramos el dialog y salimos
                dialog.dismiss();
                //finishAndRemoveTask();
                finish();
            }
        });

        //mostramos el dialogo de salida
        dialog.show();
    }
}