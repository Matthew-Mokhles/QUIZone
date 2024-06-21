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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class QuizListController implements Initializable {
    @FXML private FlowPane quizlistController;

    public QuizListController() {
    }


    @FXML
    private AnchorPane loadingPane;
    @FXML private Label header;
    private double xOffset = 0;
    private double yOffset = 0;
    private static String title = null;
    private static Student student =null;

    public static void setStudent(Student student) {
        QuizListController.student = student;
    }

    public static void setTitle(String title) {
        QuizListController.title = title;
    }

    //    Map<Quiz,Integer> quizes=null;
//    private NewScreenListner screenListner;
//    private Set<Quiz>key;
//    public void setScreenListner(NewScreenListner screenListner){
//        this.screenListner=screenListner;
//        setCards();
//
//    }
public void exit(ActionEvent event) {
    System.out.println("Exit.....");
    System.exit(0);
}
    public void switchToStuLog(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Student-login.fxml")));
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
    private void setCards() {
        ArrayList<Quiz> quizArrayList = getQuizzes();

        if (quizArrayList != null) {
            try {
                for (Quiz quiz : quizArrayList) {
                    FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("QuizCardLayout.fxml")));
                    Parent root = fxmlLoader.load();

                    QuizCardLayout quizCardLayout = fxmlLoader.getController();

                    // Set the quiz and number of questions
                    quizCardLayout.setQuiz(quiz);
                    quizCardLayout.setNoq("Number Of Questions: " + quiz.getTotalQuestions());

                    // Add the created QuizCardLayout to the quiz listController
                    quizlistController.getChildren().add(root);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void refreshQuizes(){
        loadingPane.setVisible(true);
        Thread thread = new Thread(()->{
            try {Platform.runLater(() ->quizlistController.getChildren().clear());
                Platform.runLater(this::setCards);
            }catch (Exception e){
                e.printStackTrace();
            }
            finally {
                Platform.runLater(() -> loadingPane.setVisible(false));
            }
        });
        thread.start();
    }
    private ArrayList<Quiz> getQuizzes() {
        ArrayList<Quiz> quizArrayList = new ArrayList<>();
        String sql = "SELECT q.QuizID, q.CourseID, q.QuizTitle, q.TotalQuestions " +
                "FROM quiz_mangement.registration r " +
                "JOIN quiz_mangement.quizzes q ON r.CourseID = q.CourseID " +
                "LEFT JOIN quiz_mangement.quizattempts a ON q.QuizID = a.QuizID AND a.StudentID = r.StudentID " +
                "WHERE r.StudentID = ? AND a.QuizID IS NULL";

        try {
            // Assuming student.getId() returns the ID of the student
            JDBC jdbc = new JDBC();
            PreparedStatement preparedStatement = jdbc.getPreparedstatement(sql);
            preparedStatement.setInt(1, student.getId()); // Using a prepared statement to avoid SQL injection
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Retrieve quiz details from the result set
                int quizID = resultSet.getInt("QuizID");
                String courseID = resultSet.getString("CourseID");
                String quizTitle = resultSet.getString("QuizTitle");
                int totalQuestions = resultSet.getInt("TotalQuestions");

                // Create a new Quiz object using the retrieved details
                Quiz quiz = new Quiz(quizID, quizTitle, courseID, totalQuestions);
                // Add the quiz to the list
                quizArrayList.add(quiz);
            }
            // Check if no quizzes were found
            if (quizArrayList.isEmpty()) {
                // Return null to indicate that no quizzes were found
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
        return quizArrayList;
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    setCards();
    header.setText(title);
    }
}
