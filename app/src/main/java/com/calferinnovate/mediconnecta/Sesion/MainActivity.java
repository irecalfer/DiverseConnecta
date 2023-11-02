package com.calferinnovate.mediconnecta.Sesion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Empleado;


public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa ClaseGlobal y almacena los datos de los pacientes, por ejemplo:
        ClaseGlobal claseGlobal = (ClaseGlobal) getApplicationContext();


        //FragmentManager fmSesion = getSupportFragmentManager();
        //fmSesion.beginTransaction().replace(R.id.HomeView, new SesionFragment()).commit();


    }

}

