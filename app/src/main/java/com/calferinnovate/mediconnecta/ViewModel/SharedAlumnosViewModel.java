package com.calferinnovate.mediconnecta.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.VolleyError;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.Aulas;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.ContactoFamiliares;
import com.calferinnovate.mediconnecta.Model.ControlSomatometrico;
import com.calferinnovate.mediconnecta.Model.Crisis;
import com.calferinnovate.mediconnecta.Model.Curso;
import com.calferinnovate.mediconnecta.Model.Empleado;
import com.calferinnovate.mediconnecta.Model.Especialista;
import com.calferinnovate.mediconnecta.Model.Pae;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.Model.Seguimiento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * ViewModel compartido encargado de gestionar los datos relativos a los pacientes.
 * Usa variables MutableLiveData para modificar el valor contenido en la variable después de la creación
 * y utiliza LiveData para transmitir los datos hacia la UI, posteriormente proporciona actualizaciones
 * a los observadores del fragmento.
 */
public class SharedAlumnosViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Alumnos>> mutablePacientesList = new MutableLiveData<>();
    private final MutableLiveData<Alumnos> mutablePaciente = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ContactoFamiliares>> mutableFamiliaresList = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> listaLugaresLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> listaSexoLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> listaEstadoCivilLiveData = new MutableLiveData<>();
    private PeticionesJson peticionesJson;
    private MutableLiveData<ArrayList<Pae>> mutablePaeList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<ControlSomatometrico>> mutableControlSomatometricoList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> mutableLiveDataCursos = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Aulas>> mutableLiveDataAulas = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Empleado>> mutableLiveDataEmpleadosActualizados = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Seguimiento>> mutableLiveDataSeguimientoArrayList = new MutableLiveData<>();
    private final MutableLiveData<Seguimiento> mutableSeguimiento = new MutableLiveData<>();
    private ClaseGlobal  claseGlobal;
    private final MutableLiveData<Boolean> seguimientoUpdated = new MutableLiveData<>();
    private final MutableLiveData<Boolean> crisisUpdated = new MutableLiveData<>();
    private MutableLiveData<Boolean> opcionesSeguimientoCerrado = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Crisis>> mutableLiveDataCrisisArrayList = new MutableLiveData<>();
    private final MutableLiveData<Crisis> mutableCrisis = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Crisis>> mutableLiveDataTotalCrisisArrayList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Especialista>> mutableLiveDataEspecialistasArrayList = new MutableLiveData<>();
    private final MutableLiveData<Especialista> mutableEspecialista = new MutableLiveData<>();
    private final MutableLiveData<Boolean> especialistaUpdate = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> listaDiscapacidadLiveData = new MutableLiveData<>();


    /**
     * Constructor por defecto.
     */
    public SharedAlumnosViewModel() {

    }

    /**
     * Constructor que recibe instancia de ViewModelArgs para proporcionar PeticionesJson y de
     * ClaseGlobal.
     *
     * @param viewModelArgs Instancia de ViewModelArgs que proporciona PeticionesJson y ClaseGlobal.
     */
    public SharedAlumnosViewModel(ViewModelArgs viewModelArgs) {
        this.peticionesJson = viewModelArgs.getPeticionesJson();
        this.claseGlobal = viewModelArgs.getClaseGlobal();
    }

    /**
     * Si mutablePacientesList es nulo, encapsula el contenido de listaPacientes de la clase global
     * en mutablePacientesList y devuelve el LiveData.
     *
     * @return LiveData con la lista de pacientes.
     */
    public LiveData<ArrayList<Alumnos>> getPacientesList() {

        if (mutablePacientesList.getValue() == null) {
            mutablePacientesList.postValue(ClaseGlobal.getInstance().getListaAlumnos());
        }
        return mutablePacientesList;
    }

    /**
     * Devuelve el paciente seleccionado
     *
     * @return LiveData paciente seleccionado
     */
    public LiveData<Alumnos> getPaciente() {
        return mutablePaciente;
    }

    /**
     * Asigna a mutablePaciente el paciente seleccionado.
     *
     * @param position posición del paciente seleccionado.
     */
    public void setPaciente(int position) {
        Alumnos pacienteSeleccionado = mutablePacientesList.getValue().get(position);
        mutablePaciente.postValue(pacienteSeleccionado);
    }


    /**
     * Método que realiza una solicitud al servidor a través de PeticionesJson para obtener la lista
     * de contactos de un paciente, encapsula el contenido del arraylist en mutableFamiliaresList
     * y devuelve el LiveData.
     *
     * @param paciente Paciente seleccionado.
     * @return LiveData lista de contactos del paciente.
     */
    public LiveData<ArrayList<ContactoFamiliares>> obtieneContactoFamiliares(Alumnos paciente) {
        String url = Constantes.url_part + "familiares.php?cip_sns=" + paciente.getIdAlumno();


        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<ContactoFamiliares> contactoFamiliaresArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("familiares_contacto");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ContactoFamiliares nuevoFamiliar = new ContactoFamiliares(jsonObject.optString("dni_familiar"),
                                jsonObject.optString("nombre"), jsonObject.optString("apellidos"),
                                jsonObject.optInt("telefono_contacto"), jsonObject.optInt("telefono_contacto_2"));
                        contactoFamiliaresArrayList.add(nuevoFamiliar);
                    }
                    if (!contactoFamiliaresArrayList.isEmpty()) {
                        mutableFamiliaresList.postValue(new ArrayList<>(contactoFamiliaresArrayList));
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

        return mutableFamiliaresList;
    }


    public LiveData<ArrayList<String>> obtieneSexoPacientesEnum() {
        String url = Constantes.url_part + "sexo.php";
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<String> sexoArrayList = new ArrayList<>();
                    JSONArray sexoArray = response.getJSONArray("sexo");

                    // Itera sobre los valores ENUM
                    for (int i = 0; i < sexoArray.length(); i++) {
                        String enumSexo = sexoArray.getString(i);
                        //Añadimos el valor a nuestro arrayList
                        sexoArrayList.add(enumSexo);
                    }

                    // Actualiza el LiveData con la nueva lista
                    if (!sexoArrayList.isEmpty()) {
                        listaSexoLiveData.postValue(new ArrayList<>(sexoArrayList));
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

        return listaSexoLiveData;
    }

    public LiveData<ArrayList<Pae>> obtienePae(Alumnos alumno) {
        String url = Constantes.url_part + "pae.php?id_alumno=" + alumno.getIdAlumno();
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Pae> paeArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("pae");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Pae nuevoPae = new Pae(jsonObject.optInt("id_pae"), jsonObject.optInt("curso_emision_inicio"),
                                jsonObject.optInt("curso_emision_final"), jsonObject.optInt("fk_id_enfermero"),
                                jsonObject.optInt("fk_id_profesor"), jsonObject.optInt("fk_id_alumnos"),
                                jsonObject.optString("alergias"), jsonObject.optString("diagnostico_clinico"),
                                jsonObject.optString("fiebre"), jsonObject.optString("dieta_alimentacion"),
                                jsonObject.optString("protesis"), jsonObject.optString("ortesis"),
                                jsonObject.optString("gafas"), jsonObject.optString("audifonos"),
                                jsonObject.optString("otros"), jsonObject.optString("medicacion"),
                                jsonObject.optString("datos_importantes"), jsonObject.optInt("ultima_modificacion_personal"),
                                jsonObject.optString("ultima_modificacion_tiempo"), jsonObject.optInt("fk_id_aula")
                        );
                        paeArrayList.add(nuevoPae);
                    }


                    mutablePaeList.postValue(new ArrayList<>(paeArrayList));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        return mutablePaeList;
    }

    // Método para obtener el control somatométrico del PAE
    public LiveData<ArrayList<ControlSomatometrico>> obtieneControlSomatometrico(Pae pae) {
        String url = Constantes.url_part + "control_somatometrico.php?id_pae=" + pae.getIdPae();
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<ControlSomatometrico> controlSomatometricoArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("control");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ControlSomatometrico nuevoControl = new ControlSomatometrico(jsonObject.optInt("id_control"),
                                jsonObject.optInt("frecuencia_cardiaca"),
                                jsonObject.optInt("saturacion_o2"), jsonObject.optInt("fk_trimestre"),
                                jsonObject.optInt("fk_id_pae"), jsonObject.optDouble("peso"), jsonObject.optDouble("imc"),
                                jsonObject.optDouble("percentil"), jsonObject.optDouble("temperatura"),
                                jsonObject.optString("talla"), jsonObject.optString("tension_arterial")
                        );
                        controlSomatometricoArrayList.add(nuevoControl);
                    }


                    mutableControlSomatometricoList.postValue(new ArrayList<>(controlSomatometricoArrayList));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        return mutableControlSomatometricoList;
    }



    public LiveData<ArrayList<Aulas>> obtieneListaAulas() {
        String url = Constantes.url_part + "aulas.php";

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Aulas> aulasArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("aulas");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Aulas nuevaAula = new Aulas(jsonObject.optInt("id_aula"),
                                jsonObject.optInt("fk_nivel_escolar"), jsonObject.optString("nombre"));
                        aulasArrayList.add(nuevaAula);
                    }

                    if (!aulasArrayList.isEmpty()) {
                        mutableLiveDataAulas.postValue(new ArrayList<>(aulasArrayList));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        return mutableLiveDataAulas;
    }


    public LiveData<ArrayList<String>> obtieneListaCursos() {
        String url = Constantes.url_part + "listaCursos.php";

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<String> cursoArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("cursos");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Curso nuevoCurso = new Curso(jsonObject.optString("añoInicio"),
                                jsonObject.optString("añoFin"));
                        cursoArrayList.add(nuevoCurso.getAñoInicio() + "/" + nuevoCurso.getAñoFin());
                        ClaseGlobal.getInstance().getCursoArrayList().add(nuevoCurso);
                    }

                    ClaseGlobal.getInstance().setCursoArrayList(ClaseGlobal.getInstance().getCursoArrayList());
                    if (!cursoArrayList.isEmpty()) {
                        mutableLiveDataCursos.postValue(new ArrayList<>(cursoArrayList));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        return mutableLiveDataCursos;
    }
    // Método para limpiar los datos del PAE al cambiar de alumno
    public void limpiarDatos() {
        mutablePaeList = new MutableLiveData<>();
        mutableControlSomatometricoList = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Empleado>> recargaListaEmpleados() {
        String url = Constantes.url_part + "empleados.php";
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Prueba", "Entra en try");
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
                    mutableLiveDataEmpleadosActualizados.postValue(claseGlobal.getListaEmpleados());
                } catch (JSONException e) {
                    Log.d("Prueba", "Entra en catch");
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("Prueba", "onError");
            }
        });
        return mutableLiveDataEmpleadosActualizados;
    }

    private Empleado obtieneNuevoEmpleado(JSONObject jsonObject) {
        return new Empleado(jsonObject.optString("user"), jsonObject.optString("pwd"),
                jsonObject.optString("nombre"), jsonObject.optString("apellidos"),
                jsonObject.optInt("cod_empleado"), jsonObject.optInt("fk_cargo"),
                jsonObject.optString("foto"));
    }

    public LiveData<ArrayList<Seguimiento>> getListaSeguimientos(Alumnos alumno) {
        String url = Constantes.url_part + "seguimiento.php?fk_id_alumno="+alumno.getIdAlumno();

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Seguimiento> seguimientoArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("seguimiento");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Seguimiento nuevoSeguimiento = new Seguimiento(jsonObject.optInt("id_seguimiento"),
                                jsonObject.optString("fecha_y_hora"), jsonObject.optString("descripcion"),
                                jsonObject.optInt("fk_id_alumno"));
                        seguimientoArrayList.add(nuevoSeguimiento);
                    }

                    claseGlobal.setSeguimientoArrayList(seguimientoArrayList);
                    if (!seguimientoArrayList.isEmpty()) {
                        mutableLiveDataSeguimientoArrayList.postValue(new ArrayList<>(claseGlobal.getSeguimientoArrayList()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        return mutableLiveDataSeguimientoArrayList;
    }

    public void setSeguimiento(int position) {
        Seguimiento seguimientoSeleccionado = mutableLiveDataSeguimientoArrayList.getValue().get(position);
        mutableSeguimiento.postValue(seguimientoSeleccionado);
    }

    public LiveData<Seguimiento> getSeguimiento(){
        return mutableSeguimiento;
    }

    public void setSeguimientoUpdated(Boolean updated) {
        seguimientoUpdated.setValue(updated);
    }

    public LiveData<Boolean> getSeguimientoUpdated() {
        return seguimientoUpdated;
    }

    public void setCrisisUpdated(Boolean updated){
        crisisUpdated.setValue(updated);
    }

    public LiveData<Boolean> getCrisisUpdate(){
        return crisisUpdated;
    }

    public LiveData<Boolean> getOpcionesSeguimientoCerrado() {
        return opcionesSeguimientoCerrado;
    }

    public void setOpcionesSeguimientoCerrado(boolean closed) {
        opcionesSeguimientoCerrado.setValue(closed);
    }

    public LiveData<ArrayList<Seguimiento>> getListaSeguimientosFecha(Alumnos alumno, String fechaInicio, String fechaFin) {
        String url = Constantes.url_part + "obtiene_seguimiento_fechas.php?fk_id_alumno="+alumno.getIdAlumno() +"&fecha_inicio="+ fechaInicio +"&fecha_fin=" + fechaFin;

        mutableLiveDataSeguimientoArrayList.setValue(new ArrayList<>());
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Seguimiento> seguimientoArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("seguimiento_fecha");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Seguimiento nuevoSeguimiento = new Seguimiento(jsonObject.optInt("id_seguimiento"),
                                jsonObject.optString("fecha_y_hora"), jsonObject.optString("descripcion"),
                                jsonObject.optInt("fk_id_alumno"));
                        seguimientoArrayList.add(nuevoSeguimiento);
                    }

                    claseGlobal.setSeguimientoArrayList(seguimientoArrayList);
                    if (!seguimientoArrayList.isEmpty()) {
                        mutableLiveDataSeguimientoArrayList.postValue(new ArrayList<>(claseGlobal.getSeguimientoArrayList()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        return mutableLiveDataSeguimientoArrayList;
    }

    /*CRISIS*/

    public LiveData<ArrayList<Crisis>> getListaCrisis(Alumnos alumno) {
        String url = Constantes.url_part + "crisis.php?fk_id_alumno="+alumno.getIdAlumno();

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Crisis> crisisArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("crisis");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Crisis nuevaCrisis = new Crisis(jsonObject.optInt("id_crisis"), jsonObject.optString("fecha_y_hora"),
                                jsonObject.optString("tipo"), jsonObject.optString("intensidad"),
                                jsonObject.optString("lugar"), jsonObject.optString("patrones"),
                                jsonObject.optString("descripcion"), jsonObject.optString("duracion"),
                                jsonObject.optString("recuperacion"), jsonObject.optInt("fk_id_alumno"));
                        crisisArrayList.add(nuevaCrisis);
                    }

                    claseGlobal.setCrisisArrayList(crisisArrayList);
                    if (!crisisArrayList.isEmpty()) {
                        mutableLiveDataCrisisArrayList.postValue(new ArrayList<>(claseGlobal.getCrisisArrayList()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        return mutableLiveDataCrisisArrayList;
    }

    public void setCrisis(int position) {
        Crisis crisisSeleccionada = mutableLiveDataCrisisArrayList.getValue().get(position);
        mutableCrisis.postValue(crisisSeleccionada);
    }

    public LiveData<Crisis> getCrisis(){
        return mutableCrisis;
    }

    public LiveData<ArrayList<Crisis>> getListaTotalCrisis() {
        String url = Constantes.url_part + "crisis.php";

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Crisis> crisisArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("crisis");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Crisis nuevaCrisis = new Crisis(jsonObject.optInt("id_crisis"), jsonObject.optString("fecha_y_hora"),
                                jsonObject.optString("tipo"), jsonObject.optString("intensidad"),
                                jsonObject.optString("lugar"), jsonObject.optString("patrones"),
                                jsonObject.optString("descripcion"), jsonObject.optString("duracion"),
                                jsonObject.optString("recuperacion"), jsonObject.optInt("fk_id_alumno"));
                        crisisArrayList.add(nuevaCrisis);
                    }

                    if (!crisisArrayList.isEmpty()) {
                        mutableLiveDataTotalCrisisArrayList.postValue(new ArrayList<>(crisisArrayList));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        return mutableLiveDataTotalCrisisArrayList;
    }

    public LiveData<ArrayList<Crisis>> getListaCrisisFecha(Alumnos alumno, String fechaInicio, String fechaFin) {
        String url = Constantes.url_part + "obtiene_crisis_fechas.php?fk_id_alumno="+alumno.getIdAlumno() +"&fecha_inicio="+ fechaInicio +"&fecha_fin=" + fechaFin;

        mutableLiveDataCrisisArrayList.setValue(new ArrayList<>());
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Crisis> crisisArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("crisis");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Crisis nuevaCrisis = new Crisis(jsonObject.optInt("id_crisis"), jsonObject.optString("fecha_y_hora"),
                                jsonObject.optString("tipo"), jsonObject.optString("intensidad"),
                                jsonObject.optString("lugar"), jsonObject.optString("patrones"),
                                jsonObject.optString("descripcion"), jsonObject.optString("duracion"),
                                jsonObject.optString("recuperacion"), jsonObject.optInt("fk_id_alumno"));
                        crisisArrayList.add(nuevaCrisis);
                    }

                    if (!crisisArrayList.isEmpty()) {
                        mutableLiveDataCrisisArrayList.postValue(new ArrayList<>(crisisArrayList));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        return mutableLiveDataCrisisArrayList;
    }

    public LiveData<ArrayList<Especialista>> getListaEspecialistas(Alumnos alumno) {
        String url = Constantes.url_part + "obtiene_especialistas_alumno.php?fk_id_alumno="+alumno.getIdAlumno();

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Especialista> especialistaArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("especialistas");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                       Especialista nuevoEspecialista = new Especialista(jsonObject.optInt("id_doctor"),
                               jsonObject.optString("nombre"),jsonObject.optString("centro"),
                               jsonObject.optString("especialidad"),jsonObject.optString("telefono"),
                               jsonObject.optString("email"), jsonObject.optInt("fk_id_alumno"));
                        especialistaArrayList.add(nuevoEspecialista);
                    }

                    if (!especialistaArrayList.isEmpty()) {
                        mutableLiveDataEspecialistasArrayList.postValue(new ArrayList<>(especialistaArrayList));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        return mutableLiveDataEspecialistasArrayList;
    }

    public void setEspecialista(int position) {
        Especialista especialistaSeleccionado = mutableLiveDataEspecialistasArrayList.getValue().get(position);
        mutableEspecialista.postValue(especialistaSeleccionado);
    }

    public LiveData<Especialista> getEspecialista(){
        return mutableEspecialista;
    }

    public void setEspecialistaUpdated(Boolean updated) {
        especialistaUpdate.setValue(updated);
    }

    public LiveData<Boolean> getEspecialistaUpdated() {
        return especialistaUpdate;
    }

    public LiveData<ArrayList<String>> obtieneGradoDiscapacidadEnum() {
        String url = Constantes.url_part + "grado_discapacidad.php";
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<String> discapacidadArrayList = new ArrayList<>();
                    JSONArray discapacidadArray = response.getJSONArray("grado_discapacidad");

                    // Itera sobre los valores ENUM
                    for (int i = 0; i < discapacidadArray.length(); i++) {
                        String enumDiscapacidad = discapacidadArray.getString(i);
                        //Añadimos el valor a nuestro arrayList
                        discapacidadArrayList.add(enumDiscapacidad);
                    }

                    // Actualiza el LiveData con la nueva lista
                    if (!discapacidadArrayList.isEmpty()) {
                        listaDiscapacidadLiveData.postValue(new ArrayList<>(discapacidadArrayList));
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

        return listaDiscapacidadLiveData;
    }
}
