/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.GridPane;
import m03.projectefinalpa.model.classes.Atraccio;
import m03.projectefinalpa.model.Connexio;
import m03.projectefinalpa.model.GestioDades;
import m03.projectefinalpa.model.classes.Empleados;
import m03.projectefinalpa.model.classes.Horari;
import m03.projectefinalpa.model.classes.Restaurant;

public class Asignar {

    GestioDades gestioDades = new GestioDades();
    Connection conecta;
    Connexio connexio = new Connexio();

    @FXML
    RadioButton opcionAtraccion;
    @FXML
    RadioButton opcionRestaurante;

    @FXML
    ComboBox<String> desplegableZona = new ComboBox<>();
    @FXML
    DatePicker calendario;
    @FXML
    ListView listViewHorarios;
    @FXML
    ListView listViewEmpleados;

    Timestamp fechaHoraSQL;

    ObservableList<Horari> horariRestaurant = FXCollections.observableArrayList(); //llistem tots els horaris d'un restaurant
    ObservableList<Horari> horariAtraccio = FXCollections.observableArrayList();//llistem tots els horaris d'una atraccio
    ArrayList<String> llistaHoraris = new ArrayList<>();

    ObservableList<Atraccio> llistaAtraccions = gestioDades.llistaAtraccio(); //llistem totes les atraccions quan iniciem
    ObservableList<Restaurant> llistaRestaurant = gestioDades.llistaRestaurants();//llistem tots els restaurants quan iniciem
    ObservableList<Empleados> llistaEmpleatsSQL = gestioDades.llistaEmpleatsHoraris(); //llistem tots els empleats que son "Aprendiz" y "Trabajador"

    ObservableList<String> dadesHorarisSenseAssignar = FXCollections.observableArrayList(); // Llista on guardarem les dades del horaris del metode ToSring

    private LocalDate diaCalendario;
    int idZona;
    int indice;

    public void buscar() {

        listViewHorarios.setDisable(false);

        //para hacer seleccion multiple de empleados
        listViewEmpleados.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //comprobar si hay alguna zona seleccionada 
        if (desplegableZona.getSelectionModel().getSelectedIndex() >= 0 && calendario.getValue() != null) {

            //reiniciamos la lista de horarios y limpiamos los dos ListView
            llistaHoraris.clear();
            dadesHorarisSenseAssignar.clear();
            listViewEmpleados.getItems().clear();
            listViewHorarios.getItems().clear();
            horariAtraccio.clear();
            horariRestaurant.clear();

            //miramos el indice de la zona seleccionada
            indice = desplegableZona.getSelectionModel().getSelectedIndex();

            //Introducimos la lista de empleados en el listView
            listViewEmpleados.getItems().addAll(llistaEmpleatsSQL);

            //Devolvemos el valor del calendario con fecha
            diaCalendario = calendario.getValue();

            // Convertir el objeto LocalDate a un objeto LocalDateTime de Java, agregando una hora fija (00:00:00)
            LocalDateTime fechaHora = diaCalendario.atStartOfDay();

            // Convertir el objeto LocalDateTime de Java a un objeto Timestamp de Java que se pueda utilizar en la consulta MySQL
            fechaHoraSQL = Timestamp.valueOf(fechaHora);

            cargarListasView(fechaHoraSQL);

        }

    }
    // revisar metodo para que salga mensaje de mysql
    @FXML
    public void assignar() throws SQLException, IOException  {
        boolean assignats = false;
        String missatge="";
        

            
            if (listViewHorarios.getSelectionModel().getSelectedIndex() != -1 && listViewEmpleados.getSelectionModel().getSelectedIndex() != -1) {

            ObservableList<Horari> horaris = listViewHorarios.getSelectionModel().getSelectedItems();
            ObservableList<Empleados> empleados = listViewEmpleados.getSelectionModel().getSelectedItems();

            for (int i = 0; i < horaris.size(); i++) {
                for (int j = 0; j < empleados.size(); j++) {
                   missatge = gestioDades.assignarHoraris(horaris.get(i), empleados.get(j));
                }

            }
            
            if (missatge.equals("")) {
                alerta("Horari assignat correctament");
            }  else {
                alerta(missatge);
            }

        } else {
            alerta("Selecciona un horario y una persona");
        }
            
        

    }

