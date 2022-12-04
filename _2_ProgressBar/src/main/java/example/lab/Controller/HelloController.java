package example.lab.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;

import example.lab.Client.IClient;
import example.lab.Client.ClientFactory;
import example.lab.Client.Client;

import java.io.IOException;

public class HelloController {
    private IClient client;

    @FXML
    ProgressBar pBar;
    @FXML
    private Button startButton;
    @FXML
    private Button pauseButton;
    @FXML
    Button stopButton;
    @FXML
    private void StartProgress() throws IOException {
        if (startButton.getText().equals("Старт"))
        {
            startButton.setText("Перезапуск");
            pauseButton.setDisable(false);
            stopButton.setDisable(false);
            client.startCalcProgress(value -> Platform.runLater(() -> pBar.setProgress(value)));
        }
        else{
            pauseButton.setText("Пауза");
            client.restart();
        }
    }

    @FXML
    private void PauseProgress() throws IOException {
        if(pauseButton.getText().equals("Пауза")){
            client.pause();
            pauseButton.setText("Продолжить");
        }
        else{
            client.resume();
            pauseButton.setText("Пауза");
        }
    }

    @FXML
    private void StopProgress() throws IOException {
        client.stop();
        startButton.setText("Старт");
        pauseButton.setText("Пауза");
        pauseButton.setDisable(true);
        stopButton.setDisable(true);
    }

    @FXML
    void initialize()
    {
        ClientFactory clientFactory = new ClientFactory();
        client = clientFactory.createInstance();
    }
}