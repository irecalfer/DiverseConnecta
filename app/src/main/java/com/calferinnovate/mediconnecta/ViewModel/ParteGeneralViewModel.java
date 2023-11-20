package com.calferinnovate.mediconnecta.ViewModel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.calferinnovate.mediconnecta.Model.Caidas;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.Parte;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParteGeneralViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Parte>>  arrayListParteMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Caidas>> arrayListCaidasMutableLiveData = new MutableLiveData<>();

    private PeticionesJson peticionesJson;
    private ClaseGlobal claseGlobal;
    private Context context;

    public ParteGeneralViewModel(){

    }

    public ParteGeneralViewModel(ViewModelArgs viewModelArgs) {
        this.peticionesJson = viewModelArgs.getPeticionesJson();
        this.claseGlobal = viewModelArgs.getClaseGlobal();
    }

    public LiveData<ArrayList<Parte>> getListaPartes(String fechaInicio, String fechaFin){
        return obtieneListaPartesFecha(fechaInicio,fechaFin);
    }

    public LiveData<ArrayList<Parte>> obtieneListaPartesFecha(String fechaInicio, String fechaFin){
        String url = Constantes.url_part+"partes.php?fechaInicio="+fechaInicio+"&fechaFin="+fechaFin;
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    ArrayList<Parte> parteArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("parte");
                    Log.d("fallo", "ha entradao en el try");
                    for (int i = 0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Parte parte = new Parte(jsonObject.optString("nombrePaciente"), jsonObject.optString("descripcion"),
                                jsonObject.optString("empleado"), jsonObject.optString("fecha"));
                       parteArrayList.add(parte);
                    }
                    if(!parteArrayList.isEmpty()){
                        arrayListParteMutableLiveData.postValue(parteArrayList);
                    }
                }catch(JSONException e){
                    Log.d("fallo", e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
               error.printStackTrace();
               Log.d("fallo", error.toString());
            }
        });
        return arrayListParteMutableLiveData;
    }

    public LiveData<ArrayList<Caidas>> getListaCaidas(String fechaInicio, String fechaFin){
        return obtieneCaidasPeriodo(fechaInicio, fechaFin);
    }

    public LiveData<ArrayList<Caidas>> obtieneCaidasPeriodo(String fechaInicio, String fechaFin){
        String url = Constantes.url_part+"parte_caidas.php?fechaInicio="+fechaInicio+"&fechaFin="+fechaFin;
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    ArrayList<Caidas> caidasArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("parte_caidas");
                    for(int i =0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Caidas caida = new Caidas(jsonObject.optString("fecha_y_hora"), jsonObject.optString("nombrePaciente"),
                                jsonObject.optString("lugar_caida"), jsonObject.optString("factores_de_riesgo"),
                                jsonObject.optString("causas"), jsonObject.optString("circunstancias"),
                                jsonObject.optString("consecuencias"), jsonObject.optString("unidad"),
                                jsonObject.optString("caida_presenciada"), jsonObject.optString("avisado_a_familiares"),
                                jsonObject.optString("observaciones"), jsonObject.optString("empleado"));
                        caidasArrayList.add(caida);
                    }
                    if(!caidasArrayList.isEmpty()){
                        arrayListCaidasMutableLiveData.postValue(new ArrayList<>(caidasArrayList));
                    }
                }catch(JSONException e){
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
