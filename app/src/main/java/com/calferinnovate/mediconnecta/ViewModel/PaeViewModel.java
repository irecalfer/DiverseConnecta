package com.calferinnovate.mediconnecta.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.ContactoFamiliares;
import com.calferinnovate.mediconnecta.Model.ControlSomatometrico;
import com.calferinnovate.mediconnecta.Model.Pae;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PaeViewModel extends ViewModel {

    private ClaseGlobal claseGlobal;
    private PeticionesJson peticionesJson;
    private MutableLiveData<ArrayList<Pae>> mutablePaeList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<ControlSomatometrico>> mutableControlSomatometricoList = new MutableLiveData<>();

    public PaeViewModel(){

    }

    public PaeViewModel(ViewModelArgs viewModelArgs){
        claseGlobal = viewModelArgs.getClaseGlobal();
        peticionesJson = viewModelArgs.getPeticionesJson();
    }

    public LiveData<ArrayList<Pae>> obtienePae(Alumnos alumno) {
        String url = Constantes.url_part + "pae.php?id_alumno=" + alumno.getIdAlumno();
        mutablePaeList.setValue(new ArrayList<>());

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Pae> paeArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("pae");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Pae nuevoPae = new Pae(jsonObject.optInt("id_pae"), jsonObject.optInt("curso_emision_inicio"),
                                jsonObject.optInt("curso_emision_fin"), jsonObject.optInt("fk_id_enfermero"),
                                jsonObject.optInt("fk_id_profesor"), jsonObject.optInt("fk_id_alumnos"),
                                jsonObject.optString("alergias"), jsonObject.optString("diagnostico_clinico"),
                                jsonObject.optString("fiebre"), jsonObject.optString("dieta_alimentacion"),
                                jsonObject.optString("protesis"), jsonObject.optString("ortesis"),
                                jsonObject.optString("gafas"), jsonObject.optString("audifonos"),
                                jsonObject.optString("otros"), jsonObject.optString("medicacion")
                                );
                        paeArrayList.add(nuevoPae);
                    }

                    if (!paeArrayList.isEmpty()) {
                        mutablePaeList.postValue(new ArrayList<>(paeArrayList));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        return mutablePaeList;
    }

    public LiveData<ArrayList<ControlSomatometrico>> obtieneControlSomatometrico(Pae pae) {
        String url = Constantes.url_part + "control_somatometrico.php?id_pae=" + pae.getIdPae();
        mutableControlSomatometricoList.setValue(new ArrayList<>());

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<ControlSomatometrico> controlSomatometricoArrayList= new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("control");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ControlSomatometrico nuevoControl = new ControlSomatometrico(jsonObject.optInt("id_control"),
                                jsonObject.optInt("frecuencia_cardiaca"),
                                jsonObject.optInt("saturacion_o2"), jsonObject.optInt("fk_trimestre"),
                                jsonObject.optInt("fk_id_pae"), jsonObject.optDouble("peso"), jsonObject.optDouble("imc"),
                                jsonObject.optDouble("percentil"), jsonObject.optDouble("temperatura"),
                                jsonObject.optString("talla"), jsonObject.optString("tension_arterial")
                        );
                        controlSomatometricoArrayList.add(nuevoControl);
                    }

                    if (!controlSomatometricoArrayList.isEmpty()) {
                        mutableControlSomatometricoList.postValue(new ArrayList<>(controlSomatometricoArrayList));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        return mutableControlSomatometricoList;
    }

}
