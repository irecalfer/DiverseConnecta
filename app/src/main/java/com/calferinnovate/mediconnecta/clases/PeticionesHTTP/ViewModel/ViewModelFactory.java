package com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory <T extends ViewModel> implements ViewModelProvider.Factory {
    private ViewModelArgs viewModelArgs;

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
