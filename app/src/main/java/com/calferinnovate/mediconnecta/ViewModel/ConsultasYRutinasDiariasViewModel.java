package com.calferinnovate.mediconnecta.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.PacientesAgrupadosRutinas;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * ViewModel encargado de gestionar las rutinas diarias de los pacientes.
 * Usa variables MutableLiveData para modificar el valor contenido en la variable después de la creación
 * y utiliza LiveData para transmitir los datos hacia la UI, posteriormente proporciona actualizaciones
 * a los observadores del fragmento.
 */

public class ConsultasYRutinasDiariasViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<PacientesAgrupadosRutinas>> listaProgramacionLiveData = new MutableLiveData<>();
    private PeticionesJson peticionesJson;

    public ConsultasYRutinasDiariasViewModel() {
    }

    /**
     * Constructor utilizado para obtener instancia de peticionesJson necesaria para realizar solicitudes HTTP
     *
     * @param viewModelArgs Instancia de ViewModelArgsJson que proporciona PeticionesJson.
     */
    public ConsultasYRutinasDiariasViewModel(ViewModelArgsJson viewModelArgs) {
        this.peticionesJson = viewModelArgs.getPeticionesJson();
    }

    /**
     * Método que realiza una solicitud al servudor a través de PeticionesJson para obtener la lista
     * de rutinas de lode pacientes en una fecha dada, encapsula el contenido del arraylist en listaProgramacionLiveData
     * y devuelve el LiveData.
     *
     * @param fechaRutina  Fecha seleccionada
     * @param nombreUnidad Nombre de la unidad a la que pertenecen los pacientes
     * @param tipoRutina   Tipo de Rutina de la que se desea obtener la lista de pacientes.
     * @return LiveData que contiene la lista de pacientes asociados a un tipo de rutina en una fecha dada.
     */
    public LiveData<ArrayList<PacientesAgrupadosRutinas>> obtieneDatosRutinasDiaPacientes(String fechaRutina, String nombreUnidad, String tipoRutina) {
        String url = Constantes.url_part + "programacionRutinas.php?fecha_rutina=" + fechaRutina + "&nombre=" + nombreUnidad + "&diario=" + tipoRutina;
        listaProgramacionLiveData.postValue(new ArrayList<>());
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<PacientesAgrupadosRutinas> pacientesAgrupadosRutinasArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("programacion");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        PacientesAgrupadosRutinas pacientesAgrupadosRutinas = new PacientesAgrupadosRutinas(jsonObject.optString("fk_cip_sns"), jsonObject.optInt("fk_id_rutina"), jsonObject.optString("hora_rutina"));
                        pacientesAgrupadosRutinasArrayList.add(pacientesAgrupadosRutinas);
                    }
                    if (!pacientesAgrupadosRutinasArrayList.isEmpty()) {
                        // actualiza el LiveData con la nueva lista de programación, notificando a los observadores.
                        listaProgramacionLiveData.postValue(pacientesAgrupadosRutinasArrayList);
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
        return listaProgramacionLiveData;
    }
}
