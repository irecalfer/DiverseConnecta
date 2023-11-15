package com.calferinnovate.mediconnecta.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.PacientesAgrupadosRutinas;
import com.calferinnovate.mediconnecta.PeticionesHTTP.PeticionesJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConsultasYRutinasDiariasViewModel extends ViewModel {
    // instancia de MutableLiveData, que es un contenedor de datos observable que almacena una lista de objetos
    private MutableLiveData<ArrayList<PacientesAgrupadosRutinas>> listaProgramacionLiveData = new MutableLiveData<>();
    private PeticionesJson peticionesJson;
    private ClaseGlobal claseGlobal;

    // Constructor
    public ConsultasYRutinasDiariasViewModel() {
    }

    //Constructor utilizado para obtener instancias de peticionesJson y claseGlobal necesarias
    // para realizar solicitudes HTTP y gestionar datos globales.
    public ConsultasYRutinasDiariasViewModel(ViewModelArgs viewModelArgs) {
        this.peticionesJson = viewModelArgs.getPeticionesJson();
        this.claseGlobal = viewModelArgs.getClaseGlobal();
    }

    //Este método se utiliza para obtener el LiveData de la lista de programación de rutinas. Comprueba si el LiveData ya tiene datos (diferentes de nulo), y si no, realiza una solicitud HTTP para obtener los datos de rutinas
    // utilizando el método obtieneDatosRutinasDiaPacientes(). Luego, devuelve el LiveData de la lista de programación.
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
                    // actualiza el LiveData con la nueva lista de programación, notificando a los observadores.
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
