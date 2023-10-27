package com.calferinnovate.mediconnecta.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Empleado;
import com.calferinnovate.mediconnecta.clases.Unidades;

public class HomeActivity extends AppCompatActivity {

    Empleado empleado;
    Unidades unidad;
    TextView empeladoNombre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        empeladoNombre = (TextView) findViewById(R.id.prueba);

        llamaAClaseGlobal();
        pintaNombreEmpleado(unidad);

    }

    public Unidades llamaAClaseGlobal(){
        unidad = ((ClaseGlobal) getApplicationContext()).unidades;
        return unidad;
    }

    public void pintaNombreEmpleado(Unidades e){
        empeladoNombre.setText(unidad.getNombreUnidad());
    }

}