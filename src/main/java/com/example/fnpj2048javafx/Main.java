package com.example.fnpj2048javafx;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        FileManager.loadUsers();
        LoginScreen.show(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}