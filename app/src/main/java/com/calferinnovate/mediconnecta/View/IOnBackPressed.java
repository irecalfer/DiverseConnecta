package com.calferinnovate.mediconnecta.View;

/**
 * Interfaz definida para implmentar la lógica de retroceso en los fragmentos.
 */
public interface IOnBackPressed {

        /**
         * Método que agrega la lógica específica del fragmento para manejar el restroceso.
         * @return true si el fragmento maneja el retroceso, false en caso contrario.
         */
        boolean onBackPressed();

}