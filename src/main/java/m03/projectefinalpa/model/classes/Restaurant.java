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

    public Restaurant(int id, String nombre, String ubicacion, String tipoComida) {
        super(id, nombre, ubicacion);
        this.tipoComida = tipoComida;
    }
    
    

    public String getTipoComida() {
        return tipoComida;
    }

    public void setTipoComida(String tipoComida) {
        this.tipoComida = tipoComida;
    }

    @Override
    public String toString() {
        return super.toString() + "RESTAURANTE";
    }
    


    
}