module com.example._3_matches_game {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example._3_matches_game to javafx.fxml;
    exports com.example._3_matches_game;
}