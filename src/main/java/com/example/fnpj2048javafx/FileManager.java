package com.example.fnpj2048javafx;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileManager {
    private static final String USERS_FILE = "users.dat";
    private static final String GAME_SAVE = "savegame.dat";

    private static Map<String, User> users = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            users = (Map<String, User>) ois.readObject();
        } catch (Exception e) {
            // No Comment
        }
    }

    public static void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean register(String username, String password) {
        loadUsers();
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, new User(username, password));
        saveUsers();
        return true;
    }

    public static User login(String username, String password) {
        loadUsers();
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public static void saveGame(int[][] board, int score, User user) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GAME_SAVE))) {
            oos.writeObject(board);
            oos.writeInt(score);
            oos.writeObject(user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object[] loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GAME_SAVE))) {
            int[][] board = (int[][]) ois.readObject();
            int score = ois.readInt();
            String username = (String) ois.readObject();
            return new Object[]{board, score, username};
        } catch (Exception e) {
            return null;
        }
    }
}