package m03.projectefinalpa.model.classes;

import java.util.ArrayList;

public class ZonaTrabajo {

    private int id;
    private String nombre;
    private String ubicacion;
    private ArrayList<Horari> listaHorarios;

    public ZonaTrabajo(int id, String nombre, String ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.listaHorarios = new ArrayList<>();
    }

    public ZonaTrabajo(String nombre, String ubicacion) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
      
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
