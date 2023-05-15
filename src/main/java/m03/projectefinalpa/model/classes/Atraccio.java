
package m03.projectefinalpa.model.classes;

/**
 *
 * @author joanm
 */
public class Atraccio extends ZonaTrabajo {

    private String descripcion;
    private String tipo;
    private int alturaminima;

    public Atraccio(String nombre, String descripcion, String tipo, String ubicacion) {
        super(nombre, ubicacion);
        this.descripcion = descripcion;
        this.tipo = tipo;
        
    }

    public Atraccio(String nombre, String tipo, String ubicacion, int alturaminima, String descripcion) {
        super(nombre, ubicacion);
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

    public int getAlturaminima() {
        return alturaminima;
    }

    public void setAlturaminima(int alturaminima) {
        this.alturaminima = alturaminima;
    }
    

}