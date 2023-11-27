package com.calferinnovate.mediconnecta.View.Home.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.calferinnovate.mediconnecta.Adaptadores.CaidasAdapter;
import com.calferinnovate.mediconnecta.Adaptadores.ParteAdapter;
import com.calferinnovate.mediconnecta.Model.Caidas;
import com.calferinnovate.mediconnecta.Model.Parte;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.ViewModel.ParteGeneralViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgsJson;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * ParteGeneralFragment es una fragmento que muestra dos TableLayout con los partes y partes de caídas
 * dado un rango de fechas.
 */
public class ParteGeneralFragment extends Fragment implements IOnBackPressed {
    private EditText fechasSeleccionadas;
    private Button btnFechas;
    private ParteGeneralViewModel parteGeneralViewModel;
    private PeticionesJson peticionesJson;
    private String fechaInicio, fechaFin;
    private ParteAdapter parteAdapter;
    private CaidasAdapter caidasAdapter;
    private TableLayout tableLayoutPartes, tableLayoutCaidas;
    private ArrayList<Parte> listaPartes;
    private ArrayList<Caidas> listaCaidas;

    /**
     * Método llamado cuando se crea la vista del fragmento.
     * Infla el diseño de la UI desde el archivo XML fragment_parte_general.xml.
     * Establece el título del fragmento.
     *
     * @param inflater           inflador utilizado para inflar el diseño de la UI.
     * @param container          Contenedor que contiene la vista del fragmento
     * @param savedInstanceState Estado de guardado de la instancia del fragmento
     * @return vista Es la vista inflada del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parte_general, container, false);
        enlazaRecursos(view);
        inicializaViewModel();
        getActivity().setTitle("Parte general");
        return view;
    }

    /**
     * Método que enlaza los recursos de la UI con las variables miembro.
     *
     * @param view La vista inflada.
     */
    public void enlazaRecursos(View view) {
        fechasSeleccionadas = view.findViewById(R.id.fechaSeleccionadaEditText);
        btnFechas = view.findViewById(R.id.buttonFechas);
        tableLayoutPartes = view.findViewById(R.id.tableLayoutParte);
        tableLayoutCaidas = view.findViewById(R.id.tableLayoutCaidas);
    }


    /**
     * Método que configura el ViewModel ParteGeneralViewModel mediante la creación de un ViewModelFactory
     * que proporciona una instancia de Peticiones Json al ViewModel.
     */
    public void inicializaViewModel() {
        ViewModelArgsJson viewModelArgs = () -> peticionesJson = new PeticionesJson(requireContext());

        ViewModelFactory<ParteGeneralViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        parteGeneralViewModel = new ViewModelProvider(requireActivity(), factory).get(ParteGeneralViewModel.class);
    }

    /**
     * Método llamado cuando la vista ya ha sido creada.
     * Llama a instanciaDateRangePicker() que establece el dialog del Calendario.
     *
     * @param view               Vista retornada por el inflador.
     * @param savedInstanceState Si no es nulo, este fragmento será reconstruido a partir de un
     *                           estado anterior guardado.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        instanciaDateRangePicker();

    }

    /**
     * Método que crea una instancia del DateRangePicker de Material Design, define del título de este
     * y crea una instancia del MaterialDatePicker
     */
    public void instanciaDateRangePicker() {
        MaterialDatePicker.Builder<Pair<Long, Long>> materialFechaBuilder = MaterialDatePicker.Builder.dateRangePicker();
        materialFechaBuilder.setTitleText("Selecciona las fechas");
        final MaterialDatePicker<Pair<Long, Long>> materialDatePicker = materialFechaBuilder.build();

        muestraDatePicker(materialDatePicker);
        obtieneFechasSeleccionadas(materialDatePicker);
    }

