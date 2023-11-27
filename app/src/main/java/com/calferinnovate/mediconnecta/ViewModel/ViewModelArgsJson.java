package com.calferinnovate.mediconnecta.ViewModel;

import com.calferinnovate.mediconnecta.Model.PeticionesJson;

import java.io.Serializable;

/**
 * Interfaz que proporciona método para obtener instancias de PeticionesJson al ViewModel.
 * Facilita la inyección de dependencias en los ViewModels.
 */
public interface ViewModelArgsJson extends Serializable {

    /**
     * Obtiene instancia de PeticionesJson.
     * @return instancia de PeticionesJson usada para realizar peticiones HTTP.
     */
    PeticionesJson getPeticionesJson();
}
