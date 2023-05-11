/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import m03.projectefinalpa.model.classes.Atraccio;
import m03.projectefinalpa.model.GestioDadesCrearYAsignar;
import m03.projectefinalpa.model.GestioDadesVisualitzar;
import m03.projectefinalpa.model.classes.Horari;
import m03.projectefinalpa.model.classes.Restaurant;

public class Visualizar {

    GestioDadesVisualitzar gestioDadesVisualitzar = new GestioDadesVisualitzar();
    GestioDadesCrearYAsignar gestioDades = new GestioDadesCrearYAsignar();

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

    int indice;
    int idZona;
    ObservableList<Horari> horariRestaurants = FXCollections.observableArrayList();
    ObservableList<Horari> horariAtraccions = FXCollections.observableArrayList();
    ObservableList<Atraccio> llistaAtraccions = gestioDades.llistaAtraccio(); // llistem totes les atraccions quan
                                                                              // iniciem
    ObservableList<Restaurant> llistaRestaurant = gestioDades.llistaRestaurants();// llistem tots els restaurants quan
                                                                                  // iniciem

    public void buscar() {

        // comprobar si hay alguna zona seleccionada
        if (desplegableZona.getSelectionModel().getSelectedIndex() >= 0 && calendarioDesde.getValue() != null
                && calendarioHasta.getValue() != null) {

            // reiniciamos la listView de horarios.
            listViewHorarios.getItems().clear();

            // miramos el indice de la zona seleccionada
            indice = desplegableZona.getSelectionModel().getSelectedIndex();

            // Devolvemos el valor del calendario con fecha LocalDate
            LocalDate diaDesde = calendarioDesde.getValue();
            LocalDate diaHasta = calendarioHasta.getValue();

            // comparamos si las fechas estan bien introducidas por comparacion.
            if (diaDesde.isBefore(diaHasta) || diaDesde.isEqual(diaHasta)) {

                // Convertir el objeto LocalDate de Java a un objeto LocaldateTime para añadir
                // horas y minutos.
                // Añadimos a la fecha la hora del inicio del dia a los dos y al hasta,
                // devolvemos un dia mas.
                LocalDateTime diadesdelocal = diaDesde.atStartOfDay();
                LocalDateTime diahastalocal = diaHasta.atStartOfDay().plusDays(1);

                // si seleccionamos atacciones:
                if (opcionAtraccion.isSelected()) {

                    // devolvemos el id de la atraccion
                    idZona = llistaAtraccions.get(indice).getId();
                    // pasamos a gestion de dades el id de la zona, la fecha de inicio y final y
                    // devolvemos la lista de los horarios juntos los trabajadores asignados.
                    horariAtraccions = gestioDadesVisualitzar.llistaHorarisAtraccions(idZona, diadesdelocal,
                            diahastalocal);

                    // si la lista no es vacía, lo añadimos a la listView. Si es vacía añadimos un
                    // mensaje de horarios sin asignar.
                    if (!horariAtraccions.isEmpty()) {
                        listViewHorarios.getItems().addAll(horariAtraccions);
                    } else {
                        listViewHorarios.getItems().add("Sin horarios asignados");
                    }

                } else if (opcionRestaurante.isSelected()) {

                    idZona = llistaRestaurant.get(indice).getId();
                    horariRestaurants = gestioDadesVisualitzar.llistaHorarisRestaurants(idZona, diadesdelocal,
                            diahastalocal);

                    if (!horariRestaurants.isEmpty()) {
                        listViewHorarios.getItems().addAll(horariRestaurants);
                    } else {
                        listViewHorarios.getItems().add("Sin horarios asignados");
                    }
                }

            } else {
                alerta("Datos erróneos");
            }

        }

    }

    private void initialize() {
        calendarioDesde.setPromptText("dd/MM/yyyy");
        calendarioHasta.setPromptText("dd/MM/yyyy");
    }

    // mismos métodos que en las otras clases

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
    private void cambiarPantallaAsignar() throws IOException {
        App.setRoot("Asignar");
    }

    @FXML
    private void cambiarPantallaEmpleados() throws IOException {
        App.setRoot("Empleados");
    }
    
     @FXML
    private void cambiarPantallaEditar() throws IOException {
        App.setRoot("Ingresar_Datos");
    }

}
