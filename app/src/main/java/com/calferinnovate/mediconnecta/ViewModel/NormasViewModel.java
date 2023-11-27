package com.calferinnovate.mediconnecta.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.Normas;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * ViewModel encargado de gestionar las normas de la empresa..
 * Usa variables MutableLiveData para modificar el valor contenido en la variable después de la creación
 * y utiliza LiveData para transmitir los datos hacia la UI, posteriormente proporciona actualizaciones
 * a los observadores del fragmento.
 */

public class NormasViewModel extends ViewModel {

    private PeticionesJson peticionesJson;

    private final MutableLiveData<ArrayList<Normas>> arrayListLiveDataNormas = new MutableLiveData<>();

    /**
     * Constructor por defecto.
     */
    public NormasViewModel() {

    }

    /**
     * Constructor que recibe una instancia de ViewModelArgsJson para proporcionar PeticionesJson.
     *
     * @param viewModelArgsJson Instancia de ViewModelArgsJson que proporciona PeticionesJson.
     */
    public NormasViewModel(ViewModelArgsJson viewModelArgsJson) {
        this.peticionesJson = viewModelArgsJson.getPeticionesJson();
    }


    /**
     * Método que realiza una solicitud al servudor a través de PeticionesJson para obtener la lista
     * de normas de la empresa, encapsula el contenido del arraylist en arrayListLiveDataNormas
     * y devuelve el LiveData.
     *
     * @return LiveData con la lista de normas.
     */
    public LiveData<ArrayList<Normas>> obtieneNormasEmpresa() {
        String url = Constantes.url_part + "normas.php";
        arrayListLiveDataNormas.postValue(new ArrayList<>());
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Normas> normasArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("normas");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Normas norma = new Normas(jsonObject.optString("nombre_norma"),
                                jsonObject.optString("contenido"));
                        normasArrayList.add(norma);
                    }
                    if (!normasArrayList.isEmpty()) {
                        arrayListLiveDataNormas.postValue(new ArrayList<>(normasArrayList));
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
        return arrayListLiveDataNormas;
    }
}
