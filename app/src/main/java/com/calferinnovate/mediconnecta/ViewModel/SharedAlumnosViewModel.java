package com.calferinnovate.mediconnecta.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.ContactoFamiliares;
import com.calferinnovate.mediconnecta.Model.ControlSomatometrico;
import com.calferinnovate.mediconnecta.Model.Pae;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;

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
        ClaseGlobal claseGlobal = viewModelArgs.getClaseGlobal();
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
        mutableFamiliaresList.setValue(new ArrayList<>());

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
                                jsonObject.optString("otros"), jsonObject.optString("medicacion")
                        );
                        paeArrayList.add(nuevoPae);
                    }

                    if (!paeArrayList.isEmpty()) {
                        mutablePaeList.postValue(new ArrayList<>(paeArrayList));
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

        return mutablePaeList;
    }

    public LiveData<ArrayList<ControlSomatometrico>> obtieneControlSomatometrico(Pae pae) {
        String url = Constantes.url_part + "control_somatometrico.php?id_pae=" + pae.getIdPae();

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<ControlSomatometrico> controlSomatometricoArrayList= new ArrayList<>();
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

                    if (!controlSomatometricoArrayList.isEmpty()) {
                        mutableControlSomatometricoList.postValue(new ArrayList<>(controlSomatometricoArrayList));
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

        return mutableControlSomatometricoList;
    }


}
