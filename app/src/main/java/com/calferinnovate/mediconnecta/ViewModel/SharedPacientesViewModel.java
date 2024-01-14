package com.calferinnovate.mediconnecta.ViewModel;

import android.util.Base64;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.ContactoFamiliares;
import com.calferinnovate.mediconnecta.Model.Habitaciones;
import com.calferinnovate.mediconnecta.Model.HistoriaClinica;
import com.calferinnovate.mediconnecta.Model.Informes;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.Model.Pautas;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.Model.Seguro;
import com.calferinnovate.mediconnecta.Model.Unidades;

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
public class SharedPacientesViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Pacientes>> mutablePacientesList = new MutableLiveData<>();
    private final MutableLiveData<Pacientes> mutablePaciente = new MutableLiveData<>();
    private final MutableLiveData<Seguro> mutableSeguro = new MutableLiveData<>();
    private final MutableLiveData<HistoriaClinica> mutableHistoriaClinica = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Seguro>> mutableSeguroList = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ContactoFamiliares>> mutableFamiliaresList = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Informes>> mutableInformesList = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Pautas>> mutablePautasList = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> listaLugaresLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> listaSexoLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> listaEstadoCivilLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Unidades>> listaUnidadesLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Habitaciones>> listaHabitacionesLiveData = new MutableLiveData<>();
    private PeticionesJson peticionesJson;


    /**
     * Constructor por defecto.
     */
    public SharedPacientesViewModel() {

    }

    /**
     * Constructor que recibe instancia de ViewModelArgs para proporcionar PeticionesJson y de
     * ClaseGlobal.
     *
     * @param viewModelArgs Instancia de ViewModelArgs que proporciona PeticionesJson y ClaseGlobal.
     */
    public SharedPacientesViewModel(ViewModelArgs viewModelArgs) {
        this.peticionesJson = viewModelArgs.getPeticionesJson();
        ClaseGlobal claseGlobal = viewModelArgs.getClaseGlobal();
    }

    /**
     * Si mutablePacientesList es nulo, encapsula el contenido de listaPacientes de la clase global
     * en mutablePacientesList y devuelve el LiveData.
     *
     * @return LiveData con la lista de pacientes.
     */
    public LiveData<ArrayList<Pacientes>> getPacientesList() {

        if (mutablePacientesList.getValue() == null) {
            mutablePacientesList.postValue(ClaseGlobal.getInstance().getListaPacientes());
        }
        return mutablePacientesList;
    }

    /**
     * Devuelve el paciente seleccionado
     *
     * @return LiveData paciente seleccionado
     */
    public LiveData<Pacientes> getPaciente() {
        return mutablePaciente;
    }

    /**
     * Asigna a mutablePaciente el paciente seleccionado.
     *
     * @param position posición del paciente seleccionado.
     */
    public void setPaciente(int position) {
        Pacientes pacienteSeleccionado = mutablePacientesList.getValue().get(position);
        mutablePaciente.postValue(pacienteSeleccionado);
    }

    /**
     * Método que realiza una solicitud al servudor a través de PeticionesJson para obtener la lista
     * de seguros, encapsula el contenido del arraylist en mutableSeguroList, llama a obtieneSeguroPacienteSeleccionado
     * y obtiene el seguro perteneciente a ese paciente, encapsula el seguro en mutableSeguro y devuelve el LiveData.
     *
     * @param paciente Paciente seleccionado.
     * @return LiveData del seguro del paciente.
     */
    public LiveData<Seguro> obtieneSeguroPacientes(Pacientes paciente) {
        String url = Constantes.url_part + "seguro.php";

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Seguro> seguroArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("seguro");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Seguro nuevoSeguro = new Seguro(jsonObject.optInt("id_seguro"), jsonObject.optInt("telefono"),
                                jsonObject.optString("nombre"));
                        seguroArrayList.add(nuevoSeguro);
                    }
                    if (!seguroArrayList.isEmpty()) {
                        mutableSeguroList.postValue(new ArrayList<>(seguroArrayList));
                    }

                    Seguro seguroDelPaciente = obtieneSeguroPacienteSeleccionado(paciente, seguroArrayList);
                    mutableSeguro.postValue(seguroDelPaciente);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        return mutableSeguro;
    }


    /**
     * Método que busca si el paciente tiene un seguro perteneciente a la lista de seguros.
     *
     * @param paciente        Paciente seleccionado.
     * @param seguroArrayList Lista de seguros.
     * @return seguro si el paciente lo tiene, o null en caso de que no tenga seguro.
     */
    private Seguro obtieneSeguroPacienteSeleccionado(Pacientes paciente, ArrayList<Seguro> seguroArrayList) {
        for (Seguro seguro : seguroArrayList) {
            if (seguro.getIdSeguro() == paciente.getFkIdSeguro()) {
                return seguro;
            }
        }
        return null;
    }


    /**
     * Método que realiza una solicitud al servidor a través de PeticionesJson para obtener la lista
     * de contactos de un paciente, encapsula el contenido del arraylist en mutableFamiliaresList
     * y devuelve el LiveData.
     *
     * @param paciente Paciente seleccionado.
     * @return LiveData lista de contactos del paciente.
     */
    public LiveData<ArrayList<ContactoFamiliares>> obtieneContactoFamiliares(Pacientes paciente) {
        String url = Constantes.url_part + "familiares.php?cip_sns=" + paciente.getCipSns();
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


    /**
     * Método que realiza una solicitud al servidor a través de PeticionesJson para obtener la lista
     * de informes clínicos de un paciente, convierte el PDF de Base64 a un array de bytes, encapsula el
     * contenido del arraylist en mutableInformesList y devuelve el LiveData.
     *
     * @param paciente Paciente seleccionado.
     * @return LiveData que contienen la lista de informes clínicos del paciente.
     */
    public LiveData<ArrayList<Informes>> obtieneInformesPaciente(Pacientes paciente) {
        String url = Constantes.url_part + "informes.php?fk_num_historia_clinica=" + paciente.getFkNumHistoriaClinica();
        mutableInformesList.setValue(new ArrayList<>());

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Informes> informesArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("informes");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String base64String = jsonObject.optString("PDF");
                        byte[] pdfBytes = Base64.decode(base64String, Base64.DEFAULT);
                        Informes nuevoInforme = new Informes(jsonObject.optInt("fk_num_historia_clinica"),
                                jsonObject.optString("tipo_informe"), jsonObject.optString("fecha"),
                                jsonObject.optString("centro"), jsonObject.optString("responsable"),
                                jsonObject.optString("servicio_unidad_dispositivo"), jsonObject.optString("servicio_de_salud"),
                                pdfBytes);
                        informesArrayList.add(nuevoInforme);
                    }

                    if (!informesArrayList.isEmpty()) {
                        mutableInformesList.postValue(new ArrayList<>(informesArrayList));
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

        return mutableInformesList;
    }


    /**
     * Método que realiza una solicitud al servidor a través de PeticionesJson para obtener la
     * historia clínica de un paciente, encapsula el contenido del arraylist en mutableHistoriaClinica
     * y devuelve el LiveData.
     *
     * @param paciente Paciente seleccionado.
     * @return LiveData con el contenido de la historia clínica del paciente.
     */
    public LiveData<HistoriaClinica> obtieneHistoriaClinica(Pacientes paciente) {
        String url = Constantes.url_part + "historia_clinica.php?cip_sns=" + paciente.getCipSns();
        //Reiniciamos el mutable
        mutableHistoriaClinica.setValue(new HistoriaClinica());
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("historia_clinica");
                    HistoriaClinica historiaClinica = new HistoriaClinica(jsonObject.optString("datos_salud"),
                            jsonObject.optString("tratamiento"));
                    mutableHistoriaClinica.postValue(historiaClinica);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        return mutableHistoriaClinica;
    }

    /**
     * Método que realiza una solicitud al servidor a través de PeticionesJson para obtener la lista de
     * pautas de un paciente, encapsula el contenido del arraylist en mutablePautasList y devuelve el LiveData.
     *
     * @param paciente Paciente seleccionado.
     * @return LiveData con la lista de pautas pertenecientes al paciente.
     */
    public LiveData<ArrayList<Pautas>> obtienePautasPaciente(Pacientes paciente) {
        String url = Constantes.url_part + "pautas.php?cip_sns=" + paciente.getCipSns();
        mutablePautasList.setValue(new ArrayList<>());

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Pautas> pautasArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("pautas");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Pautas nuevaPauta = new Pautas(jsonObject.optString("pauta"),
                                jsonObject.optString("observaciones"), jsonObject.optString("mañana"),
                                jsonObject.optString("tarde"), jsonObject.optString("noche"));
                        pautasArrayList.add(nuevaPauta);
                    }

                    if (!pautasArrayList.isEmpty()) {
                        mutablePautasList.postValue(new ArrayList<>(pautasArrayList));
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

        return mutablePautasList;
    }


    /**
     * Método que realiza una solicitud al servidor a través de PeticionesJson para obtener la lista
     * de lugares de caída contenidos en un enum, encapsula el contenido del arraylist en listaLugaresLiveData
     * y devuelve el LiveData.
     *
     * @return LiveData con el contenido de los avisos de dicha fecha.
     */
    public LiveData<ArrayList<String>> obtieneLosLugaresDeCaidas() {
        String url = Constantes.url_part + "caidas_enum.php";
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<String> lugaresCaidaArrayList = new ArrayList<>();
                    JSONArray lugarCaida = response.getJSONArray("lugar_caida");

                    // Itera sobre los valores ENUM
                    for (int i = 0; i < lugarCaida.length(); i++) {
                        String enumLugar = lugarCaida.getString(i);
                        //Añadimos el valor a nuestro arrayList
                        lugaresCaidaArrayList.add(enumLugar);
                    }

                    // Actualiza el LiveData con la nueva lista
                    if (!lugaresCaidaArrayList.isEmpty()) {
                        listaLugaresLiveData.postValue(new ArrayList<>(lugaresCaidaArrayList));
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

        return listaLugaresLiveData;
    }

    public LiveData<ArrayList<Seguro>> obtieneListaSeguros() {
        String url = Constantes.url_part + "seguro.php";

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Seguro> seguroArrayList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("seguro");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Seguro nuevoSeguro = new Seguro(jsonObject.optInt("id_seguro"), jsonObject.optInt("telefono"),
                                jsonObject.optString("nombre"));
                        seguroArrayList.add(nuevoSeguro);
                    }
                    if (!seguroArrayList.isEmpty()) {
                        mutableSeguroList.postValue(new ArrayList<>(seguroArrayList));
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
        return mutableSeguroList;
    }

    public LiveData<ArrayList<Unidades>> obtieneListaDeUnidades() {
        String url = Constantes.url_part + "unidades.php";

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Unidades> listaUnidades = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("datos_unidades");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Unidades unidad = new Unidades(jsonObject.optInt("id_unidad"), jsonObject.optInt("fk_id_area"),
                                jsonObject.optString("nombre"));
                        listaUnidades.add(unidad);
                    }
                    if (!listaUnidades.isEmpty()) {
                        listaUnidadesLiveData.postValue(new ArrayList<>(listaUnidades));
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
        return listaUnidadesLiveData;
    }

    public LiveData<ArrayList<Habitaciones>> obtieneHabitacionesUnidades(String nombreUnidadSeleccionada) {
        String url = Constantes.url_part + "habitaciones.php?nombre_unidad=" + nombreUnidadSeleccionada;

        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Habitaciones> listaHabitaciones = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("habitaciones");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Habitaciones habitaciones = new Habitaciones(jsonObject.optInt("num_habitacion"), jsonObject.optInt("fk_id_unidad"));
                        listaHabitaciones.add(habitaciones);
                    }
                    if (!listaHabitaciones.isEmpty()) {
                        listaHabitacionesLiveData.postValue(new ArrayList<>(listaHabitaciones));
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
        return listaHabitacionesLiveData;
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

    public LiveData<ArrayList<String>> obtieneEstadoCivilPacientesEnum() {
        String url = Constantes.url_part + "estado_civil_enum.php";
        peticionesJson.getJsonObjectRequest(url, new PeticionesJson.MyJsonObjectResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<String> estadoCivilArrayList = new ArrayList<>();
                    JSONArray estadoCivilJsonArray = response.getJSONArray("estado_civil");

                    // Itera sobre los valores ENUM
                    for (int i = 0; i < estadoCivilJsonArray.length(); i++) {
                        String enumEstadoCivil = estadoCivilJsonArray.getString(i);
                        //Añadimos el valor a nuestro arrayList
                        estadoCivilArrayList.add(enumEstadoCivil);
                    }

                    // Actualiza el LiveData con la nueva lista
                    if (!estadoCivilArrayList.isEmpty()) {
                        listaSexoLiveData.postValue(new ArrayList<>(estadoCivilArrayList));
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

}
