package com.calferinnovate.mediconnecta.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.Empleado;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * ViewModel encargado de gestionar el inicio de sesión.
 * Contiene métodos para iniciar sesión y proporciona un LiveDara para observar el estado de la sesión.
 */
public class SesionViewModel extends ViewModel {

    private ClaseGlobal claseGlobal;
    private PeticionesJson peticionesJson;
    private final MutableLiveData<Boolean> empleadoIniciaSesionMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> empleadoEnfermero = new MutableLiveData<>();
    private final MutableLiveData<Boolean> empleadoAdministrativo = new MutableLiveData<>();
    private final MutableLiveData<Boolean> empleadoMedico = new MutableLiveData<>();
    private final MutableLiveData<Boolean> empleadoTerapeutaOcupacional = new MutableLiveData<>();
    private final MutableLiveData<Boolean> empleadoFisio = new MutableLiveData<>();
    private final MutableLiveData<Boolean> empleadoTrabajadorSocial = new MutableLiveData<>();
    private MutableLiveData<String> cargoEmpleado = new MutableLiveData<>();

    /**
     * Constructor vacío del ViewModel
     */
    public SesionViewModel(){

    }

    /**
     * Constructor que recive ViewModelArgs para inicializar dependencias.
     * @param viewModelArgs contiene las instancias de ClaseGlobal y PeticionesJson necesarias para el
     *                      ViewModel.
     */
    public SesionViewModel(ViewModelArgs viewModelArgs){
        claseGlobal = viewModelArgs.getClaseGlobal();
        peticionesJson = viewModelArgs.getPeticionesJson();
    }

    /**
     * Obtiene un LiveData para observar el estado de inicio de sesión del empleado.
     * @return empleadoIniciaSesionMutableLiveData LiveData que indica si el empleado ha iniciado sesión.
     */
    public LiveData<Boolean> getEmpleadoIniciaSesion(){
        return empleadoIniciaSesionMutableLiveData;
    }

    /**
     * Inicia sesión del empleado haciendo una solicitud JSON al servidor.
     * Actualiza el estado de inicio de sesión en LiveData según la respuesta del servidor.
     * @param user Nombre de usuario del empleado. Obtenido del TextView username del fragmento SesionFragment.
     * @param password Contraseña del empleado. Obtenido del TextView password del fragmento SesionFragment.
     */
    public void inicioSesion(String user, String password){
        String url = Constantes.url_part + "inicio_sesion.php?user=" + user +
                "&pwd=" + password;
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("empleados");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Empleado empleadoLogueado = new Empleado(object.optString("user"), object.optString("pwd"),
                                object.optString("nombre"), object.optString("apellidos"), object.optString("nombreCargo"),
                                object.optInt("cod_empleado"), object.optInt("fk_cargo"), object.getString("foto"));
                        claseGlobal.setEmpleado(empleadoLogueado);
                    }
                    empleadoIniciaSesionMutableLiveData.setValue(true);

                } catch (JSONException e) {
                    Log.d("Exception", String.valueOf(e));
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                empleadoIniciaSesionMutableLiveData.setValue(false);
            }
        });
    }






}
