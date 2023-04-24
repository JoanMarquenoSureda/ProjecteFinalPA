package m03.projectefinalpa;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import m03.projectefinalpa.model.classes.Atraccio;
import m03.projectefinalpa.model.Connexio;
import m03.projectefinalpa.model.GestioDades;
import m03.projectefinalpa.model.classes.Horari;
import m03.projectefinalpa.model.classes.Restaurant;

public class Crear {

    GestioDades gestioDades = new GestioDades();
    Connection conecta;
    Connexio connexio = new Connexio();

    @FXML
    RadioButton opcionAtraccion;
    @FXML
    RadioButton opcionRestaurante;
    @FXML
    Label labelZona;
    @FXML
    ComboBox<String> desplegableZona = new ComboBox<>();
    @FXML
    DatePicker calendario;
    @FXML
    TextField horasE;
    @FXML
    TextField minutosE;
    @FXML
    TextField horasS;
    @FXML
    TextField minutosS;
    @FXML
    GridPane gridPane;

    ObservableList<Atraccio> llistaAtraccions = gestioDades.llistaAtraccio();
    ObservableList<Restaurant> llistaRestaurant = gestioDades.llistaRestaurants();
    private Horari horari;
    private int horasTextoE;
    private int minutosTextoE;
    private int horasTextoS;
    private int minutosTextoS;
    private int idZona;
    private String nombreZona;
    private LocalDateTime fecha_inici;
    private LocalDateTime fecha_fin;
    private int dia;
    private Month mes;
    private int año;
    private ObservableList<String> opcionesDesplegable;

    public void guardar() {
        boolean errores = false;
        boolean errorHoras = false;
        boolean errorMinutos = false;
        boolean errorFecha = false;
        String mensaje = "";
        try {
            LocalDate data = calendario.getValue();

            //comprueba los campos si estan llenos
            if (data == null || horasE.getText().isEmpty() || minutosE.getText().isEmpty() || horasS.getText().isEmpty() || minutosS.getText().isEmpty()) {
                errores = true;
                mensaje = "Campos vacíos";

            } else {

                int horasTextoE = Integer.parseInt(horasE.getText());
                int minutosTextoE = Integer.parseInt(minutosE.getText());
                int horasTextoS = Integer.parseInt(horasS.getText());
                int minutosTextoS = Integer.parseInt(minutosS.getText());

                //comprueba que los valores en el campo de horas y minutos sea correcto
                if (horasTextoE < 0 || horasTextoE > 23 || horasTextoS < 0 || horasTextoS > 23 || minutosTextoE < 0 || minutosTextoE > 59 || minutosTextoS < 0 || minutosTextoS > 59) {

                    errores = true;
                    mensaje = "Horas (00-23) y Minutos(00-60)";

                } else {

                    int idZona = desplegableZona.getSelectionModel().getSelectedIndex();
                    int dia = data.getDayOfMonth();
                    Month mes = data.getMonth();
                    int año = data.getYear();
                    LocalDateTime fecha_inici = LocalDateTime.of(año, mes, dia, horasTextoE, minutosTextoE);
                    LocalDateTime fecha_fin = LocalDateTime.of(año, mes, dia, horasTextoS, minutosTextoS);

                    //comprueba que el horario de entrada sea anterior al de salida
                    if (fecha_inici.isAfter(fecha_fin)) {

                        errores = true;
                        mensaje = "Entrada tiene que ser anterior a Salida";

                    } else {
                        boolean ok = false;
                        if (opcionAtraccion.isSelected()) {
                            Atraccio atraccion = llistaAtraccions.get(idZona);
                            idZona = atraccion.getId();
                            horari = new Horari(fecha_inici, fecha_fin, idZona, 0);
                            ok = gestioDades.afegeixHorariAtraccio(horari);
                        } else if (opcionRestaurante.isSelected()) {
                            Restaurant restaurant = llistaRestaurant.get(idZona);
                            idZona = restaurant.getId();
                            horari = new Horari(fecha_inici, fecha_fin, 0, idZona);
                            ok = gestioDades.afegeixHorariRestaurant(horari);
                        }

                        if (ok) {
                            alerta("Afegit correctament");
                            esborrarHores();
                        } else {
                            errores = true;
                            mensaje = "No afegit correctament";
                        }
                    }
                }
            }
        } catch (Exception e) {

            errores = false;
            alerta("Valores numericos, no texto");

        }
        if (errores) {

            alerta(mensaje);
        }
    }

    private int buscarAtraccion(String nombreZona) {
        int id = 0;
        int i = 0;
        boolean trobat = false;

        while (i < llistaAtraccions.size() && !trobat) {
            if (llistaAtraccions.get(i).getNombre().equals(nombreZona)) {
                id = llistaAtraccions.get(i).getId();
                trobat = true;
            }

            i++;

        }

        return id;
    }

    private int buscarRestaurante(String nombreZona) {
        int id = 0;
        int i = 0;
        boolean trobat = false;

        while (i < llistaRestaurant.size() && !trobat) {
            if (llistaRestaurant.get(i).getNombre().equals(nombreZona)) {
                id = llistaRestaurant.get(i).getId();
                trobat = true;
            }
            i++;

        }

        return id;
    }

    public void esborrar() {
        opcionAtraccion.setSelected(false);
        opcionRestaurante.setSelected(false);
        desplegableZona.getItems().clear();
        calendario.setValue(null);

        deshabilitarBotones();

    }

    public void esborrarHores() {
        horasE.setText("");
        horasS.setText("");
        minutosE.setText("");
        minutosS.setText("");

    }

    public void initialize(URL url, ResourceBundle rb) {

        try {
            conecta = connexio.connecta();

        } catch (Exception ex) {
            alerta(ex + "");

        }

        labelZona.setVisible(false);

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
        calendario.setDisable(false);
        horasE.setDisable(false);
        minutosE.setDisable(false);
        horasS.setDisable(false);
        minutosS.setDisable(false);
    }

    private void deshabilitarBotones() {
        desplegableZona.setDisable(true);
        calendario.setDisable(true);
        horasE.setDisable(true);
        minutosE.setDisable(true);
        horasS.setDisable(true);
        minutosS.setDisable(true);
    }

    private void cargarRestaurantes() {

        ObservableList<String> nomRestaurant = FXCollections.observableArrayList();

        for (Restaurant restaurant : llistaRestaurant) {
            nomRestaurant.add(restaurant.getNombre());

        }
        desplegableZona.setItems(nomRestaurant);

    }
    
        @FXML
    private void cambiarPantallaAsignar() throws IOException {
        App.setRoot("Asignar");
    }

}
