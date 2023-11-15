package com.calferinnovate.mediconnecta.ViewModel;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.calferinnovate.mediconnecta.Model.Area;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.Empleado;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.Model.Unidades;
import com.calferinnovate.mediconnecta.PeticionesHTTP.PeticionesJson;
import com.calferinnovate.mediconnecta.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SeleccionUnidadViewModel extends ViewModel {
    private PeticionesJson peticionesJson;
    private ClaseGlobal claseGlobal;
    private ArrayList<String> listaAreas = new ArrayList<>();
    private ArrayList<String> listaUnidadesNombre = new ArrayList<>();
    private ArrayList<Unidades> listaUnidades = new ArrayList<>();
    private MutableLiveData<ArrayList<String>> arrayListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> arrayListMutableLiveDataUnidades = new MutableLiveData<>();
    private MutableLiveData<Boolean> pacientesObtenidos = new MutableLiveData<>();

    public SeleccionUnidadViewModel(){

    }

    public SeleccionUnidadViewModel(ViewModelArgs viewModelArgs){
        this.peticionesJson = viewModelArgs.getPeticionesJson();
        this.claseGlobal = viewModelArgs.getClaseGlobal();
    }

    // Declarar LiveData para los datos necesarios en la interfaz de usuario (por ejemplo, áreas y unidades)
    // También, declarar cualquier otro LiveData necesario para la interfaz de usuario.

    // Método para obtener datos de áreas
    public LiveData<ArrayList<String>> obtenerDatosAreas() {
        // Lógica para obtener datos de áreas desde el repositorio o la fuente de datos
        // Devolver un LiveData que la interfaz de usuario puede observar.
        String urlArea = Constantes.url_part + "areas.php";
        peticionesJson.getJsonObjectRequest(urlArea, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(!claseGlobal.getListaAreas().isEmpty()){
                        claseGlobal.getListaAreas().clear();
                    }
                    Log.d("ErrorSeleccion", "Entra en try");
                    JSONArray jsonArray = response.getJSONArray("datos_area");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Log.d("ErrorSeleccion", "Entra en el for");
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Area nuevaArea = new Area(jsonObject.optInt("id_area"), jsonObject.optString("nombre_area"));
                        claseGlobal.getListaAreas().add(nuevaArea);
                        Log.d("ErrorSeleccion", nuevaArea.getNombre());
                        listaAreas.add(nuevaArea.getNombre());
                    }
                    Log.d("ErrorSeleccion", "finaliza for");
                    claseGlobal.setListaAreas(claseGlobal.getListaAreas());
                    if(!listaAreas.isEmpty()){
                        Log.d("ErrorSeleccion", "Entra en el empty");
                        arrayListMutableLiveData.postValue(new ArrayList<>(listaAreas));
                    }
                } catch (JSONException e) {
                    Log.d("ErrorSeleccion", e.toString());
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ErrorSeleccion", error.toString());
            }
        });
        return arrayListMutableLiveData;
    }

    // Método para obtener datos de unidades basados en el área seleccionada
    public LiveData<ArrayList<String>> obtenerDatosUnidades(String areaSeleccionada) {
        // Lógica para obtener datos de unidades basados en el área seleccionada desde el repositorio o la fuente de datos
        // Devolver un LiveData que la interfaz de usuario puede observar.
        String urlUnidades = Constantes.url_part + "unidades.php?nombre_area=" + areaSeleccionada;
        peticionesJson.getJsonObjectRequest(urlUnidades, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(!claseGlobal.getListaUnidades().isEmpty() || !listaUnidadesNombre.isEmpty()){
                        claseGlobal.getListaUnidades().clear();
                        listaUnidadesNombre.clear();
                    }

                    JSONArray jsonArray = response.getJSONArray("datos_unidades");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Unidades nuevaUnidad = new Unidades(jsonObject.optInt("id_unidad"), jsonObject.optInt("fk_id_area"),
                                jsonObject.optString("nombre"));
                        claseGlobal.getListaUnidades().add(nuevaUnidad);
                        listaUnidadesNombre.add(nuevaUnidad.getNombreUnidad());
                    }
                    claseGlobal.setListaUnidades(claseGlobal.getListaUnidades());
                    if(!listaUnidadesNombre.isEmpty()){
                        arrayListMutableLiveDataUnidades.postValue(new ArrayList<>(listaUnidadesNombre));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        return arrayListMutableLiveDataUnidades;
    }

    // Método para completar los datos del empleado
    public LiveData<Boolean> obtieneDatosPacientes(String nombreUnidad) {
        //Volvemos a asignar unidad, ya que hemos cambiado el valor que unidades en la clase global
        //De tal manera que podamos utilizar la instancia creada para poder acceder a su nombre y
        //de igual manera poder acceder a los pacientes de dicha unidad.
        //Unidades unidadActual = claseGlobal.getUnidades();
        String url = Constantes.url_part + "pacientes.php?nombre=" + nombreUnidad;
        //String url = Constantes.url_part + "pacientes.php?nombre=" + claseGlobal.getUnidades().getNombreUnidad();
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(!claseGlobal.getListaPacientes().isEmpty()){
                        claseGlobal.getListaPacientes().clear();
                    }
                    JSONArray jsonArray = response.getJSONArray("pacientes");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Pacientes nuevoPacientes = obtieneNuevoPaciente(jsonObject);
                        // Agrega un nuevo paciente a la lista en tu fragmento
                        claseGlobal.getListaPacientes().add(nuevoPacientes);
                    }
                    // Actualiza la lista de pacientes en ClaseGlobal
                    claseGlobal.setListaPacientes(claseGlobal.getListaPacientes());
                    pacientesObtenidos.postValue(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                pacientesObtenidos.postValue(false);
            }
        });

        return pacientesObtenidos;
    }

    private Pacientes obtieneNuevoPaciente(JSONObject jsonObject) {
        return new Pacientes(jsonObject.optString("nombre"), jsonObject.optString("apellidos"),
                jsonObject.optString("foto"), jsonObject.optString("fecha_nacimiento"),
                jsonObject.optString("dni"), jsonObject.optString("lugar_nacimiento"),
                jsonObject.optString("sexo"), jsonObject.optString("cip_sns"),
                jsonObject.optInt("num_seguridad_social"), jsonObject.optInt("fk_id_unidad"),
                jsonObject.optInt("fk_id_seguro"), jsonObject.optInt("fk_num_habitacion"),
                jsonObject.optInt("fk_num_historia_clinica"), jsonObject.optString("fecha_ingreso"),
                jsonObject.optString("estado_civil"));
    }
    // Otros métodos ViewModel relacionados con la lógica de negocio y presentación.

}
