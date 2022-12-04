module example.lab {
    requires javafx.controls;
    requires javafx.fxml;


    opens example.lab to javafx.fxml;
    exports example.lab;
    exports example.lab.Controller;
    opens example.lab.Controller to javafx.fxml;
}