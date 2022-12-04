package example.lab.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import example.lab.model.Model;
import example.lab.model.api.ModelAPI;
import example.lab.model.api.Updatable;

public class HelloController {
    @FXML
    ProgressBar pBar;
    @FXML
    private Button pauseButton;
    @FXML
    Button stopButton;
    ////////////////////////////////////////
    private boolean isStarted = false;
    private boolean isPaused = false;
    ///////////////////////////////////////
    ModelAPI model;
    @FXML
    private void StartProgress(){
        /*boolean a = false;
        while (!a){
            pBar.setProgress(pBar.getProgress() + 1);
        }*/
        if (!isStarted) {
            Restart();
        }

        else if(isStarted){
            model.interrupt();
            reset();
            Restart();
        }
    }

    @FXML
    private void PauseProgress(){
        if(!isPaused){
            model.pause();
            isPaused = true;
            pauseButton.setText("Продолжить");
        }
        else{
            model.resume();
            isPaused = false;
            pauseButton.setText("Пауза");
        }
    }

    @FXML
    private void StopProgress(){
        model.stop();
        reset();
    }


    private void Restart(){
        model = new Model(1000, new Updatable() {
            @Override
            public void update(double value) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        pBar.setProgress(model.calcProgress(value, 1000));
                    }
                });
            }
        });
        isStarted = true;
        model.start();
    }

    private void reset(){
        isStarted = false;
        pauseButton.setText("Пауза");
        isPaused = false;
    }
}