/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m03.projectefinalpa;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    ObservableList<Horari> horariRestaurants = FXCollections.observableArrayList();
    ObservableList<Horari> horariAtraccions = FXCollections.observableArrayList();
    ObservableList<Atraccio> llistaAtraccions;
    ObservableList<Restaurant> llistaRestaurant;

    private String nombreZona; //guardamos el valor del nombre de la zona.
    private int indice; // guardamos el indice del combobox seleccionado

    @FXML //metodo para buscar un horario de una zona entre varias fechas. 
    public void buscar() {

        // para hacer seleccion multiple de horarios.        
        listViewHorarios.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
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
                LocalDateTime diadesdelocal = diaDesde.atStartOfDay(); // se pondrá la hora y mm :(00:00)
                LocalDateTime diahastalocal = diaHasta.atStartOfDay().plusDays(1); // pondra la fecha con 1 día más para que sean las 23:59

                // si seleccionamos atacciones:
                if (opcionAtraccion.isSelected()) {

                    // devolvemos el nombre de la atraccion
                    nombreZona = llistaAtraccions.get(indice).getNombre();
                    // pasamos a gestion de dades el nombre de la zona, la fecha de inicio y final y
                    // devolvemos la lista de los horarios juntos los trabajadores asignados.
                    horariAtraccions = gestioDadesVisualitzar.llistaHorarisAtraccions(nombreZona, diadesdelocal,
                            diahastalocal);

                    // si la lista no es vacía, lo añadimos a la listView. Si es vacía añadimos un
                    // mensaje de horarios sin asignar.
                    if (!horariAtraccions.isEmpty()) {
                        listViewHorarios.getItems().addAll(horariAtraccions);
                        listViewHorarios.setDisable(false);
                    } else {
                        listViewHorarios.getItems().add("Sin horarios asignados");
                        listViewHorarios.setDisable(true);
                    }

                    //igual que para Atraccion pero para Restaurante
                } else if (opcionRestaurante.isSelected()) {

                    nombreZona = llistaRestaurant.get(indice).getNombre();
                    horariRestaurants = gestioDadesVisualitzar.llistaHorarisRestaurants(nombreZona, diadesdelocal,
                            diahastalocal);

                    if (!horariRestaurants.isEmpty()) {
                        listViewHorarios.getItems().addAll(horariRestaurants);
                        listViewHorarios.setDisable(false);
                        
                    } else {
                    
                        listViewHorarios.getItems().add("Sin horarios asignados");
                        listViewHorarios.setDisable(true);
                    }
                }
                 // si las fechas son giradas dará un mensaje.
            } else {
                alerta("Fechas incorrectas");
            }

        } else {
            alerta("Debes completar los campos antes de buscar un horario");
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

                // obtenemos los horarios seleccionados, ya que pueden ser varios.
                ObservableList<Horari> horaris = listViewHorarios.getSelectionModel().getSelectedItems();

                // enviamos la lista de horrarios a al metodo de gestiodades "esborrarhorari", y los borramos, recorriendo el array. 
                for (int i = 0; i < horaris.size(); i++) {
                    gestioDadesVisualitzar.esborraHorari(horaris.get(i));
                }

                // Limpiamos el listViewHorarios y lo actualizamos con el horario borrado.
                listViewHorarios.getItems().clear();
                buscar();

            }

            // Si no hay nada seleccionado enviamos una alerta
        } else {
            alerta("Selecciona un horario para borrarlo");
        }

    }

    // mismos métodos que en las otras clases
    public void initialize() {
        llistaAtraccions = gestioDades.llistaAtraccio();
        llistaRestaurant = gestioDades.llistaRestaurants();
         listViewHorarios.setDisable(true); //hacer inaccesible el listview

    }

    private void alerta(String text) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Informació");
        alerta.setContentText(text);
        alerta.show();
    }

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

    private void cargarRestaurantes() {

        ObservableList<String> nomRestaurant = FXCollections.observableArrayList();

        for (Restaurant restaurant : llistaRestaurant) {
            nomRestaurant.add(restaurant.getNombre());

        }
        desplegableZona.setItems(nomRestaurant);

    }

    @FXML
    public void cambiarPantallaCrear() throws IOException {
        App.setRoot("Crear");
    }

    @FXML
    public void cambiarPantallaAsignar() throws IOException {
        App.setRoot("Asignar");
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
