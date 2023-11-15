package com.calferinnovate.mediconnecta.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.Model.ContactoFamiliares;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class FamiliaresAdapter extends RecyclerView.Adapter<FamiliaresAdapter.FamiliaresViewHolder> {

    private ArrayList<ContactoFamiliares> listaFamiliares;
    private Context mContext;

    public FamiliaresAdapter(ArrayList<ContactoFamiliares> listaFamiliares, Context mContext){
        this.listaFamiliares = listaFamiliares;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public FamiliaresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_recyclerview_contacto_familiares, parent, false);
        return new FamiliaresViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull FamiliaresViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        ContactoFamiliares familiar = listaFamiliares.get(position);
        //Setear campos
        holder.nombreFamiliar.setText(familiar.getNombre());
        holder.apellidosFamiliar.setText(familiar.getApellidos());
        holder.telefono1Familiar.setText(String.valueOf(familiar.getTelefono1()));
        holder.telefono2Familiar.setText(String.valueOf(familiar.getTelefono2()));
        holder.dniFamiliar.setText(familiar.getDniFamiliar());
    }

    @Override
    public int getItemCount() {
        return listaFamiliares.size();
    }

    public class FamiliaresViewHolder extends RecyclerView.ViewHolder {
        private final TextInputEditText nombreFamiliar;
        private final TextInputEditText apellidosFamiliar;
        private final TextInputEditText telefono1Familiar;
        private final TextInputEditText telefono2Familiar;
        private final TextInputEditText dniFamiliar;

        public FamiliaresViewHolder(View itemView){
            super(itemView);
            nombreFamiliar = itemView.findViewById(R.id.nombreFamiliar);
            apellidosFamiliar = itemView.findViewById(R.id.apellidosFamiliar);
            telefono1Familiar = itemView.findViewById(R.id.telefono1);
            telefono2Familiar = itemView.findViewById(R.id.telefono2);
            dniFamiliar = itemView.findViewById(R.id.dniFamiliar);

        }
    }



}
