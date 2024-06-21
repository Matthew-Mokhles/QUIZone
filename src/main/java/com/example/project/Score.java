package com.example.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Score implements Initializable {
    private static String score;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private Label showScore;
    @FXML
    private Button backHome;

    public static void setScore(String score) {
        Score.score = score;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showScore.setText(score);
        backHome.setOnAction(e-> {
            try {
                switchToStuHome(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    public void switchToStuHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("QuizListController.fxml")));

        // If event is null, use the 'back' button to get the current stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


        Scene scene = new Scene(root);
        root.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        root.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
        stage.setScene(scene);
        stage.show();
    }
}
