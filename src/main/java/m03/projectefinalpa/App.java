package m03.projectefinalpa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import javafx.scene.image.Image;

public class App extends Application {

    private static Scene scene;
    public static Connection conexion;

    @Override
    public void start(Stage stage) throws IOException {

        Image favicon = new Image(getClass().getResourceAsStream("imagenes/fav.png"));

        scene = new Scene(loadFXML("Inici"), 700, 650);
        stage.setScene(scene);
        stage.getIcons().add(favicon);
        // stage.setResizable(false);
        stage.show();

    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));

        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
