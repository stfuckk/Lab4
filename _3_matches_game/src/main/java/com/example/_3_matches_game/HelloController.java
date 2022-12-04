package com.example._3_matches_game;

import com.example._3_matches_game.Client.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Objects;


public class HelloController {

    private Client client;

    @FXML
    private Button playButton;
    @FXML
    private Button takeButton;
    @FXML
    private TextField textField;
    @FXML
    private Label infoLabel;
    @FXML
    private Label countMatches;

    @FXML
    void initialize() throws IOException {
        client = new Client();
    }

    @FXML
    protected void Play(){
        infoLabel.setText("Ожидание 2-ого игрока...");
        playButton.setDisable(true);

        new Thread(()-> { //запускаем поток, который ожидает получения состояния этого клиента
            String string = null;
            try {
                string = client.Play();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(string.equals("play")){ //если получили play, то можно ходить
                textField.setDisable(false);
                takeButton.setDisable(false);
                Platform.runLater(()->infoLabel.setText("Твой ход!"));
            }
            else if (string.equals("wait")){ //если получили wait, то ждем ход второго клиента
                textField.setDisable(true);
                textField.setDisable(true);
                try {
                    getRules();
                }
                catch (IOException e) {throw new RuntimeException(e);}
            }
        }).start();
    }

    @FXML
    protected void Take() throws IOException {
        if(!Objects.equals(textField.getText(), "")){
            client.Take(Integer.parseInt(textField.getText()));
            textField.setText("");
            new Thread(()-> {
                try {
                    getRules();
                } catch (IOException e) {throw new RuntimeException(e);}
                if(!client.checkClosed())
                {
                    try {
                        getRules();
                    } catch (IOException e) {throw new RuntimeException(e);}
                }
            }).start();
        }
        else
        {
            infoLabel.setText("Введите в текстовое поле кол-во спичек: 1-5");
        }
    }
    @FXML
    protected void textFieldCheck(){
        if(!itsNumber(textField.getText()))
        {
            infoLabel.setText("Введите число: 1-5");
            textField.setText("");
        }
        else if(Integer.parseInt(textField.getText()) < 1 || Integer.parseInt(textField.getText()) > 5){
            infoLabel.setText("Введите число: 1-5");
            textField.setText("");
        }
        else if(Integer.parseInt(textField.getText()) > Integer.parseInt(countMatches.getText())){
            infoLabel.setText("Столько спичек нет!");
            textField.setText("");
        }
    }

    public static boolean itsNumber(String string){ //проверяем, является ли строка числом
        try{
            Integer.parseInt(string);
            return true;
        }
        catch (NumberFormatException e){return false;}
    }

    public void getRules() throws IOException{
        String string = String.valueOf(client.getCountMatches());
        Platform.runLater(()->countMatches.setText(string));
        String string2 = client.getRules();

        if(string2.equals("play")){
            Platform.runLater(()-> textField.setDisable(false));
            Platform.runLater(()-> takeButton.setDisable(false));
            Platform.runLater(()->infoLabel.setText("Твой ход!"));
        }
        else if(string2.equals("wait")){
            Platform.runLater(()-> textField.setDisable(true));
            Platform.runLater(()-> takeButton.setDisable(true));
            Platform.runLater(()->infoLabel.setText("Ожидание 2-ого игрока..."));
        }
        else if(string2.equals("win")){
            Platform.runLater(()-> textField.setDisable(true));
            Platform.runLater(()-> takeButton.setDisable(true));
            Platform.runLater(()->infoLabel.setText("УРА! ПОБЕДА!"));
        }
        else if(string2.equals("lose")){
            Platform.runLater(()-> textField.setDisable(true));
            Platform.runLater(()-> takeButton.setDisable(true));
            Platform.runLater(()->infoLabel.setText("БЛИН! ПРОИГРАЛ("));
        }
    }
}