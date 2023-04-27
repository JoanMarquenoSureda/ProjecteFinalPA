/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa.model.classes;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


/**
 *
 * @author joanm
 */
public class Horari {

    private int id;
    private LocalDateTime fecha_inici;
    private LocalDateTime fecha_fin;
    private int idAtraccion;
    private int idRestaurante;
    private ZonaTrabajo zona;
    private ArrayList<Empleados> Empleado;

    public Horari(LocalDateTime fecha_inici, LocalDateTime fecha_fin, int idAtraccion, int idRestaurante) {
        this.fecha_inici = fecha_inici;
        this.fecha_fin = fecha_fin;
        this.idAtraccion = idAtraccion;
        this.idRestaurante = idRestaurante;
    }

    public Horari(int id, LocalDateTime fecha_inici, LocalDateTime fecha_fin) {
        this.id = id;
        this.fecha_inici = fecha_inici;
        this.fecha_fin = fecha_fin;
        this.Empleado = new ArrayList<>();

    }

    public Horari(int id, LocalDateTime fecha_inici, LocalDateTime fecha_fin, ZonaTrabajo zona) {
        this.id = id;
        this.fecha_inici = fecha_inici;
        this.fecha_fin = fecha_fin;
        this.zona = zona;
    }

    public Horari(Date fecha_inici, Date fecha_fin, int idAtraccion, int idRestaurante) {
        this.fecha_inici = fecha_inici.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.fecha_fin = fecha_fin.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.idAtraccion = idAtraccion;
        this.idRestaurante = idRestaurante;
    }

    public Horari(int id, Date fecha_inici, Date fecha_fin, ZonaTrabajo zona) {
        this.id = id;
        this.fecha_inici = fecha_inici.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.fecha_fin = fecha_fin.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.zona = zona;
    }


    public static java.util.Date convertToDate(LocalDateTime dateTimeToConvert) {
        return java.util.Date.from(dateTimeToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }

    public LocalDateTime getFecha_inici() {
        return fecha_inici;
    }

    public void setFecha_inici(LocalDateTime fecha_inici) {
        this.fecha_inici = fecha_inici;
    }

    public LocalDateTime getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(LocalDateTime fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public int getIdAtraccion() {
        return idAtraccion;
    }

    public void setIdAtraccion(int idAtraccion) {
        this.idAtraccion = idAtraccion;
    }

    public int getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(int idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ZonaTrabajo getZona() {
        return zona;
    }

    public void setZona(ZonaTrabajo zona) {
        this.zona = zona;
    }

    public ArrayList<Empleados> getEmpleado() {
        return Empleado;
    }

    public void setEmpleado(ArrayList<Empleados> Empleado) {
        this.Empleado = Empleado;
    }

    public void añadirEmpleado(Empleados empleado) {

        if (!this.Empleado.contains(empleado)) {
            this.Empleado.add(empleado);
        }

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.id;
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
        final Horari other = (Horari) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {

        String fecha_inicio = convertirHoras(fecha_inici);
        String data = convertirDies(fecha_inici);
        String fecha_final = convertirHoras(fecha_fin);

        String result;

        if (Empleado != null && !Empleado.isEmpty()) {

            String empleados = "";
            for (int i = 0; i < Empleado.size(); i++) {
                empleados += Empleado.get(i).toString();
                if (i != Empleado.size() - 1) {
                    empleados += ", ";
                }
            }
            result = data + " " + fecha_inicio + "-" + fecha_final + " Assignat: " + empleados;
        } else {
            result = data + " " + fecha_inicio + "-" + fecha_final;
        }
        return result;
    }
    
    public String convertirHoras(LocalDateTime fecha){
        String hores;
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
         hores = fecha.format(formatter);
        
        return hores;
    }
    
     public String convertirDies(LocalDateTime fecha){
        String dies;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dies = fecha.format(formatter);
        
        return dies;
    }

}
