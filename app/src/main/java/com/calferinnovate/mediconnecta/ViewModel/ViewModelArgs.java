package com.calferinnovate.mediconnecta.ViewModel;

import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;

import java.io.Serializable;

/**
 * Interfaz que proporciona métodos para obtener instancias de PeticionesJson Y ClaseGlobal a los ViewModel.
 * Facilita la inyección de dependencias en los ViewModels.
 */
public interface ViewModelArgs extends Serializable {

    /**
     * Obtiene instancia de PeticionesJson.
     * @return instancia de PeticionesJson usada para realizar peticiones HTTP.
     */
    PeticionesJson getPeticionesJson();

    /**
     * Obtiene instancia de ClaseGlobal
     * @return instancia de ClaseGlobal contiene datos compartidos en la aplicación.
     */
    ClaseGlobal getClaseGlobal();
}
