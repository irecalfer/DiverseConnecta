package com.calferinnovate.mediconnecta.ViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.ContactoFamiliares;
import com.calferinnovate.mediconnecta.Model.HistoriaClinica;
import com.calferinnovate.mediconnecta.Model.Informes;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.Model.Pautas;
import com.calferinnovate.mediconnecta.PeticionesHTTP.PeticionesJson;
import com.calferinnovate.mediconnecta.Model.Seguro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import android.util.Base64;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SharedPacientesViewModel extends ViewModel {

    public static final String TAG = "SharedPacientesViewModel";
    private final MutableLiveData<ArrayList<Pacientes>> mutablePacientesList = new MutableLiveData<>();
    private final MutableLiveData<Pacientes> mutablePaciente = new MutableLiveData<>();
    private final MutableLiveData<Seguro> mutableSeguro= new MutableLiveData<>();
    private final MutableLiveData<HistoriaClinica> mutableHistoriaClinica= new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Seguro>> mutableSeguroList = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ContactoFamiliares>> mutableFamiliaresList = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Informes>> mutableInformesList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Pautas>> mutablePautasList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> listaLugaresLiveData = new MutableLiveData<>();

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
            mutablePacientesList.postValue(ClaseGlobal.getInstance().getListaPacientes());
        }
        return mutablePacientesList;
    }

    public LiveData<Pacientes> getPaciente() {
        return mutablePaciente;
    }

    public void obtieneSeguroPacientes(Pacientes paciente) {
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
                                        mutableSeguroList.postValue(new ArrayList<>(seguros));
                                    }

                                    // Una vez que los seguros se hayan cargado, busca el seguro del paciente
                                    Seguro seguroDelPaciente = obtieneSeguroPacienteSeleccionado(paciente);
                                    mutableSeguro.postValue(seguroDelPaciente);
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

    private Seguro obtieneSeguroPacienteSeleccionado(Pacientes paciente){
        for (Seguro seguro: claseGlobal.getListaSeguros()) {
            if (seguro.getIdSeguro() == paciente.getFkIdSeguro()) {
                Seguro seguroDelPaciente = seguro;
                return seguroDelPaciente;
            }
        }
        return null; //MANEJAR CASO EN CASO DE QUE NO LO ENCUENTRE
    }

    public LiveData<Seguro> getSeguro() {
        return mutableSeguro;
    }

    public LiveData<ArrayList<ContactoFamiliares>> obtieneContactoFamiliares(Pacientes paciente){
            String url = Constantes.url_part + "familiares.php?cip_sns=" + paciente.getCipSns();
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //Si la lista de contactos está llena para otro familiar la vaciamos para
                                //llenarla con los datos de los familiares del nuevo paciente.
                                if (!claseGlobal.getListaContactoFamiliares().isEmpty()) {
                                    claseGlobal.getListaContactoFamiliares().clear();
                                }
                                JSONArray jsonArray = response.getJSONArray("familiares_contacto");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    ContactoFamiliares nuevoFamiliar = new ContactoFamiliares(jsonObject.optString("dni_familiar"),
                                            jsonObject.optString("nombre"), jsonObject.optString("apellidos"),
                                            jsonObject.optInt("telefono_contacto"), jsonObject.optInt("telefono_contacto_2"));
                                    claseGlobal.getListaContactoFamiliares().add(nuevoFamiliar);
                                }
                                claseGlobal.setListaContactoFamiliares(claseGlobal.getListaContactoFamiliares());
                                ArrayList<ContactoFamiliares> contactoFamiliares = claseGlobal.getListaContactoFamiliares();
                                if (!contactoFamiliares.isEmpty()) {
                                    mutableFamiliaresList.postValue(new ArrayList<>(contactoFamiliares));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                }
            });
        return mutableFamiliaresList;
    }

    public LiveData<ArrayList<ContactoFamiliares>> getListaMutableFamiliares(Pacientes paciente){
        return obtieneContactoFamiliares(paciente);
    }

    public LiveData<ArrayList<Informes>> getListaMutableInformes(Pacientes paciente){
        return obtieneInformesPaciente(paciente);
    }

    public LiveData<ArrayList<Informes>> obtieneInformesPaciente(Pacientes paciente){
            String url = Constantes.url_part+"informes.php?fk_num_historia_clinica="+paciente.getFkNumHistoriaClinica();
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                //Si la lista de informes está llena para otro paciente la vaciamos para
                                //llenarla con los informes del nuevo paciente.
                                if (!claseGlobal.getListaInformes().isEmpty()) {
                                    claseGlobal.getListaInformes().clear();
                                }
                                // Verificar que claseGlobal y la lista de informes no sean nulos
                                if (claseGlobal != null && claseGlobal.getListaInformes() != null) {
                                    JSONArray jsonArray = response.getJSONArray("informes");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String base64String = jsonObject.optString("PDF");
                                        byte[] pdfBytes = Base64.decode(base64String, Base64.DEFAULT);
                                        Informes nuevoInforme = new Informes(jsonObject.optInt("fk_num_historia_clinica"),
                                                jsonObject.optString("tipo_informe"), jsonObject.optString("fecha"),
                                                jsonObject.optString("centro"), jsonObject.optString("responsable"),
                                                jsonObject.optString("servicio_unidad_dispositivo"), jsonObject.optString("servicio_de_salud"),
                                                pdfBytes);
                                        claseGlobal.getListaInformes().add(nuevoInforme);
                                    }
                                    claseGlobal.setListaInformes(claseGlobal.getListaInformes());
                                    // Actualizar el LiveData directamente
                                    if (!claseGlobal.getListaInformes().isEmpty()) {
                                        mutableInformesList.postValue(new ArrayList<>(claseGlobal.getListaInformes()));
                                    }
                                }
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                }
            });
        return mutableInformesList;
    }

    public void setPaciente(int position) {
        Pacientes pacienteSeleccionado = mutablePacientesList.getValue().get(position);
        mutablePaciente.postValue(pacienteSeleccionado);
    }

    public LiveData<HistoriaClinica> obtieneHistoriaClinica(Pacientes paciente){
        String url = Constantes.url_part+"historia_clinica.php?cip_sns="+paciente.getCipSns();
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray jsonArray = response.getJSONArray("historia_clinica");
                            for(int i =0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                HistoriaClinica nuevaHistoriaClinica = new HistoriaClinica(jsonObject.optString("datos_salud"),
                                        jsonObject.optString("tratamiento"));
                                claseGlobal.setHistoriaClinica(nuevaHistoriaClinica);
                            }
                            mutableHistoriaClinica.postValue(claseGlobal.getHistoriaClinica());
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            }
        });
        return mutableHistoriaClinica;
    }

    public LiveData<ArrayList<Pautas>> obtienePautasPaciente(Pacientes paciente){
        mutablePautasList.postValue(new ArrayList<>());  // Reiniciar el LiveData
        String url = Constantes.url_part+"pautas.php?cip_sns="+paciente.getCipSns();
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (!claseGlobal.getListaPautas().isEmpty()) {
                                claseGlobal.getListaPautas().clear();
                            }
                            JSONArray jsonArray = response.getJSONArray("pautas");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Pautas nuevaPauta = new Pautas(jsonObject.optString("pauta"),
                                        jsonObject.optString("observaciones"), jsonObject.optString("mañana"),
                                        jsonObject.optString("tarde"), jsonObject.optString("noche"));
                                claseGlobal.getListaPautas().add(nuevaPauta);
                            }
                            claseGlobal.setListaPautas(claseGlobal.getListaPautas());
                            // Actualizar el LiveData directamente
                            if (!claseGlobal.getListaPautas().isEmpty()) {
                                mutablePautasList.postValue(new ArrayList<>(claseGlobal.getListaPautas()));
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            }
        });
        return mutablePautasList;
    }

    public LiveData<ArrayList<Pautas>> getListaMutablePautas(Pacientes paciente){
        return obtienePautasPaciente(paciente);
    }

    public LiveData<ArrayList<String>> getListaLugaresLiveData() {
        return obtieneLosLugaresDeCaidas();
    }

    public LiveData<ArrayList<String>> obtieneLosLugaresDeCaidas(){
        String url = Constantes.url_part + "caidas_enum.php";
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray lugarCaida = response.getJSONArray("lugar_caida");
                            Log.d("Lugares", "Ha entrado en on response");
                            // Itera sobre los valores ENUM
                            for (int i = 0; i < lugarCaida.length(); i++) {
                                String enumLugar = lugarCaida.getString(i);
                                //Añadimos el valor a nuestro arrayList
                                claseGlobal.getListaLugares().add(enumLugar);
                            }
                            claseGlobal.setListaLugares(claseGlobal.getListaLugares());
                            // Actualiza el LiveData con la nueva lista
                            if (!claseGlobal.getListaLugares().isEmpty()) {
                                listaLugaresLiveData.postValue(new ArrayList<>(claseGlobal.getListaLugares()));
                            }
                        } catch (JSONException e) {
                            Log.d("Lugares", "Ha entrado en el catch de on response");
                            Log.d("LugaresCatch", e.toString());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Lugares", "Ha entrado en on error response");
                        error.printStackTrace();
                    }
                });
            }
        });
        return listaLugaresLiveData;
    }


}
