package m03.projectefinalpa.model.classes;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Horari {

    private int id;
    private LocalDateTime fecha_inici;
    private LocalDateTime fecha_fin;
    private String nombreAtr;
    private String nombreRes;
    private ZonaTrabajo zona;// he trabajado tambien con esta variable, para unificar el idAtraccion e idRestaurante en la clase "GestioDadesEmpleados", ya
                             // que con la consulta, podia controlar los nulos y que solo devolviera uno de los dos valores. 
    private ArrayList<EmpleadosClass> Empleado;

    public Horari(LocalDateTime fecha_inici, LocalDateTime fecha_fin, String nombreAtr, String nombreRes) {
        this.fecha_inici = fecha_inici;
        this.fecha_fin = fecha_fin;
        this.nombreAtr = nombreAtr;
        this.nombreRes = nombreRes;
    }
    
    

    public Horari(LocalDateTime fecha_inici, LocalDateTime fecha_fin, ZonaTrabajo zona) {
        this.fecha_inici = fecha_inici;
        this.fecha_fin = fecha_fin;
        this.zona = zona;
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

    public ArrayList<EmpleadosClass> getEmpleado() {
        return Empleado;
    }

    public String getNombreAtr() {
        return nombreAtr;
    }

    public void setNombreAtr(String nombreAtr) {
        this.nombreAtr = nombreAtr;
    }

    public String getNombreRes() {
        return nombreRes;
    }

    public void setNombreRes(String nombreRes) {
        this.nombreRes = nombreRes;
    }
    
    

    public void setEmpleado(ArrayList<EmpleadosClass> Empleado) {
        this.Empleado = Empleado;
    }

    public void añadirEmpleado(EmpleadosClass empleado) {

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
        
        // convertimos las fechas en texto 

        String fecha_inicio = convertirHoras(fecha_inici);
        String data = convertirDies(fecha_inici);
        String fecha_final = convertirHoras(fecha_fin);

        String result;

        // si el empleado es diferente a nullo o vacio (he tenido que comprobar las dos, ya que al comprobar solo uno daba errores)
        if (Empleado != null && !Empleado.isEmpty()) {

            // revisamos el array y ponemos todos los nombres de los empleados separados por coma
            String empleados = "";
            for (int i = 0; i < Empleado.size(); i++) {
                empleados += Empleado.get(i).toString();
                if (i != Empleado.size() - 1) {
                    empleados += ", ";
                }
            }
            // añadimos las fechas y los empleados assignados. 
            result = data + " " + fecha_inicio + "-" + fecha_final + " Assignat: " + empleados;
        } else {
            // en caso de que no haya empleados asignados hay dos posibilidades
            if (this.zona != null) {
                
                // si hay zona asignada e imprimimos su zonas
                result = data + " " + fecha_inicio + "-" + fecha_final + this.zona.toString();
                
                // cuando no hay zona asignada, no la incluimos (eso es para evitar la variable zona que se utiliza en en unas vistas y otras no)
            } else {
                result = data + " " + fecha_inicio + "-" + fecha_final;

            }
        }

        return result;
    }
//metodos para convertir las horas en texto y dias en texto. 
    public String convertirHoras(LocalDateTime fecha) {
        String hores;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        hores = fecha.format(formatter);

        return hores;
    }

    public String convertirDies(LocalDateTime fecha) {
        String dies;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dies = fecha.format(formatter);

        return dies;
    }

}
