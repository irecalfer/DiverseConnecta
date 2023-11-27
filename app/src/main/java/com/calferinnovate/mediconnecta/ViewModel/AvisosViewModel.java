package com.calferinnovate.mediconnecta.ViewModel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.calferinnovate.mediconnecta.Model.Avisos;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * ViewModel encargado de gestionar los datos relativos a los Avisos diarios.
 * Usa variables MutableLiveData para modificar el valor contenido en la variable después de la creación
 * y utiliza LiveData para transmitir los datos hacia la UI, posteriormente proporciona actualizaciones
 * a los observadores del fragmento.
 */
public class AvisosViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<String>> arrayListAvisosMutableLiveData = new MutableLiveData<>();
    private PeticionesJson peticionesJson;

    /**
     * Constructor por defecto.
     */
    public AvisosViewModel(){

    }

    /**
     * Constructor que recibe una instancia de ViewModelArgsJson para proporcionar PeticionesJson.
     *
     * @param viewModelArgsJson Instancia de ViewModelArgsJson que proporciona PeticionesJson.
     */
    public AvisosViewModel(ViewModelArgsJson viewModelArgsJson){
        peticionesJson = viewModelArgsJson.getPeticionesJson();
    }

    /**
     * Método que realiza una solicitud al servudor a través de PeticionesJson para obtener la lista
     * de avisos de una fecha dada, encapsula el contenido del arraylist en arrayListAvisosMutableLiveData
     * y devuelve el LiveData.
     * @param fecha Fecha seleccionada en el Calendar View.
     * @return LiveData con el contenido de los avisos de dicha fecha.
     */
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
                error.printStackTrace();
            }
        });
        return arrayListAvisosMutableLiveData;
    }
}
