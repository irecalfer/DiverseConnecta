package com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Pacientes;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SharedPacientesViewModel extends ViewModel {

    public static final String TAG = "SharedPacientesViewModel";
    private MutableLiveData<ArrayList<Pacientes>> mutablePacientesList = new MutableLiveData<>();
    private MutableLiveData<Pacientes> mutablePaciente = new MutableLiveData<>();



    public LiveData<ArrayList<Pacientes>> getPacientesList(){

        if (mutablePacientesList.getValue() == null) {
            mutablePacientesList.setValue(ClaseGlobal.getInstance().getListaPacientes());
        }
        return mutablePacientesList;
    }

    public LiveData<Pacientes> getPaciente(){
        return mutablePaciente;
    }

    public void obtieneDatosPaciente(){

    }

    public void setPaciente(int position){
        Pacientes pacienteSeleccionado = mutablePacientesList.getValue().get(position);
        mutablePaciente.setValue(pacienteSeleccionado);
    }

}
