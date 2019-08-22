/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author João Soares
 */
public class AlertController extends AnchorPane {

    /**
     * Initializes the controller class.
     */
    private Stage stage;
    private double x = 0, y = 0;

    @FXML
    private AnchorPane parent;

    @FXML
    private Button btnTryAgain;

    public AlertController() {

    }

    public void setParent(AnchorPane p) {
        this.parent = p;
    }

    public void setStage(Stage s) {
        this.stage = s;
    }

    private void close() {
        stage.hide();
        stage.close();
    }

    public void actions(double x, double y, MainController m) {
        stage = (Stage) parent.getScene().getWindow();

        stage.setX(x);
        stage.setY(y);

        stage.setOpacity(0.8f);

        btnTryAgain.setOnMouseClicked(e -> {
            m.newGame();
            close();
        });

        parent.getScene().setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                m.newGame();
                close();
            }
        });
    }
}
