package com.calferinnovate.mediconnecta.View.Home.Fragments.Residentes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;

public class PdfViewerFragment extends Fragment implements IOnBackPressed {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pdf_viewer, container, false);
        PDFView pdfView = view.findViewById(R.id.pdfView);
        obtieneBytesPDF(pdfView);


        return view;
    }


    public void obtieneBytesPDF(PDFView pdfView){
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
    }

    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().popBackStack();
        return true;
    }
}