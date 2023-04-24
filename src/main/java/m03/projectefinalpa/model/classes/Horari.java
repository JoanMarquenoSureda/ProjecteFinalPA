/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa.model.classes;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 *
 * @author joanm
 */
public class Horari {

    private LocalDateTime fecha_inici;
    private LocalDateTime fecha_fin;
    private int idAtraccion;
    private int idRestaurante;
    private Atraccio atraccio;
    private Restaurant restaurant;
   

    public Horari(LocalDateTime fecha_inici, LocalDateTime fecha_fin, int idAtraccion, int idRestaurante) {
        this.fecha_inici = fecha_inici;
        this.fecha_fin = fecha_fin;
        this.idAtraccion = idAtraccion;
        this.idRestaurante = idRestaurante;
    }

    public Horari(Date fecha_inici, Date fecha_fin, int idAtraccion, int idRestaurante) {
        this.fecha_inici = fecha_inici.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.fecha_fin = fecha_fin.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.idAtraccion = idAtraccion;
        this.idRestaurante = idRestaurante;
    }

    private LocalDateTime convertToLocalDateTime(java.util.Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
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

    @Override
    public String toString() {
        return "Horari{" + "fecha_inici=" + fecha_inici + ", fecha_fin=" + fecha_fin + ", idAtraccion=" + idAtraccion + ", idRestaurante=" + idRestaurante + '}';
    }

    

}