    @FXML
    public void esborrar() throws SQLException, IOException {

        if (listViewHorarios.getSelectionModel().getSelectedIndex() != -1) {

            int confirmacion = Confirmacion();

            if (confirmacion == 1) {

                ObservableList<Horari> horaris = listViewHorarios.getSelectionModel().getSelectedItems();

                for (int i = 0; i < horaris.size(); i++) {
                    gestioDades.esborraHorari(horaris.get(i));
                }
                listViewHorarios.getItems().clear();
                cargarListasView(fechaHoraSQL);

            }

        } else {
            alerta("Selecciona un horario para borrarlo");
        }

    }

    public void initialize(URL url, ResourceBundle rb) {

        try {
            conecta = connexio.connecta();

        } catch (Exception ex) {
            alerta(ex + "");

        }

    }

    private void alerta(String text) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Informació");
        alerta.setContentText(text);
        alerta.show();
    }

    private int Confirmacion() {
        int valor;
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setContentText("¿Desea eliminar este horario?");
        ButtonType buttonTypeAceptar = new ButtonType("Aceptar");
        ButtonType buttonTypeCancelar = new ButtonType("Cancelar");
        alerta.getButtonTypes().setAll(buttonTypeAceptar, buttonTypeCancelar);

        Optional<ButtonType> result = alerta.showAndWait();
        if (result.get() == buttonTypeAceptar) {
            valor = 1;
        } else {
            valor = 0;
        }

        return valor;
    }

    public void getOpcion(javafx.event.ActionEvent event) {

        if (opcionAtraccion.isSelected()) {
            habilitarBotones();
            cargarAtracciones();
        }
        if (opcionRestaurante.isSelected()) {
            habilitarBotones();
            cargarRestaurantes();
        }

    }

    private void cargarAtracciones() {

        ObservableList<String> nombresAtraccions = FXCollections.observableArrayList();

        for (Atraccio atraccio : llistaAtraccions) {
            nombresAtraccions.add(atraccio.getNombre());

        }
        desplegableZona.setItems(nombresAtraccions);

    }

    private void habilitarBotones() {
        desplegableZona.setDisable(false);
        calendario.setDisable(false);

    }

    private void deshabilitarBotones() {
        desplegableZona.setDisable(true);
        calendario.setDisable(true);

    }

    private void cargarRestaurantes() {

        ObservableList<String> nomRestaurant = FXCollections.observableArrayList();

        for (Restaurant restaurant : llistaRestaurant) {
            nomRestaurant.add(restaurant.getNombre());

        }
        desplegableZona.setItems(nomRestaurant);

    }

    private void cargarListasView(Timestamp fechaHoraSQL) {

        if (opcionAtraccion.isSelected()) {

            idZona = llistaAtraccions.get(indice).getId();

            horariAtraccio = gestioDades.llistaHorarisAtraccions(idZona, fechaHoraSQL);

            listViewHorarios.getItems().addAll(horariAtraccio);

        } else if (opcionRestaurante.isSelected()) {

            idZona = llistaRestaurant.get(indice).getId();

            horariRestaurant = gestioDades.llistaHorarisRestaurants(idZona, fechaHoraSQL);

            listViewHorarios.getItems().addAll(horariRestaurant);

        }

        if (listViewHorarios.getItems().isEmpty()) {
            listViewHorarios.setDisable(true);
            listViewHorarios.getItems().addAll("Sin horarios planificados");
        }
    }

    @FXML
    private void cambiarPantallaCrear() throws IOException {
        App.setRoot("Crear");
    }
    
    @FXML
    private void cambiarPantallaViusalizar() throws IOException {
        App.setRoot("Visualizar");
    }
    
    
     

}
