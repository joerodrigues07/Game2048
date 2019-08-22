/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author João Soares
 */
public class MainController implements Initializable {

    private final int MAX = 3;
    private final int MIN = 0;
    private final int SIZE = 4;
    private int INSERT;
    private int POINT;
    private int BEST;
    private int BACK;
    private final int[][] GRID = new int[SIZE][SIZE];
    private final int[][] GRID_AUX = new int[SIZE][SIZE];
    private boolean MOVED;
    private ArrayList<Move> MOVES;
    private double X = 0, Y = 0;

    Stage stage;

    @FXML
    AnchorPane parent;

    @FXML
    Label btnFechar;

    @FXML
    Label score;

    @FXML
    Label best;

    @FXML
    Label cell00;

    @FXML
    Label cell01;

    @FXML
    Label cell02;

    @FXML
    Label cell03;

    @FXML
    Label cell10;

    @FXML
    Label cell11;

    @FXML
    Label cell12;

    @FXML
    Label cell13;

    @FXML
    Label cell20;

    @FXML
    Label cell21;

    @FXML
    Label cell22;

    @FXML
    Label cell23;

    @FXML
    Label cell30;

    @FXML
    Label cell31;

    @FXML
    Label cell32;

    @FXML
    Label cell33;

    @FXML
    Label btnNew;

    @FXML
    Label btnUndo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        INSERT = getRandomNumberInRange(4, 7);
        POINT = 0;
        BACK = 1;

        loadValues();
        populateGrid();

        parent.setOnKeyReleased(e -> {
            MOVED = false;
            if (null != e.getCode()) {
                switch (e.getCode()) {
                    case UP:
                    case RIGHT:
                    case LEFT:
                    case DOWN:
                        move(e.getCode().toString());
                        if (MOVED) {
                            insertRandom();
                            populateGrid();
                            BACK = 1;
                            MOVES.add(new Move(copyGrid(), POINT));
                        }
                        break;
                    case ESCAPE:
                        close();
                        break;
                    case BACK_SPACE:
                        undo();
                        break;
                    case N:
                        newGame();
                        break;
                    default:
                        break;
                }
            }
            if (!validMoves() && !existsEmpty()) {
                makeAlert();
            }
        });

        btnFechar.setOnMouseClicked(e -> {
            close();
        });

        btnNew.setOnMouseClicked(e -> {
            newGame();
        });

        btnUndo.setOnMouseClicked(e -> {
            undo();
        });

