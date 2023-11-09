package com.calferinnovate.mediconnecta.Home.Fragments.Residentes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.Constantes;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;

public class PdfViewerFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pdf_viewerk, container, false);
        PDFView pdfView = view.findViewById(R.id.pdfView);

        // Obtén los bytes del PDF de los argumentos del fragmento
        Bundle bundle = getArguments();
        if (bundle != null) {
            byte[] pdfBytes = bundle.getByteArray("pdfInforme");

            // Cargar los bytes del PDF en PDFView
            if (pdfBytes != null) {
                pdfView.fromBytes(pdfBytes)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .pageFitPolicy(FitPolicy.WIDTH)
                .load();
            }
        }

        return view;
    }


}