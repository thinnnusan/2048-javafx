package com.example.fnpj2048javafx;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Random;

public class GameScreen {

    public static User currentUser;

    private int[][] board = new int[4][4];
    private Label[][] tiles = new Label[4][4];
    private int score = 0;
    private int highScore = 0;

    private Label scoreLabel = new Label();
    private Label highScoreLabel = new Label();
    private Label usernameLabel = new Label();

    public static void show(Stage stage) {
        new GameScreen().createGameScreen(stage);
    }

    private void createGameScreen(Stage stage) {
        highScore = currentUser.getHighScore();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        VBox topBar = new VBox(8);
        topBar.setAlignment(Pos.CENTER);

        usernameLabel.setText("Player: " + currentUser.getUsername());
        usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        HBox scoreBox = new HBox(30);
        scoreBox.setAlignment(Pos.CENTER);

        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        highScoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        highScoreLabel.setText("High Score: " + highScore);

        updateScore();

        scoreBox.getChildren().addAll(scoreLabel, highScoreLabel);
        topBar.getChildren().addAll(usernameLabel, scoreBox);

        root.setTop(topBar);

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setAlignment(Pos.CENTER);

        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                Label tile = new Label("");
                tile.setMinSize(100, 100);
                tile.setFont(Font.font("Arial", FontWeight.BOLD, 28));
                tile.setStyle("-fx-alignment: center; -fx-background-color: #f0e6e6; " +
                        "-fx-border-color: #bbada0; -fx-border-width: 2;");

                tiles[r][c] = tile;
                grid.add(tile, c, r);
            }
        }

        root.setCenter(grid);

        HBox buttonBar = new HBox(12);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(15, 0, 0, 0));

        Button newGameBtn = new Button("New Game");
        Button saveBtn = new Button("Save Game");
        Button loadBtn = new Button("Load Game");
        Button logoutBtn = new Button("Logout");

        styleButton(newGameBtn);
        styleButton(saveBtn);
        styleButton(loadBtn);
        styleButton(logoutBtn);

        newGameBtn.setOnAction(e -> newGame());
        saveBtn.setOnAction(e -> saveCurrentGame());
        loadBtn.setOnAction(e -> loadSavedGame());
        logoutBtn.setOnAction(e -> logout(stage));

        newGameBtn.setFocusTraversable(false);
        saveBtn.setFocusTraversable(false);
        loadBtn.setFocusTraversable(false);
        logoutBtn.setFocusTraversable(false);

        buttonBar.getChildren().addAll(newGameBtn, saveBtn, loadBtn, logoutBtn);
        root.setBottom(buttonBar);

        Scene scene = new Scene(root, 520, 620);
        scene.setOnKeyPressed(this::handleKey);

        root.setFocusTraversable(true);

        stage.setTitle("2048 - " + currentUser.getUsername());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        Platform.runLater(root::requestFocus);

        newGame();
    }

    private void styleButton(Button btn) {
        btn.setFont(Font.font("Arial", 14));
        btn.setPadding(new Insets(8, 16, 8, 16));
    }

    private void newGame() {
        board = new int[4][4];
        score = 0;
        updateScore();
        startGame();
        updateUI();
    }

    private void startGame() {
        addRandomTile();
        addRandomTile();
    }

    private void handleKey(KeyEvent e) {
        int[][] oldBoard = copyBoard();

        switch (e.getCode()) {
            case LEFT:
                moveLeft();
                break;
            case RIGHT:
                rotate180();
                moveLeft();
                rotate180();
                break;
            case UP:
                rotateLeft();
                moveLeft();
                rotateRight();
                break;
            case DOWN:
                rotateRight();
                moveLeft();
                rotateLeft();
                break;
        }

        if (!sameBoard(oldBoard, board)) {
            addRandomTile();
            updateUI();
            updateScore();
            checkGame();
        }
    }

    private void moveLeft() {
        for (int r = 0; r < 4; r++) {
            int[] newRow = new int[4];
            int pos = 0;

            for (int c = 0; c < 4; c++) {
                if (board[r][c] != 0) {
                    if (newRow[pos] == 0) {
                        newRow[pos] = board[r][c];
                    } else if (newRow[pos] == board[r][c]) {
                        newRow[pos] *= 2;
                        score += newRow[pos];
                        pos++;
                    } else {
                        pos++;
                        newRow[pos] = board[r][c];
                    }
                }
            }
            board[r] = newRow;
        }
    }

    private void rotateRight() {
        board = rotate(board);
    }

    private void rotateLeft() {
        board = rotate(board);
        board = rotate(board);
        board = rotate(board);
    }

    private void rotate180() {
        board = rotate(board);
        board = rotate(board);
    }

    private int[][] rotate(int[][] m) {
        int[][] res = new int[4][4];
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                res[c][3 - r] = m[r][c];
            }
        }
        return res;
    }

    private void addRandomTile() {
        Random rand = new Random();
        while (true) {
            int row = rand.nextInt(4);
            int col = rand.nextInt(4);
            if (board[row][col] == 0) {
                board[row][col] = rand.nextInt(10) < 9 ? 2 : 4;
                break;
            }
        }
    }

    private void updateUI() {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                int value = board[r][c];
                Label tile = tiles[r][c];

                tile.setText(value == 0 ? "" : String.valueOf(value));

                String color = getColor(value);
                tile.setStyle("-fx-alignment: center; -fx-font-size: 28; -fx-font-weight: bold; " +
                        "-fx-background-color: " + color + "; " +
                        "-fx-border-color: #bbada0; -fx-border-width: 2;");
            }
        }
    }

    private void updateScore() {
        scoreLabel.setText("Score: " + score);

        if (score > highScore) {
            highScore = score;
            highScoreLabel.setText("High Score: " + highScore);
            currentUser.setHighScore(highScore);
        }
    }

    private String getColor(int v) {
        switch (v) {
            case 2:   return "#fde2e4";
            case 4:   return "#fad2e1";
            case 8:   return "#e2ece9";
            case 16:  return "#bee1e6";
            case 32:  return "#cde4b8";
            case 64:  return "#fff1b6";
            case 128: return "#ffcdb2";
            case 256: return "#d8b4fe";
            case 512: return "#cdb4db";
            case 1024: return "#a2d2ff";
            case 2048: return "#ffadc4";
            default:  return "#f0e6e6";
        }
    }

    private void checkGame() {
        boolean has2048 = false;
        for (int[] row : board) {
            for (int v : row) {
                if (v == 2048) has2048 = true;
            }
        }

        if (has2048) {
            showAlert("Congratulations! You reached 2048!", "You Win!");
        }

        if (!canMove()) {
            showAlert("Game Over!\nYour final score: " + score, "Game Over");
        }
    }

    private void showAlert(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean canMove() {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (board[r][c] == 0) return true;
                if (r < 3 && board[r][c] == board[r + 1][c]) return true;
                if (c < 3 && board[r][c] == board[r][c + 1]) return true;
            }
        }
        return false;
    }

    private void saveCurrentGame() {
        FileManager.saveGame(board, score, currentUser);
        showAlert("Game saved successfully!", "Save Game");
    }

    private void loadSavedGame() {
        Object[] loaded = FileManager.loadGame();
        if (loaded != null) {
            board = (int[][]) loaded[0];
            score = (int) loaded[1];
            updateUI();
            updateScore();
            showAlert("Game loaded successfully!", "Load Game");
        } else {
            showAlert("No saved game found!", "Load Game");
        }
    }

    private void logout(Stage stage) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Logout");
        confirm.setContentText("Do you want to logout?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                LoginScreen.show(stage);
            }
        });
    }

    private int[][] copyBoard() {
        int[][] copy = new int[4][4];
        for (int r = 0; r < 4; r++) {
            System.arraycopy(board[r], 0, copy[r], 0, 4);
        }
        return copy;
    }

    private boolean sameBoard(int[][] a, int[][] b) {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (a[r][c] != b[r][c]) return false;
            }
        }
        return true;
    }
}