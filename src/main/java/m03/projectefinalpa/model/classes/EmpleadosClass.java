/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa.model.classes;

import java.awt.Image;
import java.sql.Blob;
import java.util.ArrayList;

/**
 *
 * @author User
 */
public class EmpleadosClass {
    
    private int id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private double decimal;
    private String categoria;
    private int jornada;
    private Blob foto;
    private ArrayList<Horari> listaHorario;

    public EmpleadosClass(int id, String nombre, String direccion, String telefono, String email, double decimal, String categoria, int jornada) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.decimal = decimal;
        this.categoria = categoria;
        this.jornada = jornada;
        this.listaHorario = new ArrayList<>();
    }

    public EmpleadosClass(String nombre, String direccion, String telefono, String email, Blob foto) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.foto = foto;
    }
    
    

    public EmpleadosClass(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    
     public EmpleadosClass(int id, String nombre, String email) {
       this.id = id;
       this.nombre = nombre;
       this.email = email;
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

    public Blob getFoto() {
        return foto;
    }

    public void setFoto(Blob foto) {
        this.foto = foto;
    }
    

    public double getDecimal() {
        return decimal;
    }

    public void setDecimal(double decimal) {
        this.decimal = decimal;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getJornada() {
        return jornada;
    }

    public void setJornada(int jornada) {
        this.jornada = jornada;
    }

    public ArrayList<Horari> getListaHorario() {
        return listaHorario;
    }

    public void setListaHorario(ArrayList<Horari> listaHorario) {
        this.listaHorario = listaHorario;
    }

 
    
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.id;
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
        return this.id == other.id;
    }
    
    

    @Override
    public String toString() {
        return  nombre;
    }
    
    
    
    
    
}
