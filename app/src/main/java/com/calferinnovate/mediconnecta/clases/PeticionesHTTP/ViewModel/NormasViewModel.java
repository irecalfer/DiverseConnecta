package com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.calferinnovate.mediconnecta.Adaptadores.NormasAdapter;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Constantes;
import com.calferinnovate.mediconnecta.clases.Normas;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.PeticionesJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NormasViewModel extends ViewModel {

    private ClaseGlobal claseGlobal;
    private PeticionesJson peticionesJson;

    private MutableLiveData<ArrayList<Normas>> arrayListLiveData = new MutableLiveData<>();

    public NormasViewModel(){

    }

    public NormasViewModel(ViewModelArgs viewModelArgs){
        this.peticionesJson = viewModelArgs.getPeticionesJson();
        this.claseGlobal = viewModelArgs.getClaseGlobal();
    }

    public LiveData<ArrayList<Normas>> getArrayListLiveData(){
        return arrayListLiveData;
    }

    public void obtieneNormasEmpresa(){
        String url = Constantes.url_part+"normas.php";
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray jsonArray = response.getJSONArray("normas");
                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Normas norma = new Normas(jsonObject.optString("nombre_norma"),
                                jsonObject.optString("contenido"));
                        claseGlobal.getListaNormas().add(norma);
                    }
                    claseGlobal.setListaNormas(claseGlobal.getListaNormas());
                    ArrayList<Normas> listaNormas = claseGlobal.getListaNormas();
                    if(!listaNormas.isEmpty()){
                        arrayListLiveData.postValue(new ArrayList<>(listaNormas));
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
    }
}
