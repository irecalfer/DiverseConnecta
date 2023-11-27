package com.calferinnovate.mediconnecta.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calferinnovate.mediconnecta.Model.ContactoFamiliares;
import com.calferinnovate.mediconnecta.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

/**
 * Adaptador que pobla el RecyclerView con los datos de contacto de los familiares de los pacientes.
 */
public class FamiliaresAdapter extends RecyclerView.Adapter<FamiliaresAdapter.FamiliaresViewHolder> {

    private final ArrayList<ContactoFamiliares> listaFamiliares;
    private final Context mContext;

    /**
     * Constructor del adaptador
     *
     * @param listaFamiliares lista de familiares del paciente.
     * @param mContext        Contexto.
     */
    public FamiliaresAdapter(ArrayList<ContactoFamiliares> listaFamiliares, Context mContext) {
        this.listaFamiliares = listaFamiliares;
        this.mContext = mContext;
    }

    /**
     * Método utilizado para crear un ViewHolder nuevo, es invocado por el layout manager.
     * Crea una nueva vista, que define la UI del elemento de la lista.
     *
     * @param parent   El ViewGroup al que se añadirá la nueva View después de que se vincule a una posición de adaptador.
     * @param viewType El tipo de vista de la nueva Vista.
     * @return la vista creada.
     */
    @NonNull
    @Override
    public FamiliaresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_recyclerview_contacto_familiares, parent, false);
        return new FamiliaresViewHolder(view);
    }

    /**
     * Método que reemplaza el contenido de la vista, es invocado por el layout manager.
     * Obtiene el elemento de listaFamiliares en cierta posición y reemplaza el contenido de la vista con ese elemento.
     *
     * @param holder   El ViewHolder que debe actualizarse para representar el contenido del en la posición
     *                 dada en la lista.
     * @param position La posición del elemento dentro de lista del adaptador.
     */
    @Override
    public void onBindViewHolder(@NonNull FamiliaresViewHolder holder, int position) {
        ContactoFamiliares familiar = listaFamiliares.get(position);

        holder.nombreFamiliar.setText(familiar.getNombre());
        holder.apellidosFamiliar.setText(familiar.getApellidos());
        holder.telefono1Familiar.setText(String.valueOf(familiar.getTelefono1()));
        holder.telefono2Familiar.setText(String.valueOf(familiar.getTelefono2()));
        holder.dniFamiliar.setText(familiar.getDniFamiliar());
    }

    /**
     * Método que devuelve el tamaño de la lista.
     *
     * @return Tamaño de la lista.
     */
    @Override
    public int getItemCount() {
        return listaFamiliares.size();
    }

    /**
     * Proporciona una referencia al tipo de vistas que se están utilizando.
     */
    public class FamiliaresViewHolder extends RecyclerView.ViewHolder {
        private final TextInputEditText nombreFamiliar;
        private final TextInputEditText apellidosFamiliar;
        private final TextInputEditText telefono1Familiar;
        private final TextInputEditText telefono2Familiar;
        private final TextInputEditText dniFamiliar;

        public FamiliaresViewHolder(View itemView) {
            super(itemView);
            nombreFamiliar = itemView.findViewById(R.id.nombreFamiliar);
            apellidosFamiliar = itemView.findViewById(R.id.apellidosFamiliar);
            telefono1Familiar = itemView.findViewById(R.id.telefono1);
            telefono2Familiar = itemView.findViewById(R.id.telefono2);
            dniFamiliar = itemView.findViewById(R.id.dniFamiliar);

        }
    }


}
