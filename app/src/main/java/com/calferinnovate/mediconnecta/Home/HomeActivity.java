package com.calferinnovate.mediconnecta.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.calferinnovate.mediconnecta.Home.Fragments.HomeFragment;
import com.calferinnovate.mediconnecta.Home.Fragments.PacientesFragment;
import com.calferinnovate.mediconnecta.Home.Fragments.ParteGeneralFragment;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.Backpressedlistener;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Empleado;
import com.calferinnovate.mediconnecta.clases.Unidades;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Backpressedlistener {

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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PacientesFragment()).commit();
        }else if(item.getItemId() == R.id.parte_general_item_id){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ParteGeneralFragment()).commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}