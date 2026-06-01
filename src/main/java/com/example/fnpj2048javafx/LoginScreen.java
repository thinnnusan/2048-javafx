package com.example.fnpj2048javafx;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginScreen {

    public static void show(Stage stage) {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new javafx.geometry.Insets(20));

        Label title = new Label("2048");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 48));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(250);

        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register");
        Button exitBtn = new Button("Exit");

        Label message = new Label();
        message.setStyle("-fx-text-fill: red;");

        loginBtn.setOnAction(e -> {
            User user = FileManager.login(usernameField.getText().trim(), passwordField.getText());
            if (user != null) {
                GameScreen.currentUser = user;
                GameScreen.show(stage);
            } else {
                message.setText("Invalid username or password!");
            }
        });

        registerBtn.setOnAction(e -> {
            String user = usernameField.getText().trim();
            String pass = passwordField.getText();
            if (user.isEmpty() || pass.isEmpty()) {
                message.setText("Please fill all fields!");
            } else if (FileManager.register(user, pass)) {
                message.setStyle("-fx-text-fill: green;");
                message.setText("Registration successful! You can now login.");
            } else {
                message.setText("Username already exists!");
            }
        });

        exitBtn.setOnAction(e -> stage.close());

        root.getChildren().addAll(title, usernameField, passwordField,
                loginBtn, registerBtn, exitBtn, message);

        Scene scene = new Scene(root, 420, 500);
        stage.setTitle("2048 - Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}