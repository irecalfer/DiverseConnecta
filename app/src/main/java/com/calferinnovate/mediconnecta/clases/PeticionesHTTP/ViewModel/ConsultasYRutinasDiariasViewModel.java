package com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Constantes;
import com.calferinnovate.mediconnecta.clases.PacientesAgrupadosRutinas;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.PeticionesJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConsultasYRutinasDiariasViewModel extends ViewModel {
    private MutableLiveData<ArrayList<PacientesAgrupadosRutinas>> listaProgramacionLiveData = new MutableLiveData<>();
    private PeticionesJson peticionesJson;
    private ClaseGlobal claseGlobal;

    // Constructor
    public ConsultasYRutinasDiariasViewModel() {
    }

    public ConsultasYRutinasDiariasViewModel(ViewModelArgs viewModelArgs) {
        this.peticionesJson = viewModelArgs.getPeticionesJson();
        this.claseGlobal = viewModelArgs.getClaseGlobal();
    }

    public LiveData<ArrayList<PacientesAgrupadosRutinas>> getListaProgramacionLiveData(String fechaRutina, String nombreUnidad, String tipoRutina) {
        if (listaProgramacionLiveData.getValue() == null) {obtieneDatosRutinasDiaPacientes(fechaRutina, nombreUnidad, tipoRutina);
        }
        return listaProgramacionLiveData;
    }

    public void obtieneDatosRutinasDiaPacientes(String fechaRutina, String nombreUnidad, String tipoRutina) {
        String url = Constantes.url_part + "programacionRutinas.php?fecha_rutina=" + fechaRutina + "&nombre=" + nombreUnidad + "&diario=" + tipoRutina;

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("programacion");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        PacientesAgrupadosRutinas pacientesAgrupadosRutinas = new PacientesAgrupadosRutinas(jsonObject.optString("fk_cip_sns"), jsonObject.optInt("fk_id_rutina"), jsonObject.optString("hora_rutina"));
                        claseGlobal.getListaProgramacion().add(pacientesAgrupadosRutinas);
                    }
                    claseGlobal.setListaProgramacion(claseGlobal.getListaProgramacion());
                    listaProgramacionLiveData.setValue(claseGlobal.getListaProgramacion());
                } catch (JSONException e) {
                    // Maneja errores, si es necesario
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}
