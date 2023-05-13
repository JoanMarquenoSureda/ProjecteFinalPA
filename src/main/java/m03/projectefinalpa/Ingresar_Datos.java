/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa;

import java.io.IOException;
import javafx.fxml.FXML;

/**
 *
 * @author User
 */
public class Ingresar_Datos {

    
    
    @FXML
    private void cambiarPantallaAsignar() throws IOException {
        App.setRoot("Asignar");
    }

    @FXML
    private void cambiarPantallaVisualizar() throws IOException {
        App.setRoot("Visualizar");
    }

    @FXML
    private void cambiarPantallaEmpleados() throws IOException {
        App.setRoot("Empleados");
    }
     @FXML
    private void cambiarPantallaCrear() throws IOException {
        App.setRoot("Crear");
    }
    
    @FXML
    private void cambiarPantallaCrearAtracciones() throws IOException {
        App.setRoot("CrearAtracciones");
    }
    
     @FXML
    private void cambiarPantallaCrearEmpleados() throws IOException {
        App.setRoot("CrearEmpleados");
    }
    
     @FXML
    private void cambiarPantallaCrearRestaurantes() throws IOException {
        App.setRoot("CrearRestaurantes");
    }
    
}


