
package m03.projectefinalpa.model.classes;

import java.util.ArrayList;
import java.util.Objects;
import javafx.scene.image.Image;


public class EmpleadosClass {
    
    private String dni;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private String categoria;
    private Image fotoImage;
 
    private ArrayList<Horari> listaHorario;


    public EmpleadosClass(String dni, String nombre, String direccion, String telefono, String email, String categoria, Image fotoImage) {
        this.dni = dni;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.categoria = categoria;
        this.fotoImage = fotoImage;
    }
  
    public EmpleadosClass(String dni, String nombre, String direccion, String telefono, String email, String categoria) {
        this.dni = dni;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.categoria = categoria;
    }

    public EmpleadosClass(String dni, String nombre, String direccion, String telefono, String email, Image fotoImage) {
       this.dni = dni;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.fotoImage = fotoImage;
    }
     
    
    public EmpleadosClass(String dni, String nombre) {
       this.dni = dni;
        this.nombre = nombre;
    }
    
     public EmpleadosClass(String dni, String nombre, String email) {
       this.dni = dni;
       this.nombre = nombre;
       this.email = email;
    }

    public String getDni() {
        return dni;
    }

    
    public void setDni(String dni) {
        this.dni = dni;
    }

    public Image getFotoImage() {
        return fotoImage;
    }

    public void setFotoImage(Image fotoImage) {
        this.fotoImage = fotoImage;
    }
     
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }
    

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }


    public ArrayList<Horari> getListaHorario() {
        return listaHorario;
    }

    public void setListaHorario(ArrayList<Horari> listaHorario) {
        this.listaHorario = listaHorario;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.dni);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EmpleadosClass other = (EmpleadosClass) obj;
        return Objects.equals(this.dni, other.dni);
    }


       
 
    @Override
    public String toString() {
        return  nombre;
    }
    
    
    
    
    
}
