package com.calferinnovate.mediconnecta.clases;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PacientesAgrupadosRutinas {
   private ArrayList<Rutinas> listRutinas;

   public PacientesAgrupadosRutinas(){
       listRutinas = new ArrayList<>();
   }

    public ArrayList<Rutinas> getListRutinas() {
        return listRutinas;
    }

    public void setListRutinas(ArrayList<Rutinas> listRutinas) {
        this.listRutinas = listRutinas;
    }
}
