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
import m03.projectefinalpa.model.GestioDadesVisualitzar;
import m03.projectefinalpa.model.classes.Empleados;
import m03.projectefinalpa.model.classes.Horari;
import m03.projectefinalpa.model.classes.Restaurant;

public class Visualizar {

    GestioDadesVisualitzar gestioDadesVisualitzar = new GestioDadesVisualitzar();
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
    DatePicker calendarioDesde;
    @FXML
    DatePicker calendarioHasta;
    @FXML
    ListView listViewHorarios;

    Timestamp fechaHoraSQL_inicio;
    Timestamp fechaHoraSQL_final;

    int indice;
    int idZona;
    ObservableList<Horari> horariRestaurants = FXCollections.observableArrayList();
    ObservableList<Horari> horariAtraccions = FXCollections.observableArrayList();
    ObservableList<Atraccio> llistaAtraccions = gestioDades.llistaAtraccio(); //llistem totes les atraccions quan iniciem
    ObservableList<Restaurant> llistaRestaurant = gestioDades.llistaRestaurants();//llistem tots els restaurants quan iniciem

    public void buscar() {

        //comprobar si hay alguna zona seleccionada 
        if (desplegableZona.getSelectionModel().getSelectedIndex() >= 0 && calendarioDesde.getValue() != null && calendarioHasta.getValue() != null) {

            //reiniciamos la listView de horarios.
            listViewHorarios.getItems().clear();

            //miramos el indice de la zona seleccionada
            indice = desplegableZona.getSelectionModel().getSelectedIndex();

            //Devolvemos el valor del calendario con fecha
            LocalDate diaDesde = calendarioDesde.getValue();
            LocalDate diaHasta = calendarioHasta.getValue();

            if (diaDesde.isBefore(diaHasta) || diaDesde.isEqual(diaHasta)) {
                // Convertir el objeto LocalDateTime de Java a un objeto Timestamp de Java que se pueda utilizar en la consulta MySQL
                fechaHoraSQL_inicio = Timestamp.valueOf(diaDesde.atStartOfDay());
                fechaHoraSQL_final = Timestamp.valueOf(diaHasta.atTime(00, 00, 00));

                if (opcionAtraccion.isSelected()) {

                    idZona = llistaAtraccions.get(indice).getId();
                    horariAtraccions = gestioDadesVisualitzar.llistaHorarisAtraccions(idZona, fechaHoraSQL_inicio, fechaHoraSQL_final);

                    if (!horariAtraccions.isEmpty()) {
                        listViewHorarios.getItems().addAll(horariAtraccions);
                    } else {
                        listViewHorarios.getItems().add("Sense Horaris assignats");
                    }

                } else if (opcionRestaurante.isSelected()) {

                    idZona = llistaRestaurant.get(indice).getId();
                    horariRestaurants = gestioDadesVisualitzar.llistaHorarisRestaurants(idZona, fechaHoraSQL_inicio, fechaHoraSQL_final);

                    if (!horariRestaurants.isEmpty()) {
                        listViewHorarios.getItems().addAll(horariRestaurants);
                    } else {
                        listViewHorarios.getItems().add("Sense Horaris assignats");
                    }
                }

            } else {
                alerta("Dates erròneas");
            }

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
        calendarioDesde.setDisable(false);
        calendarioHasta.setDisable(false);

    }

    private void deshabilitarBotones() {
        desplegableZona.setDisable(true);
        calendarioDesde.setDisable(true);
        calendarioHasta.setDisable(true);

    }

    private void cargarRestaurantes() {

        ObservableList<String> nomRestaurant = FXCollections.observableArrayList();

        for (Restaurant restaurant : llistaRestaurant) {
            nomRestaurant.add(restaurant.getNombre());

        }
        desplegableZona.setItems(nomRestaurant);

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
