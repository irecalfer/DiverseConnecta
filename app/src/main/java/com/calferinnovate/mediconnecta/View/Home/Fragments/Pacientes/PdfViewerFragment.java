package com.calferinnovate.mediconnecta.View.Home.Fragments.Pacientes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;

/**
 * Fragmento encargado de mostrar los informes clínicos de los pacientes en PDF.
 */
public class PdfViewerFragment extends Fragment implements IOnBackPressed {

    /**
     * Método llamado cuando se crea la vista del fragmento.
     * Infla el diseño de la UI desde el archivo XML fragment_pdf_viewer.xml.
     * Enlaza el recurso PDFView con la variable pdfView.
     * Establece el título del fragmento.
     * Llama a obtieneBytesPDF() para obtener el PDF pasado como argumento.
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
        View view = inflater.inflate(R.layout.fragment_pdf_viewer, container, false);
        PDFView pdfView = view.findViewById(R.id.pdfView);
        getActivity().setTitle("Informe Clínico");
        obtieneBytesPDF(pdfView);


        return view;
    }

    /**
     * Obtiene el array de Bytes del PDF de los argumentos del fragmento y si no es null muestra el
     * PDF en el fragmento.
     *
     * @param pdfView Vista del PDF del informe.
     */
    public void obtieneBytesPDF(PDFView pdfView) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            byte[] pdfBytes = bundle.getByteArray("pdfInforme");

            if (pdfBytes != null) {
                pdfView.fromBytes(pdfBytes)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .pageFitPolicy(FitPolicy.WIDTH)
                        .load();
            }
        }
    }

    /**
     * Método que agrega la lógica específica del fragmento para manejar el restroceso.
     * Al presionar back volvería a ClínicaPacientesFragment del paciente seleccionado.
     *
     * @return true si el fragmento manejar el retroceso, false en caso contrario.
     */
    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().popBackStack();
        return true;
    }
}