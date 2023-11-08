package com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Constantes;
import com.calferinnovate.mediconnecta.clases.ContactoFamiliares;
import com.calferinnovate.mediconnecta.clases.Pacientes;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.PeticionesJson;
import com.calferinnovate.mediconnecta.clases.Seguro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SharedPacientesViewModel extends ViewModel {

    public static final String TAG = "SharedPacientesViewModel";
    private final MutableLiveData<ArrayList<Pacientes>> mutablePacientesList = new MutableLiveData<>();
    private final MutableLiveData<Pacientes> mutablePaciente = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Seguro>> mutableSeguroList = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ContactoFamiliares>> mutableFamiliaresList = new MutableLiveData<>();
    private PeticionesJson peticionesJson;
    private ClaseGlobal claseGlobal;
    private Context context;

    public SharedPacientesViewModel(){

    }

    public SharedPacientesViewModel(ViewModelArgs viewModelArgs) {
        this.peticionesJson = viewModelArgs.getPeticionesJson();
        this.claseGlobal = viewModelArgs.getClaseGlobal();
    }

    public LiveData<ArrayList<Pacientes>> getPacientesList() {

        if (mutablePacientesList.getValue() == null) {
            mutablePacientesList.setValue(ClaseGlobal.getInstance().getListaPacientes());
        }
        return mutablePacientesList;
    }

    public LiveData<Pacientes> getPaciente() {
        return mutablePaciente;
    }

    public void obtieneSeguroPacientes() {
        String url = Constantes.url_part+"seguro.php";
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("seguro");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Seguro nuevoSeguro = new Seguro(jsonObject.optInt("id_seguro"), jsonObject.optInt("telefono"),
                                            jsonObject.optString("nombre"));
                                    claseGlobal.getListaSeguros().add(nuevoSeguro);
                                }
                                claseGlobal.setListaSeguros(claseGlobal.getListaSeguros());
                                ArrayList<Seguro> seguros = claseGlobal.getListaSeguros();
                                if (!seguros.isEmpty()) {
                                    mutableSeguroList.setValue(new ArrayList<>(seguros));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void obtieneContactoFamiliares(Pacientes paciente){
        String url = Constantes.url_part+"familiares.php?cip_sns"+paciente.getCipSns();
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            //Si la lista de contactos está llena para otro familiar la vaciamos para
                            //llenarla con los datos de los familiares del nuevo paciente.
                            if(!claseGlobal.getListaContactoFamiliares().isEmpty()){
                                claseGlobal.getListaContactoFamiliares().clear();
                            }
                            JSONArray jsonArray = response.getJSONArray("familiares_contacto");
                            for(int i =0; i< jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ContactoFamiliares nuevoFamiliar = new ContactoFamiliares(jsonObject.optString("dni_familiar"),
                                        jsonObject.optString("nombre"), jsonObject.optString("apellidos"),
                                        jsonObject.optInt("telefono_contacto"), jsonObject.optInt("telefono_contacto_2"));
                                claseGlobal.getListaContactoFamiliares().add(nuevoFamiliar);
                            }
                            claseGlobal.setListaContactoFamiliares(claseGlobal.getListaContactoFamiliares());
                            ArrayList<ContactoFamiliares> contactoFamiliares = claseGlobal.getListaContactoFamiliares();
                            if (!contactoFamiliares.isEmpty()) {
                                mutableFamiliaresList.setValue(new ArrayList<>(contactoFamiliares));
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            }
        });
    }

    public void setPaciente(int position) {
        Pacientes pacienteSeleccionado = mutablePacientesList.getValue().get(position);
        mutablePaciente.setValue(pacienteSeleccionado);
    }

}
