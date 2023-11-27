package com.calferinnovate.mediconnecta.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.calferinnovate.mediconnecta.Model.Area;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.Model.Unidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * ViewModel encargado de gestionar la selección de área y unidad de trabajo.
 * Usa variables MutableLiveData para modificar el valor contenido en la variable después de la creación
 * y utiliza LiveData para transmitir los datos hacia la UI, posteriormente proporciona actualizaciones
 * a los observadores del fragmento.
 */

public class SeleccionUnidadViewModel extends ViewModel {
    private PeticionesJson peticionesJson;
    private ClaseGlobal claseGlobal;
    private final ArrayList<Area> listaAreas = new ArrayList<>();
    private final MutableLiveData<ArrayList<String>> arrayListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> arrayListMutableLiveDataUnidades = new MutableLiveData<>();
    private final MutableLiveData<Boolean> pacientesObtenidos = new MutableLiveData<>();

    /**
     * Constructor por defecto.
     */
    public SeleccionUnidadViewModel() {

    }

    /**
     * Constructor que recibe instancia de ViewModelArgs para proporcionar PeticionesJson y de
     * ClaseGlobal.
     *
     * @param viewModelArgs Instancia de ViewModelArgs que proporciona PeticionesJson y ClaseGlobal.
     */
    public SeleccionUnidadViewModel(ViewModelArgs viewModelArgs) {
        this.peticionesJson = viewModelArgs.getPeticionesJson();
        this.claseGlobal = viewModelArgs.getClaseGlobal();
    }

    /**
     * Método que realiza una solicitud al servidor a través de PeticionesJson para obtener la lista
     * de areas, encapsula el contenido del arraylist en arrayListMutableLiveData, setea la lista en claseGlobal
     * y devuelve el LiveData.
     *
     * @return LiveData con el lista de nombres de las áreas.
     */
    public LiveData<ArrayList<String>> obtenerDatosAreas() {
        String urlArea = Constantes.url_part + "areas.php";
        peticionesJson.getJsonObjectRequest(urlArea, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<String> areaArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("datos_area");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Area nuevaArea = new Area(jsonObject.optInt("id_area"), jsonObject.optString("nombre_area"));
                        listaAreas.add(nuevaArea);
                        areaArrayList.add(nuevaArea.getNombre());
                    }

                    if (!areaArrayList.isEmpty()) {
                        arrayListMutableLiveData.postValue(new ArrayList<>(areaArrayList));
                        claseGlobal.setListaAreas(listaAreas);
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
        return arrayListMutableLiveData;
    }

    /**
     * Método que realiza una solicitud al servidor a través de PeticionesJson para obtener la lista
     * de unidades pertenecientes a un área, encapsula el contenido del arraylist en arrayListMutableLiveDataUnidades,
     * setea la lista en claseGlobal y devuelve el LiveData.
     *
     * @param areaSeleccionada Area seleccionada en el Spinner.
     * @return LiveData con el lista de nombres de las unidades.
     */
    public LiveData<ArrayList<String>> obtenerDatosUnidades(String areaSeleccionada) {
        String urlUnidades = Constantes.url_part + "unidades.php?nombre_area=" + areaSeleccionada;
        peticionesJson.getJsonObjectRequest(urlUnidades, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<String> unidadesArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("datos_unidades");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Unidades nuevaUnidad = new Unidades(jsonObject.optInt("id_unidad"), jsonObject.optInt("fk_id_area"),
                                jsonObject.optString("nombre"));
                        claseGlobal.getListaUnidades().add(nuevaUnidad);
                        unidadesArrayList.add(nuevaUnidad.getNombreUnidad());
                    }
                    if (!unidadesArrayList.isEmpty()) {
                        arrayListMutableLiveDataUnidades.postValue(new ArrayList<>(unidadesArrayList));
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
        return arrayListMutableLiveDataUnidades;
    }

    /**
     * Método que realiza una solicitud al servidor a través de PeticionesJson para obtener la lista
     * de pacientes pertenecientes a una unidad, setea la lista de pacientes en claseGlobal.
     * Si se han obtenido correctamente los datos se pone el MutableLiveData pacientesObtenidos a true y devuelve el LiveData.
     *
     * @param nombreUnidad Nombre de la unidad seleccionada.
     * @return LiveData booleano con la verificación de si se han obtenido los pacientes.
     */
    public LiveData<Boolean> obtieneDatosPacientes(String nombreUnidad) {
        String url = Constantes.url_part + "pacientes.php?nombre=" + nombreUnidad;

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!claseGlobal.getListaPacientes().isEmpty()) {
                        claseGlobal.getListaPacientes().clear();
                    }
                    JSONArray jsonArray = response.getJSONArray("pacientes");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Pacientes nuevoPacientes = obtieneNuevoPaciente(jsonObject);
                        claseGlobal.getListaPacientes().add(nuevoPacientes);
                    }

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

}
