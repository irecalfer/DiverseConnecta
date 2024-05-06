package com.calferinnovate.mediconnecta.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.Aulas;
import com.calferinnovate.mediconnecta.Model.Cargo;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.Empleado;
import com.calferinnovate.mediconnecta.Model.EmpleadosTrabajanAulas;
import com.calferinnovate.mediconnecta.Model.NivelEscolar;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * ViewModel encargado de gestionar el inicio de sesión.
 * Contiene métodos para iniciar sesión y proporciona un LiveDara para observar el estado de la sesión.
 */
public class SesionViewModel extends ViewModel {

    private ClaseGlobal claseGlobal;
    private PeticionesJson peticionesJson;
    private final MutableLiveData<Boolean> empleadoIniciaSesionMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> pacientesObtenidos = new MutableLiveData<>();
    private final MutableLiveData<Boolean> empleadosObtenidos = new MutableLiveData<>();
    private final MutableLiveData<Boolean> relacionEmpleadosAulasObtenidos = new MutableLiveData<>();
    private final MutableLiveData<Boolean> aulasObtenidas = new MutableLiveData<>();
    private final MutableLiveData<Boolean> nivelesEscolaresObtenidos = new MutableLiveData<>();
    private final MutableLiveData<Boolean> cargosObtenidos = new MutableLiveData<>();

    /**
     * Constructor vacío del ViewModel
     */
    public SesionViewModel() {

    }

    /**
     * Constructor que recive ViewModelArgs para inicializar dependencias.
     *
     * @param viewModelArgs contiene las instancias de ClaseGlobal y PeticionesJson necesarias para el
     *                      ViewModel.
     */
    public SesionViewModel(ViewModelArgs viewModelArgs) {
        claseGlobal = viewModelArgs.getClaseGlobal();
        peticionesJson = viewModelArgs.getPeticionesJson();
    }

    /**
     * Obtiene un LiveData para observar el estado de inicio de sesión del empleado.
     *
     * @return empleadoIniciaSesionMutableLiveData LiveData que indica si el empleado ha iniciado sesión.
     */
    public LiveData<Boolean> getEmpleadoIniciaSesion() {
        return empleadoIniciaSesionMutableLiveData;
    }

    /**
     * Inicia sesión del empleado haciendo una solicitud JSON al servidor.
     * Actualiza el estado de inicio de sesión en LiveData según la respuesta del servidor.
     *
     * @param user     Nombre de usuario del empleado. Obtenido del TextView username del fragmento SesionFragment.
     * @param password Contraseña del empleado. Obtenido del TextView password del fragmento SesionFragment.
     */
    public void inicioSesion(String user, String password) {
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
                                object.optString("nombre"), object.optString("apellidos"),
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


    /**
     * Método que realiza una solicitud al servidor a través de PeticionesJson para obtener la lista
     * de pacientes pertenecientes a una unidad, setea la lista de pacientes en claseGlobal.
     * Si se han obtenido correctamente los datos se pone el MutableLiveData pacientesObtenidos a true y devuelve el LiveData.
     *
     * @return LiveData booleano con la verificación de si se han obtenido los pacientes.
     */
    public LiveData<Boolean> obtieneDatosAlumnos() {
        String url = Constantes.url_part + "alumnos.php";

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!claseGlobal.getListaAlumnos().isEmpty()) {
                        claseGlobal.getListaAlumnos().clear();
                    }
                    JSONArray jsonArray = response.getJSONArray("alumnos");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Alumnos nuevoAlumnos = obtieneNuevoPaciente(jsonObject);
                        claseGlobal.getListaAlumnos().add(nuevoAlumnos);
                    }

                    claseGlobal.setListaAlumnos(claseGlobal.getListaAlumnos());
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

    private Alumnos obtieneNuevoPaciente(JSONObject jsonObject) {
        return new Alumnos(jsonObject.optString("nombre"), jsonObject.optString("apellidos"),
                jsonObject.optString("foto"), jsonObject.optString("fecha_nacimiento"),
                jsonObject.optString("dni"),
                jsonObject.optString("sexo"), jsonObject.optString("grado_discapacidad"),
                jsonObject.optInt("id_alumno"), jsonObject.optInt("fk_profesor"),
                jsonObject.optInt("fk_aula"));
    }


