/*
 * Copyright (c) 2024 Matthew Mokhles
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.example.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class QuizCardLayout implements Initializable {
    public Label title;

    public QuizCardLayout() {
    }

    public Label noq;

    public Button startButton;
    private Quiz quiz;
    private double xOffset = 0;
    private double yOffset = 0;


    public QuizCardLayout(Label title, Label noq, Button startButton, Quiz quiz) {
        this.title = title;
        this.noq = noq;
        this.startButton = startButton;
        this.quiz = quiz;
    }


//    private NewScreenListner screenListner;

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        this.title.setText(this.quiz.getTitle());
    }

//    public void setScreenListner(NewScreenListner screenListner) {
//    this.screenListner=screenListner;
//    }
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {

       /* HashMap quizes = Quiz.getAllWithQuetionCount();
        Set<Quiz> key=quizes.keySet();
        for(Quiz quiz:key){
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("QuizCardLayOut.fxml"));
            try {
                Node node = fxmlLoader.load();
                QuizCardLayout quizCardLayout = fxmlLoader.getController();
                quizCardLayout.setTitle(quiz.getTitle());
                quizCardLayout.setNoq(quizes.get(quiz)+"");
                QuizListController.getChildren().add(node);
            }
            catch (IOException e) {
                e.printStackTrace();

            }
            }
        }*/
    }
    public void setNoq(String value){
        this.noq.setText(value);
    }

    public void startQuiz(ActionEvent actionEvent) throws IOException {
    QuestionScreenController.setQuiz(quiz);
    switchToQuestionScreen(actionEvent);
    }
    public void switchToQuestionScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("QuestionScreenController.fxml")));
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
