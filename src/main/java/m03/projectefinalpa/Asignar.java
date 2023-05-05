/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa;

import java.io.IOException;
import java.sql.SQLException;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import m03.projectefinalpa.model.classes.Atraccio;
import m03.projectefinalpa.model.GestioDadesCrearYAsignar;
import m03.projectefinalpa.model.classes.EmpleadosClass;
import m03.projectefinalpa.model.classes.Horari;
import m03.projectefinalpa.model.classes.Restaurant;

public class Asignar {

    GestioDadesCrearYAsignar gestioDades = new GestioDadesCrearYAsignar();

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

    ObservableList<Horari> horariRestaurant = FXCollections.observableArrayList(); // llistem tots els horaris d'un
                                                                                   // restaurant
    ObservableList<Horari> horariAtraccio = FXCollections.observableArrayList();// llistem tots els horaris d'una
                                                                                // atraccio
    ArrayList<String> llistaHoraris = new ArrayList<>(); //

    ObservableList<Atraccio> llistaAtraccions = gestioDades.llistaAtraccio(); // llistem totes les atraccions quan
                                                                              // iniciem
    ObservableList<Restaurant> llistaRestaurant = gestioDades.llistaRestaurants();// llistem tots els restaurants quan
                                                                                  // iniciem
    ObservableList<EmpleadosClass> llistaEmpleatsSQL = gestioDades.llistaEmpleatsHoraris(); // llistem tots els empleats
                                                                                            // que son "Aprendiz" y
                                                                                            // "Trabajador"

    ObservableList<String> dadesHorarisSenseAssignar = FXCollections.observableArrayList(); // Llista on guardarem les
                                                                                            // dades del horaris del
                                                                                            // metode ToSring
    private Timestamp fechaHoraSQL;
    private LocalDate diaCalendario;
    int idZona;
    int indice;

    public void buscar() {

        listViewHorarios.setDisable(false);
        listViewEmpleados.setDisable(false);

        // para hacer seleccion multiple de empleados y horarios.
        listViewEmpleados.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listViewHorarios.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // comprobar si hay alguna zona seleccionada
        if (desplegableZona.getSelectionModel().getSelectedIndex() >= 0 && calendario.getValue() != null) {

            inicializarvistas();

            // miramos el indice de la zona seleccionada
            indice = desplegableZona.getSelectionModel().getSelectedIndex();

            // Introducimos la lista de empleados en el listView
            listViewEmpleados.getItems().addAll(llistaEmpleatsSQL);

            // Devolvemos el valor del calendario con fecha
            diaCalendario = calendario.getValue();

            // Convertir el objeto LocalDate a un objeto LocalDateTime de Java, agregando
            // una hora fija (00:00:00)
            LocalDateTime fechaHora = diaCalendario.atStartOfDay();

            // Convertir el objeto LocalDateTime de Java a un objeto Timestamp de Java que
            // se pueda utilizar en la consulta MySQL
            fechaHoraSQL = Timestamp.valueOf(fechaHora);

            // cargar la lista de horarios en la ListView
            cargarListasView();

        }

    }

    // metodo para asignar con el button asignar
    @FXML
    public void assignar() throws SQLException, IOException {

        String missatge = "";

        // revisamos que hay algun elemento seleccionado de la listview horarios y de la
        // listView empleados.
        if (listViewHorarios.getSelectionModel().getSelectedIndex() != -1
                && listViewEmpleados.getSelectionModel().getSelectedIndex() != -1) {

            // Añadimos los horarios y los empleados seleccionados en una lista.
            ObservableList<Horari> horaris = listViewHorarios.getSelectionModel().getSelectedItems();
            ObservableList<EmpleadosClass> empleados = listViewEmpleados.getSelectionModel().getSelectedItems();

            // reccorremos los horarios y los empleados, para asignar cada horario a cada
            // persona seleccionada.
            for (int i = 0; i < horaris.size(); i++) {
                for (int j = 0; j < empleados.size(); j++) {
                    missatge = gestioDades.assignarHoraris(horaris.get(i), empleados.get(j));
                }

            }

            if (missatge.equals("")) {
                alerta("Horario asignado correctamente");

                // limpiamos las celdas seleccionadas, para que así no confunda al usuario una
                // vez asignado.
                listViewEmpleados.getSelectionModel().clearSelection();
                listViewHorarios.getSelectionModel().clearSelection();
            } else {
                alerta(missatge);
            }

        } else {
            alerta("Selecciona un horario y una persona");
        }

    }

