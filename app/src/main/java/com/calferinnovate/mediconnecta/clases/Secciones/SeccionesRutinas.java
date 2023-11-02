package com.calferinnovate.mediconnecta.clases.Secciones;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.PacientesAgrupadosRutinas;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class SeccionesRutinas extends Section {

    private String tituloRutina;
    private PacientesAgrupadosRutinas pacientesAgrupadosRutinas;

    //Llamamos al constructor con el layout de los items y del header
    public SeccionesRutinas(){
        super(SectionParameters.builder()
                .itemResourceId(R.layout.items_recyclerview_rutinas)
                .headerResourceId(R.layout.section_rutinas)
                .build());


    }
    @Override
    public int getContentItemsTotal() {
        return pacientesAgrupadosRutinas.getListRutinas().size(); // Numero de items de esa seccion
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // Devuelve una instancia customizada del VIewHolder para los items de esta seccion
        return new RutinasViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final RutinasViewHolder rutinasHolder = (RutinasViewHolder) holder;
        // bind your view here
        rutinasHolder.datosPersonales.setText(pacientesAgrupadosRutinas.getListRutinas().get(position+1).getNombre()+" "+pacientesAgrupadosRutinas.getListRutinas().get(position+1).getApellidos());
        rutinasHolder.hora.setText(pacientesAgrupadosRutinas.getListRutinas().get(position+1).getHoraRutina());

    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderRutinasViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(final RecyclerView.ViewHolder holder) {
        final HeaderRutinasViewHolder headerHolder = (HeaderRutinasViewHolder) holder;
        int count = 0;
        headerHolder.sectionText.setText(pacientesAgrupadosRutinas.getListRutinas().get(count).getDiario());
        count++;
    }
}