        makeDraggable();
    }

    private void insertRandom() {
        int c, l;
        while (1 == 1) {
            c = getRandomNumberInRange(MIN, MAX);
            l = getRandomNumberInRange(MIN, MAX);
            if (GRID[l][c] == 0) {
                INSERT--;

                GRID[l][c] = INSERT > 0 ? 2 : 4;
                if (INSERT == 0) {
                    INSERT = getRandomNumberInRange(4, 7);
                }
                break;
            }
        }
    }

    private void undo() {
        if (MOVES.size() > 1) {
            if (BACK == 1) {
                MOVES.remove(MOVES.size() - 1);
                BACK = 0;
            }
            Move m = MOVES.get(MOVES.size() - 1);
            score.setText(String.valueOf(m.getPoint()));
            POINT = m.getPoint();
            copyGrid(m.getGrid());
            MOVES.remove(MOVES.size() - 1);
            populateGrid();
        }
    }

    private void clear() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                GRID[i][j] = 0;
                GRID_AUX[i][j] = 0;
            }
        }
        X = Y = 0;
        MOVES = new ArrayList<>();
        POINT = 0;

        score.setText("0");

        cell00.getStyleClass().clear();
        cell00.getStyleClass().add("cell-empty");

        cell01.getStyleClass().clear();
        cell01.getStyleClass().add("cell-empty");

        cell02.getStyleClass().clear();
        cell02.getStyleClass().add("cell-empty");

        cell03.getStyleClass().clear();
        cell03.getStyleClass().add("cell-empty");

        cell10.getStyleClass().clear();
        cell10.getStyleClass().add("cell-empty");

        cell11.getStyleClass().clear();
        cell11.getStyleClass().add("cell-empty");

        cell12.getStyleClass().clear();
        cell12.getStyleClass().add("cell-empty");

        cell13.getStyleClass().clear();
        cell13.getStyleClass().add("cell-empty");

        cell20.getStyleClass().clear();
        cell20.getStyleClass().add("cell-empty");

        cell21.getStyleClass().clear();
        cell21.getStyleClass().add("cell-empty");

        cell22.getStyleClass().clear();
        cell22.getStyleClass().add("cell-empty");

        cell23.getStyleClass().clear();
        cell23.getStyleClass().add("cell-empty");

        cell30.getStyleClass().clear();
        cell30.getStyleClass().add("cell-empty");

        cell31.getStyleClass().clear();
        cell31.getStyleClass().add("cell-empty");

        cell32.getStyleClass().clear();
        cell32.getStyleClass().add("cell-empty");

        cell33.getStyleClass().clear();
        cell33.getStyleClass().add("cell-empty");

        insertRandom();
        insertRandom();

    }

    private int getRandomNumberInRange(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    private void move(String dir) {
        switch (dir) {
            case "UP":
                moveUp();
                break;
            case "LEFT":
                moveLeft();
                break;
            case "RIGHT":
                moveRight();
                break;
            case "DOWN":
                moveDown();
                break;
            default:
                break;
        }
        score.setText(String.valueOf(POINT));
    }

    private void populateGrid() {
        String style, value;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {

                value = GRID[i][j] == 0 ? "" : String.valueOf(GRID[i][j]);
                style = "cell-" + (GRID[i][j] == 0 ? "empty" : String.valueOf(GRID[i][j]));

                switch (String.valueOf(i) + String.valueOf(j)) {
                    case "00":
                        cell00.getStyleClass().clear();
                        cell00.getStyleClass().add(style);
                        cell00.setText(value);
                        break;
                    case "01":
                        cell01.getStyleClass().clear();
                        cell01.getStyleClass().add(style);
                        cell01.setText(value);
                        break;
                    case "02":
                        cell02.getStyleClass().clear();
                        cell02.getStyleClass().add(style);
                        cell02.setText(value);
                        break;
                    case "03":
                        cell03.getStyleClass().clear();
                        cell03.getStyleClass().add(style);
                        cell03.setText(value);
                        break;
                    case "10":
                        cell10.getStyleClass().clear();
                        cell10.getStyleClass().add(style);
                        cell10.setText(value);
                        break;
                    case "11":
                        cell11.getStyleClass().clear();
                        cell11.getStyleClass().add(style);
                        cell11.setText(value);
                        break;
                    case "12":
                        cell12.getStyleClass().clear();
                        cell12.getStyleClass().add(style);
                        cell12.setText(value);
                        break;
                    case "13":
                        cell13.getStyleClass().clear();
                        cell13.getStyleClass().add(style);
                        cell13.setText(value);
                        break;
                    case "20":
                        cell20.getStyleClass().clear();
                        cell20.getStyleClass().add(style);
                        cell20.setText(value);
                        break;
                    case "21":
                        cell21.getStyleClass().clear();
                        cell21.getStyleClass().add(style);
                        cell21.setText(value);
                        break;
                    case "22":
                        cell22.getStyleClass().clear();
                        cell22.getStyleClass().add(style);
                        cell22.setText(value);
                        break;
                    case "23":
                        cell23.getStyleClass().clear();
                        cell23.getStyleClass().add(style);
                        cell23.setText(value);
                        break;
                    case "30":
                        cell30.getStyleClass().clear();
                        cell30.getStyleClass().add(style);
                        cell30.setText(value);
                        break;
                    case "31":
                        cell31.getStyleClass().clear();
                        cell31.getStyleClass().add(style);
                        cell31.setText(value);
                        break;
                    case "32":
                        cell32.getStyleClass().clear();
                        cell32.getStyleClass().add(style);
                        cell32.setText(value);
                        break;
                    case "33":
                        cell33.getStyleClass().clear();
                        cell33.getStyleClass().add(style);
                        cell33.setText(value);
                        break;
                }
            }
        }
    }

    private void makeDraggable() {
        parent.setOnMousePressed(e -> {
            X = e.getSceneX();
            Y = e.getSceneY();
            parent.setCursor(Cursor.CLOSED_HAND);
        });

        parent.setOnMouseDragged(e -> {
            stage = (Stage) parent.getScene().getWindow();
            stage.setX(e.getScreenX() - X);
            stage.setY(e.getScreenY() - Y);
            stage.setOpacity(0.6f);
        });

        parent.setOnDragDone(e -> {
            stage = (Stage) parent.getScene().getWindow();
            stage.setOpacity(1.0f);
        });

        parent.setOnMouseReleased(e -> {
            stage = (Stage) parent.getScene().getWindow();
            stage.setOpacity(1.0f);
            parent.setCursor(Cursor.DEFAULT);
        });
    }

    private int[][] copyGrid() {
        int[][] g = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(GRID[i], 0, g[i], 0, SIZE);
        }
        return g;
    }

    private void copyGridAux() {
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(GRID[i], 0, GRID_AUX[i], 0, SIZE);
        }
    }

    private void copyGrid(int[][] g) {
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(g[i], 0, GRID[i], 0, SIZE);
        }
    }

    private void verifyIfIsMoved() {
        for (int i = 0; i < SIZE; i++) {
            if (MOVED) {
                break;
            }
            for (int j = 0; j < SIZE; j++) {
                if (GRID[i][j] != GRID_AUX[i][j]) {
                    MOVED = true;
                    break;
                }
            }
        }
    }

    private void moveUp() {
        copyGridAux();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = j + 1; k < 4; k++) {
                    if (GRID[j][i] == 0) {
                        GRID[j][i] = GRID[k][i];
                        GRID[k][i] = 0;
                    }
                }
            }
            if (GRID[0][i] == GRID[1][i] && GRID[1][i] != 0) {
                GRID[0][i] += GRID[1][i];
                GRID[1][i] = 0;
                POINT += GRID[0][i];
            }
            if (GRID[1][i] == GRID[2][i] && GRID[2][i] != 0) {
                GRID[1][i] += GRID[2][i];
                GRID[2][i] = 0;
                POINT += GRID[1][i];
            }
            if (GRID[2][i] == GRID[3][i] && GRID[3][i] != 0) {
                GRID[2][i] += GRID[3][i];
                GRID[3][i] = 0;
                POINT += GRID[2][i];
            }
            for (int j = 0; j < 3; j++) {
                for (int k = j + 1; k < 4; k++) {
                    if (GRID[j][i] == 0) {
                        GRID[j][i] = GRID[k][i];
                        GRID[k][i] = 0;
                    }
                }
            }
        }
        verifyIfIsMoved();
    }

    private void moveLeft() {
        copyGridAux();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = j + 1; k < 4; k++) {
                    if (GRID[i][j] == 0) {
                        GRID[i][j] = GRID[i][k];
                        GRID[i][k] = 0;
                    }
                }
            }
            if (GRID[i][0] == GRID[i][1] && GRID[i][1] != 0) {
                GRID[i][0] += GRID[i][1];
                GRID[i][1] = 0;
                POINT += GRID[i][0];
            }
            if (GRID[i][1] == GRID[i][2] && GRID[i][2] != 0) {
                GRID[i][1] += GRID[i][2];
                GRID[i][2] = 0;
                POINT += GRID[i][1];
            }
            if (GRID[i][2] == GRID[i][3] && GRID[i][3] != 0) {
                GRID[i][2] += GRID[i][3];
                GRID[i][3] = 0;
                POINT += GRID[i][2];
            }
            for (int j = 0; j < 3; j++) {
                for (int k = j + 1; k < 4; k++) {
                    if (GRID[i][j] == 0) {
                        GRID[i][j] = GRID[i][k];
                        GRID[i][k] = 0;
                    }
                }
            }
        }
        verifyIfIsMoved();
    }

    private void moveRight() {
        copyGridAux();
        for (int i = 3; i >= 0; i--) {
            for (int j = 3; j > 0; j--) {
                for (int k = j - 1; k >= 0; k--) {
                    if (GRID[i][j] == 0) {
                        GRID[i][j] = GRID[i][k];
                        GRID[i][k] = 0;
                    }
                }
            }
            if (GRID[i][3] == GRID[i][2] && GRID[i][2] != 0) {
                GRID[i][3] += GRID[i][2];
                GRID[i][2] = 0;
                POINT += GRID[i][3];
            }
            if (GRID[i][2] == GRID[i][1] && GRID[i][1] != 0) {
                GRID[i][2] += GRID[i][1];
                GRID[i][1] = 0;
                POINT += GRID[i][2];
            }
            if (GRID[i][1] == GRID[i][0] && GRID[i][0] != 0) {
                GRID[i][1] += GRID[i][0];
                GRID[i][0] = 0;
                POINT += GRID[i][1];
            }
            for (int j = 3; j > 0; j--) {
                for (int k = j - 1; k >= 0; k--) {
                    if (GRID[i][j] == 0) {
                        GRID[i][j] = GRID[i][k];
                        GRID[i][k] = 0;
                    }
                }
            }
        }
        verifyIfIsMoved();
    }

    private void moveDown() {
        copyGridAux();
        for (int i = 3; i >= 0; i--) {
            for (int j = 3; j > 0; j--) {
                for (int k = j - 1; k >= 0; k--) {
                    if (GRID[j][i] == 0) {
                        GRID[j][i] = GRID[k][i];
                        GRID[k][i] = 0;
                    }
                }
            }
            if (GRID[3][i] == GRID[2][i] && GRID[2][i] != 0) {
                GRID[3][i] += GRID[2][i];
                GRID[2][i] = 0;
                POINT += GRID[3][i];
            }
            if (GRID[2][i] == GRID[1][i] && GRID[1][i] != 0) {
                GRID[2][i] += GRID[1][i];
                GRID[1][i] = 0;
                POINT += GRID[2][i];
            }
            if (GRID[1][i] == GRID[0][i] && GRID[0][i] != 0) {
                GRID[1][i] += GRID[0][i];
                GRID[0][i] = 0;
                POINT += GRID[1][i];
            }
            for (int j = 3; j > 0; j--) {
                for (int k = j - 1; k >= 0; k--) {
                    if (GRID[j][i] == 0) {
                        GRID[j][i] = GRID[k][i];
                        GRID[k][i] = 0;
                    }
                }
            }
        }
        verifyIfIsMoved();
    }

    private boolean validMoves() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int atual = GRID[i][j];
                int up, down, left, right;

                up = i - 1 >= 0 ? (i - 1) : -1;
                down = i + 1 < 4 ? (i + 1) : -1;
                left = j - 1 >= 0 ? (j - 1) : -1;
                right = j + 1 < 4 ? (j + 1) : -1;

                if (right != -1) {
                    if (atual == GRID[i][right]) {
                        return true;
                    }
                }

                if (left != -1) {
                    if (atual == GRID[i][left]) {
                        return true;
                    }
                }

                if (up != -1) {
                    if (atual == GRID[up][j]) {
                        return true;
                    }
                }

                if (down != -1) {
                    if (atual == GRID[down][j]) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    private boolean existsEmpty() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (GRID[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void makeAlert() {

        try {
            Stage s = new Stage();
            s.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Alert.fxml"));
            Parent p = loader.load();

            Scene scene = new Scene(p);
            s.setScene(scene);
            s.initStyle(StageStyle.UNDECORATED);

            X = parent.getScene().getWindow().getX();
            Y = parent.getScene().getWindow().getY();

            AlertController alert = loader.getController();
            alert.setStage(s);
            alert.setParent((AnchorPane) p);
            alert.actions(X + 16, Y + 204, this);

            s.show();
            s.toFront();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void newGame() {
        int actual = POINT > BEST ? POINT : BEST;
        clear();
        saveValues(actual, 0, GRID);
        loadValues();
        populateGrid();
    }

    private void close() {
        saveValues(BEST, POINT, GRID);
        parent.getScene().getWindow().hide();
    }

    private void loadValues() {
        try {
            File file = new File(getHistoryGamePath());
            if (file.exists()) {
                FileReader read = new FileReader(file);
                BufferedReader reader = new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("best")) {
                        this.best.setText(Crypt.decrypt(Crypt.unformat(line.split(":")[1].trim(), 12)));
                        BEST = Integer.parseInt(this.best.getText());
                    } else if (line.contains("score")) {
                        this.score.setText(Crypt.decrypt(Crypt.unformat(line.split(":")[1].trim(), 12)));
                        POINT = Integer.parseInt(this.score.getText());
                    } else if (line.contains("grid")) {
                        String values[] = line.replace("grid:,", "").split(",");
                        int c = 0;
                        for (int i = 0; i < SIZE; i++) {
                            for (int j = 0; j < SIZE; j++, c++) {
                                GRID[i][j] = Integer.parseInt(Crypt.decrypt(Crypt.unformat(values[c], c * c)));
                            }
                        }
                    }
                }
                MOVES = new ArrayList<>();
            } else {
                clear();
                this.best.setText("0");
                BEST = 0;
            }
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void saveValues(int best, int score, int[][] grid) {
        try {

            File file = new File(getHistoryGamePath());

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getCanonicalFile());
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write("best:" + Crypt.format(Crypt.encrypt(String.valueOf(best)), 12));
                bw.write("\n");
                bw.write("score:" + Crypt.format(Crypt.encrypt(String.valueOf(score)), 12));
                bw.write("\n");
                String l = "grid:";
                int c = 0;
                for (int i = 0; i < SIZE; i++) {
                    for (int j = 0; j < SIZE; j++, c++) {
                        l += "," + Crypt.format(Crypt.encrypt(String.valueOf(grid[i][j])), c * c);
                    }
                }
                bw.write(l);
                bw.write("\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getHistoryGamePath() {
        String path = System.getenv("LOCALAPPDATA") + File.separator + "Game2048";

        File file = new File(path);

        if (!file.exists()) {
            file.mkdir();
        }
        return path + File.separator + "pontuation.txt";
    }
}
