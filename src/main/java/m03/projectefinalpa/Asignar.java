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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

    ObservableList<Horari> horariRestaurant = FXCollections.observableArrayList(); // llistem tots els horaris d'un restaurant
    ObservableList<Horari> horariAtraccio = FXCollections.observableArrayList();// llistem tots els horaris d'una atraccio
    ArrayList<String> llistaHoraris = new ArrayList<>(); //
    ObservableList<Atraccio> llistaAtraccions; // llistem totes les atraccions quan iniciem
    ObservableList<Restaurant> llistaRestaurant;// llistem tots els restaurants quan iniciem
    ObservableList<EmpleadosClass> llistaEmpleatsSQL = gestioDades.llistaEmpleatsHoraris(); // llistem tots els empleats que son "Aprendiz" y "Trabajador"
    ObservableList<String> dadesHorarisSenseAssignar = FXCollections.observableArrayList(); // Llista on guardarem les  dades del horaris del metode ToSring

    private Timestamp fechaHoraSQL; //Data format MYsql
    private LocalDate diaCalendario;
    private String nombreZona;
    private int indice;

    //método para buscar un horario asociado al boton buscar
    public void buscar() {

        listViewHorarios.setDisable(false);
        listViewEmpleados.setDisable(false);

        // para hacer seleccion multiple de empleados y horarios.
        listViewEmpleados.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listViewHorarios.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // comprobar si hay alguna zona seleccionada
        if (desplegableZona.getSelectionModel().getSelectedIndex() >= 0 && calendario.getValue() != null) {

            // reiniciamos la lista de horarios y limpiamos los dos ListView
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

        } else {
            alerta("Debes completar los campos antes de buscar un horario");
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

            switch (missatge) {
                case "ok":
                    alerta("Horario asignado correctamente");
                    // limpiamos las celdas seleccionadas, para que así no confunda al usuario una
                    // vez asignado.
                    listViewEmpleados.getSelectionModel().clearSelection();
                    listViewHorarios.getSelectionModel().clearSelection();
                    break;
                case "":
                    //controlamos primero si el mensaje se devuelve vacío, ya que no se ha assignado correctamente
                    alerta("Horario no asignado correctamente");
                    break;
                // si devuelve un mensaje específico, lo mandamos por alerta
                default:
                    alerta(missatge);
                    break;
            }

        } else {
            alerta("Selecciona un horario y una persona");
        }

    }

    public void initialize() {
        llistaAtraccions = gestioDades.llistaAtraccio();
        llistaRestaurant = gestioDades.llistaRestaurants();
    }

    // alerta general de errores.
    private void alerta(String text) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Información");
        alerta.setContentText(text);
        alerta.show();
    }

    // metodo que revisa si esta seleccionado el radiobutton atraccion o
    // restaurante, y habilita los botones y carga la lista de la zona en el
    // ComboBox.
    @FXML
    public void obtenerOpcionZona(javafx.event.ActionEvent event) {

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

    // metodo que devuelve los horarios de la zona seleccionada con los horas
    // seleccionada. Se ha trabajado diferenciando si es atraccion o restaurante, ya que ha habido la incidendia de que no está unificado con un solo id. 
    private void cargarListasView() {

        // revisa si las atracciones estan seleccionadas
        if (opcionAtraccion.isSelected()) {

            // devuelve el id de la atraccion sobre la lista de atracciones.
            nombreZona = llistaAtraccions.get(indice).getNombre();

            // devuelve una lista con los horaris de la atraccion , a través de la clase
            // gestioDades.
            horariAtraccio = gestioDades.llistaHorarisAtraccions(nombreZona, fechaHoraSQL);

            // cargar los horarios en la listView Horarios, que lo visualiza con el metodo
            // toString de la classe Horarios.
            listViewHorarios.getItems().addAll(horariAtraccio);

            // para la opcio restaurante
        } else if (opcionRestaurante.isSelected()) {

            nombreZona = llistaRestaurant.get(indice).getNombre();

            horariRestaurant = gestioDades.llistaHorarisRestaurants(nombreZona, fechaHoraSQL);

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
    public void cambiarPantallaCrear() throws IOException {
        App.setRoot("Crear");
    }

    @FXML
    public void cambiarPantallaViusalizar() throws IOException {
        App.setRoot("Visualizar");
    }

    @FXML
    public void cambiarPantallaEmpleados() throws IOException {
        App.setRoot("Empleados");
    }

    @FXML
    public void cambiarPantallaEditar() throws IOException {
        App.setRoot("Ingresar_Datos");
    }

}
