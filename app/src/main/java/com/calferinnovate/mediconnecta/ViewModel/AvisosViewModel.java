package com.calferinnovate.mediconnecta.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.calferinnovate.mediconnecta.Model.Avisos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AvisosViewModel extends ViewModel {

    private MutableLiveData<ArrayList<String>> arrayListAvisosMutableLiveData = new MutableLiveData<>();
    private ClaseGlobal claseGlobal;
    private PeticionesJson peticionesJson;
    private ArrayList<String> contenidoAvisosArrayList = new ArrayList<>();

    public AvisosViewModel(){

    }

    public AvisosViewModel(ViewModelArgs viewModelArgs){
        claseGlobal = viewModelArgs.getClaseGlobal();
        peticionesJson = viewModelArgs.getPeticionesJson();
    }

    public LiveData<ArrayList<String>> obtieneAvisosFecha(String fecha){
        String url = Constantes.url_part+"avisos.php?fecha_aviso="+fecha;
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    ArrayList<String> contenidoArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("avisos");
                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Avisos nuevoAvisos = new Avisos(jsonObject.optInt("num_aviso"), jsonObject.optString(("fecha_aviso")), jsonObject.optString("contenido"));
                        contenidoArrayList.add(nuevoAvisos.getContenido());
                    }
                    if(!contenidoArrayList.isEmpty()){
                        arrayListAvisosMutableLiveData.postValue(new ArrayList<>(contenidoArrayList));
                    }
                }catch(JSONException jsonException){
                    throw new RuntimeException(jsonException);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                error.toString();
                Log.d("error", error.toString());
            }
        });
        return arrayListAvisosMutableLiveData;
    }
}
