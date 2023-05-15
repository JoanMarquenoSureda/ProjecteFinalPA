package m03.projectefinalpa.model.classes;

import java.util.ArrayList;

public class ZonaTrabajo {

    private String nombre;
    private String ubicacion;
    private ArrayList<Horari> listaHorarios;

    public ZonaTrabajo(String nombre, String ubicacion) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
      
    }
    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public ArrayList<Horari> getListaHorarios() {
        return listaHorarios;
    }

    public void setListaHorarios(ArrayList<Horari> listaHorarios) {
        this.listaHorarios = listaHorarios;
    }
    

    @Override
    public String toString() {

        return " (" + nombre + " en " + ubicacion+")";
    }

}
