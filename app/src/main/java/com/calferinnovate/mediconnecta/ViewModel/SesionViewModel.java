package com.calferinnovate.mediconnecta.ViewModel;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.Empleado;
import com.calferinnovate.mediconnecta.PeticionesHTTP.PeticionesJson;
import com.calferinnovate.mediconnecta.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SesionViewModel extends ViewModel {

    private ClaseGlobal claseGlobal;
    private PeticionesJson peticionesJson;
    private MutableLiveData<Boolean> empleadoIniciaSesionMutableLiveData = new MutableLiveData<>();

    public SesionViewModel(){

    }

    public SesionViewModel(ViewModelArgs viewModelArgs){
        claseGlobal = viewModelArgs.getClaseGlobal();
        peticionesJson = viewModelArgs.getPeticionesJson();
    }

    public LiveData<Boolean> getEmpleadoIniciaSesion(){
        return empleadoIniciaSesionMutableLiveData;
    }

    public void inicioSesion(String user, String password){
        String url = Constantes.url_part + "inicio_sesion.php?user=" + user +
                "&pwd=" + password;
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("empleados");
                    // Recorrer los datos del usuario
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
