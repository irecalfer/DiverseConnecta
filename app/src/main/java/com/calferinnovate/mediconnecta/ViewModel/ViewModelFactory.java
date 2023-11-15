package com.calferinnovate.mediconnecta.ViewModel;

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
        }else if (modelClass == SharedPacientesViewModel.class) {
            return (T) new SharedPacientesViewModel(viewModelArgs);
        }else if(modelClass == ParteGeneralViewModel.class){
            return (T) new ParteGeneralViewModel(viewModelArgs);
        }else if(modelClass == NormasViewModel.class){
            return (T) new NormasViewModel(viewModelArgs);
        }else if(modelClass == SesionViewModel.class){
            return (T) new SesionViewModel(viewModelArgs);
        }else if(modelClass == SeleccionUnidadViewModel.class){
            return (T) new SeleccionUnidadViewModel(viewModelArgs);
        }
        // Agregar más verificaciones y creación de ViewModel según sea necesario
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