    // metodo borrar del button borrar, que borra un horario seleccionado del
    // listView.
    @FXML
    public void esborrar() throws SQLException, IOException {

        // revisamos si hay alguna fila seleccionada
        if (listViewHorarios.getSelectionModel().getSelectedIndex() != -1) {

            // enviamos la alerta de confirmacion y vemos la opcion escogida por el usuario=
            // 1.Borrar, 0.Cancelar
            int confirmacion = Confirmacion();

            // si el usuario decide borrar,
            if (confirmacion == 1) {

                // obtenemos el horario seleccionado
                ObservableList<Horari> horaris = listViewHorarios.getSelectionModel().getSelectedItems();

                // enviamos la lista de horrarios al objeto GestioDades, y lo borramos con el
                // metodo esborrarHorari.
                for (int i = 0; i < horaris.size(); i++) {
                    gestioDades.esborraHorari(horaris.get(i));
                }

                // Limpiamos el listViewHorarios y lo actualizamos con el horario borrado.
                listViewHorarios.getItems().clear();
                cargarListasView();

            }

            // Si no hay nada seleccionado enviamos una alerta
        } else {
            alerta("Selecciona un horario para borrarlo");
        }

    }

    private void initialize() {
        calendario.setPromptText("dd/MM/yyyy");
    }

    // alerta general de errores.
    private void alerta(String text) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Información");
        alerta.setContentText(text);
        alerta.show();
    }

    // alerta de confirmación, para eliminar el horario.
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

    // metodo que revisa si esta seleccionado el radiobutton atraccion o
    // restaurante, y habilita los botones y carga la lista de la zona en el
    // ComboBox.
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

    // metodo para habilitat botones del desplegable de zonas y calendario.
    private void habilitarBotones() {
        desplegableZona.setDisable(false);
        calendario.setDisable(false);

    }

    // método para deshabilitar los botones del desplegable y calendario.
    private void deshabilitarBotones() {
        desplegableZona.setDisable(true);
        calendario.setDisable(true);

    }

    // reiniciamos la lista de horarios y limpiamos los dos ListView
    private void inicializarvistas() {

        llistaHoraris.clear();
        dadesHorarisSenseAssignar.clear();
        listViewEmpleados.getItems().clear();
        listViewHorarios.getItems().clear();
        horariAtraccio.clear();
        horariRestaurant.clear();
    }

    // Carga la lista de restaurantes en el ComboBox de desplegableZona
    private void cargarRestaurantes() {

        ObservableList<String> nomRestaurant = FXCollections.observableArrayList();

        for (Restaurant restaurant : llistaRestaurant) {
            nomRestaurant.add(restaurant.getNombre());

        }
        desplegableZona.setItems(nomRestaurant);

    }

    // Carga la lista de Atracciones en el ComboBox de desplegableZona
    private void cargarAtracciones() {

        ObservableList<String> nombresAtraccions = FXCollections.observableArrayList();

        for (Atraccio atraccio : llistaAtraccions) {
            nombresAtraccions.add(atraccio.getNombre());

        }
        desplegableZona.setItems(nombresAtraccions);

    }

    // metodo que devuelve los horarios de la zona seleccionada con los hora
    // seleccionada.
    private void cargarListasView() {

        // revisa si las atracciones estan seleccionadas
        if (opcionAtraccion.isSelected()) {

            // devuelve el id de la atraccion sobre la lisya de atracciones.
            idZona = llistaAtraccions.get(indice).getId();

            // devuelve una lista con los horaris de la atraccion , a través de la clase
            // gestioDades.
            horariAtraccio = gestioDades.llistaHorarisAtraccions(idZona, fechaHoraSQL);

            // cargar los horarios en la listView Horarios, que lo visualiza con el metodo
            // toString de la classe Horarios.
            listViewHorarios.getItems().addAll(horariAtraccio);

            // para la opcio restaurante
        } else if (opcionRestaurante.isSelected()) {

            idZona = llistaRestaurant.get(indice).getId();

            horariRestaurant = gestioDades.llistaHorarisRestaurants(idZona, fechaHoraSQL);

            listViewHorarios.getItems().addAll(horariRestaurant);

        }

        // si la lista está vacía, escribimos un mensaje de horarios no planificados y
        // deshabilitamos para que el usuario no pueda seleccionar filas.
        if (listViewHorarios.getItems().isEmpty()) {
            listViewEmpleados.setDisable(true);
            listViewHorarios.setDisable(true);
            listViewHorarios.getItems().addAll("Sin horarios planificados");
            listViewEmpleados.getItems().clear();
            listViewEmpleados.getItems().addAll("Sin empleados para assignar");
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

    @FXML
    private void cambiarPantallaEmpleados() throws IOException {
        App.setRoot("Empleados");
    }

}
