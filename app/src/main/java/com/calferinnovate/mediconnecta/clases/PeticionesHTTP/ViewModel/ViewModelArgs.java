package com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel;

import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.PeticionesJson;

import java.io.Serializable;

public interface ViewModelArgs extends Serializable {
    PeticionesJson getPeticionesJson();

    ClaseGlobal getClaseGlobal();
}