    /**
     * Método que realiza una solicitud al servidor a través de PeticionesJson para obtener la lista
     * de pacientes pertenecientes a una unidad, setea la lista de pacientes en claseGlobal.
     * Si se han obtenido correctamente los datos se pone el MutableLiveData pacientesObtenidos a true y devuelve el LiveData.
     *
     * @return LiveData booleano con la verificación de si se han obtenido los pacientes.
     */
    public LiveData<Boolean> obtieneDatosEmpleados() {
        String url = Constantes.url_part + "empleados.php";
        empleadosObtenidos.setValue(false);
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!claseGlobal.getListaEmpleados().isEmpty()) {
                        claseGlobal.getListaEmpleados().clear();
                    }
                    JSONArray jsonArray = response.getJSONArray("empleados");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Empleado nuevoEmpleado= obtieneNuevoEmpleado(jsonObject);
                        claseGlobal.getListaEmpleados().add(nuevoEmpleado);
                    }

                    claseGlobal.setListaEmpleados(claseGlobal.getListaEmpleados());
                    empleadosObtenidos.postValue(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                empleadosObtenidos.postValue(false);
            }
        });

        return empleadosObtenidos;
    }

    private Empleado obtieneNuevoEmpleado(JSONObject jsonObject) {
        return new Empleado(jsonObject.optString("user"), jsonObject.optString("pwd"),
                jsonObject.optString("nombre"), jsonObject.optString("apellidos"),
                jsonObject.optInt("cod_empleado"), jsonObject.optInt("fk_cargo"),
                jsonObject.optString("foto"));
    }

    /**
     * Método que realiza una solicitud al servidor a través de PeticionesJson para obtener la lista
     * de pacientes pertenecientes a una unidad, setea la lista de pacientes en claseGlobal.
     * Si se han obtenido correctamente los datos se pone el MutableLiveData pacientesObtenidos a true y devuelve el LiveData.
     *
     * @return LiveData booleano con la verificación de si se han obtenido los pacientes.
     */
    public LiveData<Boolean> obtieneRelacionEmpleadosAulas() {
        String url = Constantes.url_part + "empleadosAulas.php";

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!claseGlobal.getListaEmpleadosAulas().isEmpty()) {
                        claseGlobal.getListaEmpleadosAulas().clear();
                    }
                    JSONArray jsonArray = response.getJSONArray("empleadosAulas");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        EmpleadosTrabajanAulas nuevaRelacion= obtieneNuevaRelacion(jsonObject);
                        claseGlobal.getListaEmpleadosAulas().add(nuevaRelacion);
                    }

                    claseGlobal.setListaEmpleadosAulas(claseGlobal.getListaEmpleadosAulas());
                    relacionEmpleadosAulasObtenidos.postValue(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                relacionEmpleadosAulasObtenidos.postValue(false);
            }
        });

        return relacionEmpleadosAulasObtenidos;
    }

    private EmpleadosTrabajanAulas obtieneNuevaRelacion(JSONObject jsonObject) {
        return new EmpleadosTrabajanAulas(jsonObject.optInt("id_relacion_empleados_aulas"),
                jsonObject.optInt("cod_empleado"),
                jsonObject.optInt("id_aula"));
    }

    /**
     * Método que realiza una solicitud al servidor a través de PeticionesJson para obtener la lista
     * de pacientes pertenecientes a una unidad, setea la lista de pacientes en claseGlobal.
     * Si se han obtenido correctamente los datos se pone el MutableLiveData pacientesObtenidos a true y devuelve el LiveData.
     *
     * @return LiveData booleano con la verificación de si se han obtenido los pacientes.
     */
    public LiveData<Boolean> obtieneAulas() {
        String url = Constantes.url_part + "aulas.php";

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!claseGlobal.getListaAulas().isEmpty()) {
                        claseGlobal.getListaAulas().clear();
                    }
                    JSONArray jsonArray = response.getJSONArray("aulas");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Aulas nuevaAula= obtieneNuevaAula(jsonObject);
                        claseGlobal.getListaAulas().add(nuevaAula);
                    }

                    claseGlobal.setListaAulas(claseGlobal.getListaAulas());
                    aulasObtenidas.postValue(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                aulasObtenidas.postValue(false);
            }
        });

        return aulasObtenidas;
    }

    private Aulas obtieneNuevaAula(JSONObject jsonObject) {
        return new Aulas(jsonObject.optInt("id_aula"),
                jsonObject.optInt("fk_nivel_escolar"),
                jsonObject.optString("nombre"));
    }

    /**
     * Método que realiza una solicitud al servidor a través de PeticionesJson para obtener la lista
     * de pacientes pertenecientes a una unidad, setea la lista de pacientes en claseGlobal.
     * Si se han obtenido correctamente los datos se pone el MutableLiveData pacientesObtenidos a true y devuelve el LiveData.
     *
     * @return LiveData booleano con la verificación de si se han obtenido los pacientes.
     */
    public LiveData<Boolean> obtieneNivelEscolar() {
        String url = Constantes.url_part + "nivel_escolar.php";

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!claseGlobal.getListaNivelEscolar().isEmpty()) {
                        claseGlobal.getListaNivelEscolar().clear();
                    }
                    JSONArray jsonArray = response.getJSONArray("nivel_escolar");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        NivelEscolar nuevoNivel= obtieneNuevoNivel(jsonObject);
                        claseGlobal.getListaNivelEscolar().add(nuevoNivel);
                    }

                    claseGlobal.setListaNivelEscolar(claseGlobal.getListaNivelEscolar());
                    nivelesEscolaresObtenidos.postValue(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                nivelesEscolaresObtenidos.postValue(false);
            }
        });

        return nivelesEscolaresObtenidos;
    }

    private NivelEscolar obtieneNuevoNivel(JSONObject jsonObject) {
        return new NivelEscolar(jsonObject.optInt("id_nivel_escolar"),
                jsonObject.optString("nombre_nivel"));
    }

    public LiveData<Boolean> obtieneCargos() {
        String url = Constantes.url_part + "cargos.php";

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!claseGlobal.getCargoArrayList().isEmpty()) {
                        claseGlobal.getCargoArrayList().clear();
                    }
                    JSONArray jsonArray = response.getJSONArray("cargos");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Cargo nuevoCargo= new Cargo(jsonObject.optInt("id_cargo"), jsonObject.optString("nombre"));
                        claseGlobal.getCargoArrayList().add(nuevoCargo);
                    }

                    claseGlobal.setCargoArrayList(claseGlobal.getCargoArrayList());
                    cargosObtenidos.postValue(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                cargosObtenidos.postValue(false);
            }
        });

        return cargosObtenidos;
    }

    public void recargarDatosEmpleados() {
        obtieneDatosEmpleados(); // Método para obtener los datos de los empleados
    }

}
