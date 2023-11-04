package com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory <T extends ViewModel> implements ViewModelProvider.Factory {
    //Instancia de ViewModelArgs, que se utiliza para pasar argumentos necesarios a los ViewModels creados
    // por esta fábrica.
    private ViewModelArgs viewModelArgs;

    //La clase tiene un constructor que toma un objeto ViewModelArgs como parámetro. Esto permite que
    // la fábrica tenga acceso a los argumentos necesarios para construir ViewModels personalizados.
    public ViewModelFactory(ViewModelArgs viewModelArgs) {
        this.viewModelArgs = viewModelArgs;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass == ConsultasYRutinasDiariasViewModel.class) {
            return (T) new ConsultasYRutinasDiariasViewModel(viewModelArgs);
        }
        // Agregar más verificaciones y creación de ViewModel según sea necesario
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
