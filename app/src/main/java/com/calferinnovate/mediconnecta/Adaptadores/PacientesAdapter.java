package com.calferinnovate.mediconnecta.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adaptador utilizado para poblar el RecyclerView de la lista de pacientes pertenecientes a una unidad.
 */
public class PacientesAdapter extends RecyclerView.Adapter<PacientesAdapter.PacientesViewHolder> {

    /**
     * Interfaz para que el fragmento implemente el listener y poder capturar el evento fuera del adaptador
     */
    public interface ItemClickListener {
        void onClick(int position);
    }

    private final ArrayList<Pacientes> listaPacientes;
    ArrayList<Pacientes> listaOriginal;
    private final Context mContext;
    private ItemClickListener clickListener;

    /**
     * Constructor del adaptador
     *
     * @param listaPacientes Lista de pacientes pertenecientes a una unidad
     * @param context        Contexto
     * @param clickListener  Escucha del click.
     */
    public PacientesAdapter(ArrayList<Pacientes> listaPacientes, Context context, ItemClickListener clickListener) {
        this.listaPacientes = listaPacientes;
        mContext = context;
        this.clickListener = clickListener;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(listaPacientes);
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
    public PacientesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout_residentes, parent, false);
        return new PacientesViewHolder(view);
    }

    /**
     * Método que reemplaza el contenido de la vista, es invocado por el layout manager.
     * Obtiene el elemento de listaPacientes en cierta posición y reemplaza el contenido de la vista con ese elemento.
     *
     * @param holder   El ViewHolder que debe actualizarse para representar el contenido del en la posición
     *                 dada en la lista.
     * @param position La posición del elemento dentro de lista del adaptador.
     */
    @Override
    public void onBindViewHolder(@NonNull PacientesViewHolder holder, int position) {
        Pacientes pacientes = listaPacientes.get(position);

        configuraFotoPacientes(holder, pacientes);
        configuraDatosYTamaño(holder, pacientes);


    }

    private void configuraFotoPacientes(PacientesViewHolder holder, Pacientes pacientes){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        int targetWidth = Math.min(screenWidth, 300); // Tamaño máximo
        int targetHeight = Math.min(screenHeight, 300); // Tamaño máximo


        Glide.with(mContext)
                .load(pacientes.getFoto())
                .circleCrop()
                .override(targetWidth, targetHeight)  // Aquí se establece el tamaño deseado
                .into(holder.fotoPacienteImageView);
    }

    private void configuraDatosYTamaño(PacientesViewHolder holder, Pacientes pacientes) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenHeight = displayMetrics.heightPixels;

        // Porcentaje del alto de la pantalla para el tamaño de texto
        float textPercentage = 0.02f; // Puedes ajustar este valor según tus necesidades

        int desiredTextSize = (int) (screenHeight * textPercentage);

        holder.nombreCompletoTextView.setText(pacientes.getNombre());
        holder.nombreCompletoTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        holder.apellidos.setText(pacientes.getApellidos());
        holder.apellidos.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        holder.habitacionTextView.setText("Habitación:" + " " + pacientes.getFkNumHabitacion());
        holder.habitacionTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);
    }






    /**
     * Método utilizado para filtrar la lista de pacientes cuando se hace una búsqueda con el SearchView.
     * Si el usuario no ha buscado nada se muestra la lista completa de pacientes.
     * En caso de que el usuario haya efectuado una búsqueda asignaremos a una nueva lista la lista de pacientes,
     * buscaremos por nombre de paciente y lo transformaremos a minúsculas para que no haya sensibilidad.
     * Se limpia la lista original de pacientes y se añaden los pacientes pertenecientes a la búsqueda.
     *
     * @param busqueda String de búsqueda proporcionado por el usuario.
     */
    public void filtrado(String busqueda) {
        int longitud = busqueda.length();
        if (longitud == 0) {
            listaPacientes.clear();
            listaPacientes.addAll(listaOriginal);
        } else {
            List<Pacientes> collection = listaPacientes.stream()
                    .filter(i -> i.getNombre().toLowerCase().contains(busqueda.toLowerCase()))
                    .collect(Collectors.toList());
            listaPacientes.clear();
            listaPacientes.addAll(collection);
        }
        notifyDataSetChanged();
    }

    /**
     * Tamaño de la lista de pacientes
     *
     * @return Tamaño de la lista.
     */
    @Override
    public int getItemCount() {
        return listaPacientes.size();
    }


    /**
     * Proporciona una referencia al tipo de vistas que se están utilizando.
     * Configura el botón para seleccionar un paciente. Esto propaga el evento hacia afuera, así podemos capturarlo
     * en el punto que queramos de nuestra aplicación
     */
    public class PacientesViewHolder extends RecyclerView.ViewHolder {
        private final ImageView fotoPacienteImageView;
        private final TextView nombreCompletoTextView;
        private final TextView habitacionTextView;
        private final TextView apellidos;


        public PacientesViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoPacienteImageView = itemView.findViewById(R.id.idResidenteFoto);
            nombreCompletoTextView = itemView.findViewById(R.id.idNombreCompleto);
            habitacionTextView = itemView.findViewById(R.id.idHabitacion);
            apellidos = itemView.findViewById(R.id.idApellidosCompletos);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(getAdapterPosition());
                }
            });

        }


    }

    /**
     * Este metodo se utiliza desde el fragmento que captura el evento de clic de los items
     */
    public void setOnClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }


}
