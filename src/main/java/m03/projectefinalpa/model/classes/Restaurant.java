/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa.model.classes;

/**
 *
 * @author joanm
 */
public class Restaurant extends ZonaTrabajo{
   
    private String tipoComida;
    private String descripcion;
    
    
    public Restaurant(String nombre, String ubicacion, String tipoComida, String descripcion) {
        super(nombre, ubicacion);
        this.tipoComida = tipoComida;
        this.descripcion = descripcion;
    }


    public String getTipoComida() {
        return tipoComida;
    }

    public void setTipoComida(String tipoComida) {
        this.tipoComida = tipoComida;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
}