    /**
     * Método que establece la escucha del btnFechas, al hacer click se abrirá el DatePicker, interacturá
     * con los fragmentos asociados al tag MATERIAL_DATE_PICKER para obtener los errores en el Logcat.
     *
     * @param materialDatePicker DatePicker
     */
    public void muestraDatePicker(MaterialDatePicker<Pair<Long, Long>> materialDatePicker) {
        btnFechas.setOnClickListener(v -> {
            materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });
    }

    /**
     * Método que maneja el click del botón de selector de fecha del MaterialDatePicker, obtiene las fechas
     * ,actualiza el EditText con el rango de fechas, asigna la fecha inicio y fecha fin a las variables miembro
     * y actualiza los TableLayout de partes y caídas.
     *
     * @param materialDatePicker DatePicker
     */
    public void obtieneFechasSeleccionadas(MaterialDatePicker<Pair<Long, Long>> materialDatePicker) {
        materialDatePicker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>) selection -> {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

            fechasSeleccionadas.setText(materialDatePicker.getHeaderText());

            Date dateInicio = new Date(selection.first);
            fechaInicio = formato.format(dateInicio);
            Date dateFin = new Date(selection.second);
            fechaFin = formato.format(dateFin);

            rellenaPartes();
            rellenaCaidas();
        });
    }

    /**
     * Método que llama al método obtieneListaPartesFecha del ParteGeneralViewModel pasando fecha inicio y fin,
     * configura el adaptador ParteAdapter y llama a creaTablaPartes para actualizar el TableLayout.
     */
    public void rellenaPartes() {
        parteGeneralViewModel.obtieneListaPartesFecha(fechaInicio, fechaFin).observe(getViewLifecycleOwner(), partes -> {
            listaPartes = partes;
            parteAdapter = new ParteAdapter(requireContext(), listaPartes);
            tableLayoutPartes.removeAllViews();

            creaTablaPartes();

            parteAdapter.notifyDataSetChanged();
        });
    }

    /**
     * Método que crea dinámicamente las filas de la tabla tableLayoutPartes. Infla el header y posteriormente
     * las filas.
     */
    public void creaTablaPartes() {
        View headerViewParte = parteAdapter.inflaElHeader(tableLayoutPartes);
        tableLayoutPartes.addView(headerViewParte);

        for (Parte parte : listaPartes) {
            View rowView = parteAdapter.getView(listaPartes.indexOf(parte), null, tableLayoutPartes);
            tableLayoutPartes.addView(rowView);
        }

        parteAdapter.notifyDataSetChanged();
    }

    /**
     * Método que llama al método obtieneCaidasPeriodo del ParteGeneralViewModel pasando fecha inicio y fin,
     * configura el adaptador CaidasAdapter y llama a creaTablaCaidas para actualizar el TableLayout.
     */
    public void rellenaCaidas() {
        parteGeneralViewModel.obtieneCaidasPeriodo(fechaInicio, fechaFin).observe(getViewLifecycleOwner(), caidas -> {
            listaCaidas = caidas;
            caidasAdapter = new CaidasAdapter(requireContext(), listaCaidas);
            tableLayoutCaidas.removeAllViews();
            creaTablaCaidas();
        });
    }

    /**
     * Método que crea dinámicamente las filas de la tabla tableLayoutCaidas. Infla el header y posteriormente
     * las filas.
     */
    public void creaTablaCaidas() {
        View headerView = caidasAdapter.inflaElHeader(tableLayoutCaidas);
        tableLayoutCaidas.addView(headerView);

        for (Caidas caida : listaCaidas) {
            View rowView = caidasAdapter.getView(listaCaidas.indexOf(caida), null, tableLayoutCaidas);
            tableLayoutCaidas.addView(rowView);
        }

        caidasAdapter.notifyDataSetChanged();
    }


    /**
     * Método que agrega la lógica específica del fragmento para manejar el restroceso.
     * Al presionar back volvería al HomeFragment.
     *
     * @return true si el fragmento manejar el retroceso, false en caso contrario.
     */
    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        return true;
    }
}