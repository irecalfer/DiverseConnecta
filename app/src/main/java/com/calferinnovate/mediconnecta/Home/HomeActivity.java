package com.calferinnovate.mediconnecta.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Empleado;

public class HomeActivity extends AppCompatActivity {

    Empleado empleado;
    TextView empeladoNombre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        empeladoNombre = (TextView) findViewById(R.id.prueba);

        llamaAClaseGlobal();
        pintaNombreEmpleado(empleado);

    }

    public Empleado llamaAClaseGlobal(){
        empleado = ((ClaseGlobal) getApplicationContext()).empleado;
        return empleado;
    }

    public void pintaNombreEmpleado(Empleado e){
        empeladoNombre.setText(empleado.getNombre().toString());
    }

}