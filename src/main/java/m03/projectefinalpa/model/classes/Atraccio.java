
package m03.projectefinalpa.model.classes;

import java.util.ArrayList;

/**
 *
 * @author joanm
 */
public class Atraccio  extends ZonaTrabajo{
   
    private String descripcion;
    private String tipo;
    private double alturaminima;

    public Atraccio(int id, String nombre, String descripcion, String tipo, double alturaminima, String ubicacion ) {
        super(id, nombre, ubicacion);
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.alturaminima = alturaminima;
    }
                      
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getAlturaminima() {
        return alturaminima;
    }

    public void setAlturaminima(double alturaminima) {
        this.alturaminima = alturaminima;
    }

    @Override
    public String toString() {
        return super.toString();
    }
 
    

}