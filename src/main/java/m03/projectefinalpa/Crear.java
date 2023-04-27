package m03.projectefinalpa;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
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
    

    //lista de los datos de la zona para los desplegables.  
    ObservableList<Atraccio> llistaAtraccions = gestioDades.llistaAtraccio();
    ObservableList<Restaurant> llistaRestaurant = gestioDades.llistaRestaurants();

    //variables para guardar los datos de los usuarios introducidos por teclado. 
    private Horari horari;
    private int horasTextoE;
    private int minutosTextoE;
    private int horasTextoS;
    private int minutosTextoS;
    private int idZona;
    private LocalDateTime fecha_inici;
    private LocalDateTime fecha_fin;
    private int dia;
    private Month mes;
    private int año;

    @FXML
    public void guardar() {

        boolean errores = false;
        String mensaje = "";

        try {
            LocalDate data = calendario.getValue();

            //comprueba los campos si estan llenos
            if (data == null || horasE.getText().isEmpty() || minutosE.getText().isEmpty() || horasS.getText().isEmpty() || minutosS.getText().isEmpty()) {
                errores = true;
                mensaje = "Campos vacíos";

            } else {
                // guardamos el texto introducido por el usuario
                horasTextoE = Integer.parseInt(horasE.getText());
                minutosTextoE = Integer.parseInt(minutosE.getText());
                horasTextoS = Integer.parseInt(horasS.getText());
                minutosTextoS = Integer.parseInt(minutosS.getText());

                //comprueba que los valores en el campo de horas y minutos sea correcto
                if (horasTextoE < 0 || horasTextoE > 23 || horasTextoS < 0 || horasTextoS > 23 || minutosTextoE < 0 || minutosTextoE > 59 || minutosTextoS < 0 || minutosTextoS > 59) {

                    errores = true;
                    mensaje = "Horas (00-23) y Minutos(00-60)";

                } else {

                    int indiceLista = desplegableZona.getSelectionModel().getSelectedIndex();
                    dia = data.getDayOfMonth();
                    mes = data.getMonth();
                    año = data.getYear();
                    fecha_inici = LocalDateTime.of(año, mes, dia, horasTextoE, minutosTextoE);
                    fecha_fin = LocalDateTime.of(año, mes, dia, horasTextoS, minutosTextoS);

                    //comprueba que el horario de entrada sea anterior al de salida
                    if (fecha_inici.isAfter(fecha_fin)) {

                        errores = true;
                        mensaje = "Entrada tiene que ser anterior a Salida";

                    } else {
                        boolean ok = false;

                        // si la attraccion es seleccionada, guardamos la atraccion, conseguimos su id y lo creamos mediante el metodo afegeixHorariAtraccio, de la clase
                        //gestio de dades
                        if (opcionAtraccion.isSelected()) {
                            Atraccio atraccion = llistaAtraccions.get(indiceLista);
                            idZona = atraccion.getId();
                            horari = new Horari(fecha_inici, fecha_fin, idZona, 0);
                            ok = gestioDades.afegeixHorariAtraccio(horari); //nos devuelve si se ha guardado o no,

                            //mismo metodo pero paralos restaurantes. 
                        } else if (opcionRestaurante.isSelected()) {
                            Restaurant restaurant = llistaRestaurant.get(indiceLista);
                            idZona = restaurant.getId();
                            horari = new Horari(fecha_inici, fecha_fin, 0, idZona);
                            ok = gestioDades.afegeixHorariRestaurant(horari);
                        }

                        // si se ha añadido, mensaje de alerta y borramos las horas, para seguir añadiendo. 
                        if (ok) {
                            alerta("Afegit correctament");
                            esborrarHores();

                            //si no se ha añadido, ponemos los mensajes de error como true, y guardos un mensaje en la variable mensaje. 
                        } else {
                            errores = true;
                            mensaje = "No afegit correctament";
                        }
                    }
                }
            }
            //si encuentra la excepcion, enviara un mensaje de que no se ha podido convertir el texto a valores. 
        } catch (IOException | NumberFormatException | SQLException e) {

            errores = false;
            alerta("Valores numericos, no texto");

        } // si hay otros errores, se envia el mensaje específico de cada error.
        if (errores) {

            alerta(mensaje);
        }
    }

    // inicializamos la apliacion como un reset. 
    @FXML
    public void esborrar() {
        opcionAtraccion.setSelected(false);
        opcionRestaurante.setSelected(false);
        desplegableZona.getItems().clear();
        calendario.setValue(null);

        deshabilitarBotones();

    }

    private void esborrarHores() {
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

    // metodo que revisa si esta seleccionado el radiobutton atraccion o restaurante, y habilita los botones y carga la lista de la zona en el ComboBox. 
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

    //metodos para cargar las tracciones en el combobox zona
    private void cargarAtracciones() {

        ObservableList<String> nombresAtraccions = FXCollections.observableArrayList();

        for (Atraccio atraccio : llistaAtraccions) {
            nombresAtraccions.add(atraccio.getNombre());

        }
        desplegableZona.setItems(nombresAtraccions);

    }

    //metodos para cargar las restaurantes en el combobox zona
    private void cargarRestaurantes() {

        ObservableList<String> nomRestaurant = FXCollections.observableArrayList();

        for (Restaurant restaurant : llistaRestaurant) {
            nomRestaurant.add(restaurant.getNombre());

        }
        desplegableZona.setItems(nomRestaurant);

    }

    //metodos para habilitat y deshabilitat botones para que el usuario no interactúe. 
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

    @FXML
    private void cambiarPantallaAsignar() throws IOException {
        App.setRoot("Asignar");
    }

    @FXML
    private void cambiarPantallaVisualizar() throws IOException {
        App.setRoot("Visualizar");
    }
}
