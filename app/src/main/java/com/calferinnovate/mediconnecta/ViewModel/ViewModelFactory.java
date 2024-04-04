package com.calferinnovate.mediconnecta.ViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Clase usada como fábrica para crear instancias de ViewModels con argumentos personalizados.
 */
public class ViewModelFactory<T extends ViewModel> implements ViewModelProvider.Factory {
    private ViewModelArgs viewModelArgs;
    private ViewModelArgsJson viewModelArgsJson;


    /**
     * Constructor que permite que el factory tenga acceso a los argumentos necesarios para construir
     * ViewModels personalizados.
     *
     * @param viewModelArgs objeto ViewModelArgs que contiene instancias de PeticionesJson y ClaseGlobal
     *                      se utiliza para pasar argumentos necesarios a los ViewModels creados
     *                      por esta fábrica.
     */
    public ViewModelFactory(ViewModelArgs viewModelArgs) {
        this.viewModelArgs = viewModelArgs;
    }

    /**
     * Constructor que permite que el factory tenga acceso a los argumentos necesarios para construir
     * ViewModels personalizados.
     *
     * @param viewModelArgsJson objeto ViewModelArgs que contiene instancia de PeticionesJson. Se utiliza
     *                          para pasar argumentos necesarios a los ViewModels creados
     *                          por esta fábrica.
     */
    public ViewModelFactory(ViewModelArgsJson viewModelArgsJson) {
        this.viewModelArgsJson = viewModelArgsJson;
    }

    /**
     * Método para crear instancias de ViewModel según el tipo de clase.
     *
     * @param modelClass Tipo de ViewModel que se desea crear.
     * @param <T>        Tipo genérico del ViewModel.
     * @return Instancia del ViewModel correspondiente al tipo de clase proporcionada.
     */
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass == SharedAlumnosViewModel.class) {
            return (T) new SharedAlumnosViewModel(viewModelArgs);
        } else if (modelClass == SesionViewModel.class) {
            return (T) new SesionViewModel(viewModelArgs);
        } else if (modelClass == AvisosViewModel.class) {
            return (T) new AvisosViewModel(viewModelArgsJson);
        } else if (modelClass == PaeViewModel.class) {
            return (T) new PaeViewModel(viewModelArgs);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
