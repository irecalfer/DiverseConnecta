package com.calferinnovate.mediconnecta.clases.Secciones;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calferinnovate.mediconnecta.R;

final class HeaderRutinasViewHolder extends RecyclerView.ViewHolder {

    final TextView sectionText;
    public HeaderRutinasViewHolder(@NonNull View itemView) {
        super(itemView);
        sectionText = itemView.findViewById(R.id.section_text);
    }
}
