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

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;


public class HelloController {
    private Stage stage;
    private  static String rootFXML;
    @FXML
    private WebView webView;
    @FXML
    private Label label;
    @FXML
    private ImageView imageView;
    @FXML
    private Label loginStatus;
    @FXML
    private TextField id;
    @FXML
    private TextField password;
    private WebEngine webEngine;
    private double xOffset = 0;
    private double yOffset = 0;
    private Scene scene;
    private Parent root;

    public void exit(ActionEvent event) {
        System.out.println("Exit.....");
        System.exit(0);
    }

    public void HyperLink(ActionEvent event) throws IOException {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setX(200);
        stage.setY(150);
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Hyperlink.fxml")));
        initialize();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void switchToFirstScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Home.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
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

    public void switchToStuLog(ActionEvent event) throws IOException {
        rootFXML = "studentlogin";
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Student-login.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
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
    public void switchToProfLog(ActionEvent event) throws IOException {
        rootFXML = "proflog";
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("prof-log.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
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
    public void switchToProfHome(Stage stage) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Prof-home.fxml")));
        scene = new Scene(root);
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
    public void switchToStudentHome(Stage stage) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("QuizListController.fxml")));
        scene = new Scene(root);
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
    public void switchToQuestionBank(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("questionBank.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
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
    public void switchToQuizmaker(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Quizmaker.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
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
    @FXML
    public void initialize() {
        if (webView != null) {
            webEngine = webView.getEngine();
            webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
                if (newState == Worker.State.FAILED) {
                    System.out.println("Failed to load page: " + webEngine.getLocation());
                    label.setText("Unable to connect.....Check Your Connection!!");
                    imageView.setImage(new Image("C:/Users/Matthew/IdeaProjects/Project/src/main/resources/Pic/warning.png"));
                }
            });
            webEngine.load("https://recovery.aast.edu/students/resetpass");
        }
    }
    public boolean validateStu(int id, int Password) throws SQLException {
        JDBC jdbc = new JDBC("SELECT * FROM quiz_mangement.students");
        ResultSet resultSet = jdbc.executeQuery();
        while (resultSet.next()){
       if(Integer.parseInt(resultSet.getString("StudentID")) == id && Integer.parseInt(resultSet.getString("PIN_Code")) == Password){
           Student student =new Student(Integer.parseInt(resultSet.getString("StudentID")),resultSet.getString("Name"),resultSet.getString("PIN_Code"));
           QuizListController.setStudent(student);
           QuestionScreenController.setStudentId(student.getId());
           QuizListController.setTitle("Hello "+ resultSet.getString("Name"));
        jdbc.closeConnection();
        return true;
       }
        }
        return false;
    }
    public boolean validateProf(int id, int Password) throws SQLException {
        JDBC jdbc = new JDBC("SELECT * FROM quiz_mangement.professors");
        ResultSet resultSet = jdbc.executeQuery();
        while (resultSet.next()){
            if(Integer.parseInt(resultSet.getString("ProfessorID")) == id && Integer.parseInt(resultSet.getString("PIN_Code")) == Password){
                QuestionController.setTitle("Hello "+resultSet.getString("FirstName")+" "+resultSet.getString("LastName"));
                jdbc.closeConnection();
                return true;
            }
        }
        return false;
    }
    public boolean validateAdmin(int id, int Password) throws SQLException {
        JDBC jdbc = new JDBC("SELECT * FROM quiz_mangement.admin");
        ResultSet resultSet = jdbc.executeQuery();
        while (resultSet.next()){
            if(Integer.parseInt(resultSet.getString("id")) == id && Integer.parseInt(resultSet.getString("pincode")) == Password){
                AdminController.setTitle(resultSet.getString("name"));
                System.out.println(AdminController.title);
                jdbc.closeConnection();
                return true;
            }
        }
        return false;
    }
    public void Login(ActionEvent event) {
        loginStatus.setTextFill(Color.RED);
        if(id.getText().isBlank()&&password.getText().isBlank()){
            loginStatus.setText("Nothing entered");
            System.out.println(rootFXML);
        }
        else if(id.getText().isBlank()&&!password.getText().isBlank()){
            loginStatus.setText("id is not entered");
        } else if (!id.getText().isBlank()&&password.getText().isBlank()) {
            loginStatus.setText("Pin Code is not entered");
        }
        else {
            int currentid = Integer.parseInt(id.getText().trim());
            int currentpassword = Integer.parseInt(password.getText().trim());
            loginStatus.setText("");
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.8), e -> {
                try {
                    if (rootFXML.equals("studentlogin")) {
                        System.out.println("student login");
                        if (validateStu(currentid, currentpassword)) {
                            loginStatus.setText("Login Successfully");
                            loginStatus.setTextFill(Color.GREEN);
                            PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
                            pause.setOnFinished(evt -> {
                                try {
                                    switchToStudentHome((Stage) ((Node) event.getSource()).getScene().getWindow());
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            });
                            pause.play();
                        } else {
                            loginStatus.setText("Incorrect Student ID or PIN code");
                        }
                    } else if (rootFXML.equals("proflog")) {
                        System.out.println("Professor login");
                        if (validateProf(currentid, currentpassword)){
                            loginStatus.setText("Login Successfully");
                            loginStatus.setTextFill(Color.GREEN);
                            PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
                            pause.setOnFinished(evt -> {
                                try {
                                    switchToProfHome((Stage) ((Node) event.getSource()).getScene().getWindow());
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            });
                            pause.play();
                        } else {
                            loginStatus.setText("Incorrect Professor ID or PIN code");
                        }

                    } else if (rootFXML.equals("adminlog")){
                        System.out.println("Admin login");
                        if (validateAdmin(currentid, currentpassword)){
                            loginStatus.setText("Login Successfully");
                            loginStatus.setTextFill(Color.GREEN);
                            PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
                            pause.setOnFinished(evt -> {
                                try {
                                    switchToadmin((Stage) ((Node) event.getSource()).getScene().getWindow());
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            });
                            pause.play();

                        } else {
                            loginStatus.setText("Incorrect Professor ID or PIN code");
                        }
                    }
                }catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }));
            timeline.play();
        }


    }
    public void switchToadminlog(ActionEvent event) throws IOException {
        rootFXML = "adminlog";
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admin-log.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
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
    public void switchToadmin(Stage stage) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admin.fxml")));
        scene = new Scene(root);
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
