package com.calferinnovate.mediconnecta.View.Sesion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;

/**
 * Activity que contiene un fragment container donde se mostrarán los fragmentos de inicio de sesión
 * y Selección de unidad.
 * @author Irene Caldelas Fernández
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

}

