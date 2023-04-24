module m03.projectefinalpa {
   requires javafx.controls;
    requires javafx.fxml;
    requires  java.sql; 
    requires javafx.swing;
    requires java.desktop; 
    requires javafx.base;

    opens m03.projectefinalpa to javafx.fxml;
    exports m03.projectefinalpa;
}
