package com.calferinnovate.mediconnecta.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.calferinnovate.mediconnecta.Model.Caidas;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.Parte;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * ViewModel encargado de gestionar los partes y partes de caída pertenecientes a un rango de fechas.
 * Usa variables MutableLiveData para modificar el valor contenido en la variable después de la creación
 * y utiliza LiveData para transmitir los datos hacia la UI, posteriormente proporciona actualizaciones
 * a los observadores del fragmento.
 */

public class ParteGeneralViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Parte>> arrayListParteMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Caidas>> arrayListCaidasMutableLiveData = new MutableLiveData<>();

    private PeticionesJson peticionesJson;

    public ParteGeneralViewModel() {

    }

    /**
     * Constructor que recibe una instancia de ViewModelArgsJson para proporcionar PeticionesJson.
     *
     * @param viewModelArgsJson Instancia de ViewModelArgsJson que proporciona PeticionesJson.
     */
    public ParteGeneralViewModel(ViewModelArgsJson viewModelArgsJson) {
        this.peticionesJson = viewModelArgsJson.getPeticionesJson();
    }

    /**
     * Método que realiza una solicitud al servidor a través de PeticionesJson para obtener la lista
     * de partes de un rango de fecha dado, encapsula el contenido del arraylist en arrayListParteMutableLiveData
     * y devuelve el LiveData.
     *
     * @param fechaInicio Fecha de inicio seleccionada
     * @param fechaFin    Fecha de finalización seleccionada
     * @return LivevData que contiene la lista de los partes de dicho rango de fechas.
     */
    public LiveData<ArrayList<Parte>> obtieneListaPartesFecha(String fechaInicio, String fechaFin) {
        String url = Constantes.url_part + "partes.php?fechaInicio=" + fechaInicio + "&fechaFin=" + fechaFin;
        arrayListParteMutableLiveData.setValue(new ArrayList<>());
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Parte> parteArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("parte");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Parte parte = new Parte(jsonObject.optString("nombrePaciente"), jsonObject.optString("descripcion"),
                                jsonObject.optString("empleado"), jsonObject.optString("fecha"));
                        parteArrayList.add(parte);
                    }
                    if (!parteArrayList.isEmpty()) {
                        arrayListParteMutableLiveData.postValue(parteArrayList);
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
        return arrayListParteMutableLiveData;
    }


    /**
     * Método que realiza una solicitud al servidor a través de PeticionesJson para obtener la lista
     * de partes de caída de un rango de fecha dado, encapsula el contenido del arraylist en arrayListCaidasMutableLiveData
     * y devuelve el LiveData.
     *
     * @param fechaInicio Fecha de inicio seleccionada
     * @param fechaFin    Fecha de finalización seleccionada
     * @return LivevData que contiene la lista de los partes de caídas de dicho rango de fechas.
     */
    public LiveData<ArrayList<Caidas>> obtieneCaidasPeriodo(String fechaInicio, String fechaFin) {
        String url = Constantes.url_part + "parte_caidas.php?fechaInicio=" + fechaInicio + "&fechaFin=" + fechaFin;
        arrayListCaidasMutableLiveData.setValue(new ArrayList<>());
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Caidas> caidasArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("parte_caidas");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Caidas caida = new Caidas(jsonObject.optString("fecha_y_hora"), jsonObject.optString("nombrePaciente"),
                                jsonObject.optString("lugar_caida"), jsonObject.optString("factores_de_riesgo"),
                                jsonObject.optString("causas"), jsonObject.optString("circunstancias"),
                                jsonObject.optString("consecuencias"), jsonObject.optString("unidad"),
                                jsonObject.optString("caida_presenciada"), jsonObject.optString("avisado_a_familiares"),
                                jsonObject.optString("observaciones"), jsonObject.optString("empleado"));
                        caidasArrayList.add(caida);
                    }
                    if (!caidasArrayList.isEmpty()) {
                        arrayListCaidasMutableLiveData.postValue(new ArrayList<>(caidasArrayList));
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
        return arrayListCaidasMutableLiveData;
    }
}
