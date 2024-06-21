package com.example.project;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class QuestionScreenController implements Initializable {
    public static String title;
    public Label header;
    public Label question;
    public RadioButton option1;
    public RadioButton option2;
    public RadioButton option3;
    public RadioButton option4;
    public ToggleGroup options;
    public Label timerLabel;
    private Timeline timeline;
    private int timeRemaining;
    private static Quiz quiz;
    private int id = 0;
    private int count;
    private List<Question> questions = new ArrayList<>();
    private List<studentAnswers> answersList = new ArrayList<>();
    private double xOffset = 0;
    private double yOffset = 0;
    public Button next;
    public Button back;
    @FXML
    private Button submit;
    private static int studentId;
    public FlowPane progresscontainer;
    private List<Progresscirclecontroller> progressControllers = new ArrayList<>();

    public static void setQuiz(Quiz quiz) {
        QuestionScreenController.quiz = quiz;
        title = quiz.getTitle();
    }

    public static void setStudentId(int studentId) {
        QuestionScreenController.studentId = studentId;
    }
    private int fetchQuizDuration() throws SQLException {
        String sql = "SELECT quizTime FROM quiz_mangement.quizzes WHERE QuizID = ?";
        JDBC jdbc = new JDBC();
        try {
            PreparedStatement preparedStatement = jdbc.getPreparedstatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String quizTime = resultSet.getString("quizTime");
                String[] parts = quizTime.split(":");
                int hours = Integer.parseInt(parts[0]);
                int minutes = Integer.parseInt(parts[1]);
                int seconds = Integer.parseInt(parts[2]);
                return (hours * 3600) + (minutes * 60) + seconds;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbc.closeConnection();
        }
        return 0;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questions.clear();
        header.setText(title);
        back.setText("Home");
        try {
            id = fetchQuizid();

            // Check if the student has already started this quiz
            if (hasStudentStartedQuiz(studentId, id)) {
                // Show a notification and redirect to the home screen or another appropriate screen
                Notifications.create()
                        .title("Quiz Already Started")
                        .text("You have already started this quiz.")
                        .darkStyle()
                        .position(Pos.BOTTOM_RIGHT)
                        .showWarning();

                Platform.runLater(() -> {
                    try {
                        switchToStuHome(null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                return;
            }

            // Mark the quiz as started
            markQuizAsStarted(studentId, id);

            if (questions.isEmpty()) {
                System.out.println("Inserting questions into the database.");
                QuizQuestionGenerator.insertQuestions(id, quiz.getTotalQuestions());
            }

            System.out.println("Fetching questions for quiz ID " + id);
            getQuestions();

            if (questions.isEmpty()) {
                System.out.println("No questions found.");
            } else {
                System.out.println("Questions fetched: " + questions.size());
            }

            renderProgress();
            try {
                timeRemaining = fetchQuizDuration();
                startCountdownTimer();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        submit.setDisable(true);
        count = 0;
        display(count);
        progressControllers.get(count).setCurrentQuestionColor();
    }
    private void startCountdownTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (timeRemaining > 0) {
                timeRemaining--;
                updateTimerLabel();
            } else {
                try {
                    endQuiz();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    private void updateTimerLabel() {
        int hours = timeRemaining / 3600;
        int minutes = (timeRemaining % 3600) / 60;
        int seconds = timeRemaining % 60;
        timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }
    private void endQuiz() throws IOException {
        timeline.stop();
        Notifications.create()
                .title("Time's Up!")
                .text("The quiz time has ended.")
                .darkStyle()
                .position(Pos.BOTTOM_RIGHT)
                .showWarning();
        submit(); // Automatically submit the quiz
    }

    public boolean hasStudentStartedQuiz(int studentId, int quizId) {
        JDBC jdbc = new JDBC();
        String sql = "SELECT COUNT(*) FROM QuizAttempts WHERE StudentID = ? AND QuizID = ?";
        try {
            PreparedStatement preparedStatement = jdbc.getPreparedstatement(sql);
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, quizId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // Return true if there's any record
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbc.closeConnection();
        }
        return false;
    }

    public void markQuizAsStarted(int studentId, int quizId) {
        JDBC jdbc = new JDBC();
        String sql = "INSERT INTO QuizAttempts (StudentID, QuizID) VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = jdbc.getPreparedstatement(sql);
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, quizId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbc.closeConnection();
        }
    }

    public void nextQuestions(ActionEvent event) {
        saveAnswer(); // Save the current answer before moving to the next question
        count++;
        updateNavigationButtonsState();
        display(count); // Update display to show the next question
        options.selectToggle(null); // Deselect all options
        loadAnswer(); // Load the saved answer if exists

        Platform.runLater(() -> {
            // Change the color of the current progress circle
            if (count < progressControllers.size()) {
                progressControllers.get(count).setCurrentQuestionColor();
            }
            if (count - 1 < progressControllers.size() && count > 0) {
                progressControllers.get(count - 1).setDefaultColor();
            }
        });
    }

    public void saveAnswer() {
        RadioButton selectedOption = (RadioButton) options.getSelectedToggle();
        if (selectedOption != null) {
            String answer = getSelectedOptionLabel(selectedOption);
            boolean isCorrect = validate(answer);

            if (count < answersList.size()) {
                answersList.get(count).setAnswer(answer);
                answersList.get(count).setCorrect(isCorrect);
            } else {
                studentAnswers studentAnswer = new studentAnswers(studentId, questions.get(count).getQuestionId(), answer, isCorrect);
                answersList.add(studentAnswer);
            }

            Notifications.create()
                    .title("Answer Saved")
                    .text("Your answer has been saved successfully.")
                    .darkStyle()
                    .position(Pos.TOP_RIGHT)
                    .showInformation();
        } else {
            Notifications.create()
                    .title("Question is not answered")
                    .text("Please make sure that you selected an option before submit.")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showWarning();
        }
    }

    private String getSelectedOptionLabel(RadioButton selectedOption) {
        if (selectedOption == option1) {
            return "First Choice";
        } else if (selectedOption == option2) {
            return "Second Choice";
        } else if (selectedOption == option3) {
            return "Third Choice";
        } else if (selectedOption == option4) {
            return "Fourth Choice";
        }
        return "";
    }

    private Boolean validate(String answer) {
        return answer.equals(questions.get(count).getCorrect());
    }

    public void back(ActionEvent event) throws IOException {
        saveAnswer(); // Save the current answer before moving back to the previous question
        if (count == 0) {
            switchToStuHome(event); // Navigate to student home if count is 0
        } else {
            count--;
            updateNavigationButtonsState();
            display(count); // Update display to show the previous question
            options.selectToggle(null); // Deselect all options
            loadAnswer(); // Load the saved answer if exists

            Platform.runLater(() -> {
                // Change the color of the current progress circle
                if (count < progressControllers.size()) {
                    progressControllers.get(count).setCurrentQuestionColor();
                }
                if (count + 1 < progressControllers.size() && count + 1 >= 0) {
                    progressControllers.get(count + 1).setDefaultColor();
                }
            });
        }
    }


    private void updateNavigationButtonsState() {
        back.setText(count == 0 ? "Home" : "Back");
        next.setDisable(count == questions.size() - 1);
        submit.setDisable(count != questions.size() - 1);
    }

    public void submit() throws IOException {
        if (timeline != null) {
            timeline.stop(); // Stop the timer if the quiz is submitted manually
        }
        int score = 0;
        saveAnswer();
        JDBC jdbc = new JDBC();

        try {
            for (studentAnswers answer : answersList) {
                int studentId = answer.getStudentID();
                int questionId = answer.getQuestionID();
                String studentAnswer = answer.getAnswer();
                int isCorrect = answer.isCorrect() ? 1 : 0; // Convert boolean to int for database

                if (answer.isCorrect()) {
                    score++;
                }

                String sql = "INSERT INTO StudentAnswers (StudentID, QuestionID, Answer, IsCorrect) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = jdbc.getPreparedstatement(sql);
                preparedStatement.setInt(1, studentId);
                preparedStatement.setInt(2, questionId);
                preparedStatement.setString(3, studentAnswer);
                preparedStatement.setInt(4, isCorrect);
                preparedStatement.executeUpdate();
            }

            answersList.clear();

            Notifications.create()
                    .title("Success")
                    .text("Student answers submitted successfully.")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showInformation();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbc.closeConnection();
        }

        insertResult(studentId, id, score);
        Score.setScore(score + "/" + questions.size());
        switchToScore(null);
    }

    private void loadAnswer() {
        if (count < answersList.size() && answersList.get(count) != null) {
            String savedAnswer = answersList.get(count).getAnswer();
            options.selectToggle(getRadioButtonForLabel(savedAnswer));
        } else {
            options.selectToggle(null); // Ensure no option is selected for unanswered questions
        }
    }

    private RadioButton getRadioButtonForLabel(String label) {
        switch (label) {
            case "First Choice":
                return option1;
            case "Second Choice":
                return option2;
            case "Third Choice":
                return option3;
            case "Fourth Choice":
                return option4;
            default:
                return null;
        }
    }
    public void insertResult(int studentId, int quizId, int score) {
        JDBC jdbc = new JDBC();
        PreparedStatement preparedStatement = null;

        try {
            // Construct the SQL query to insert the result
            String sql = "INSERT INTO Results (StudentID, QuizID, Score) VALUES (?, ?, ?)";
            preparedStatement = jdbc.getPreparedstatement(sql);

            // Set the parameters in the PreparedStatement
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, quizId);
            preparedStatement.setInt(3, score);

            // Execute the query
            preparedStatement.executeUpdate();

            // Optionally, display a success message
            System.out.println("Result inserted successfully.");
        } catch (SQLException e) {
            // Handle any potential SQL exceptions
            e.printStackTrace();
        } finally {
            // Close PreparedStatement and JDBC connection
            jdbc.closeConnection();
        }
    }

    public int fetchQuizid() throws SQLException {
        String sql = "SELECT * FROM quiz_mangement.quizzes";
        JDBC jdbc = new JDBC(sql);
        ResultSet resultSet = jdbc.executeQuery();
        while (resultSet.next()) {
            if (quiz.getTitle().equals(resultSet.getString("QuizTitle"))) {
                return resultSet.getInt("QuizID");
            }
        }
        return 0;
    }

    public void getQuestions() {
        if (questions.isEmpty()) {
            String sql = "SELECT ques.* " +
                    "FROM Quizzes q " +
                    "JOIN QuizQuestions qq ON q.QuizID = qq.QuizID " +
                    "JOIN Questions ques ON qq.QuestionID = ques.QuestionID " +
                    "WHERE q.QuizID = ? " +
                    "ORDER BY qq.QuizQuestionID DESC " +
                    "LIMIT ?";
            try {
                JDBC jdbc = new JDBC();
                PreparedStatement preparedStatement = jdbc.getPreparedstatement(sql);
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, quiz.getTotalQuestions());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt("QuestionID");
                    String questionText = resultSet.getString("QuestionText");
                    String ans1 = resultSet.getString("Answer1Text");
                    String ans2 = resultSet.getString("Answer2Text");
                    String ans3 = resultSet.getString("Answer3Text");
                    String ans4 = resultSet.getString("Answer4Text");
                    String correctAnswer = resultSet.getString("CorrectAnswerText");
                    String category = resultSet.getString("Category");
                    Question question = new Question(questionText, ans1, ans2, ans3, ans4, correctAnswer, category);
                    question.setQuestionId(id);
                    System.out.println(questionText);
                    System.out.println(id);
                    questions.add(question);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void display(int count) {
        if (questions.isEmpty()) {
            System.out.println("No questions to display.");
            return;
        }
        header.setText(title);
        question.setText(questions.get(count).getQuestion());
        option1.setText(questions.get(count).getAns1());
        option2.setText(questions.get(count).getAns2());
        option3.setText(questions.get(count).getAns3());
        option4.setText(questions.get(count).getAns4());
    }
    public void switchToStuHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("QuizListController.fxml")));

        // If event is null, use the 'back' button to get the current stage
        Stage stage;
        if (event != null) {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        } else {
            stage = (Stage) back.getScene().getWindow();
        }

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

    public void renderProgress() {
        progressControllers.clear(); // Clear the list before rendering
        for (int i = 0; i < questions.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("progresscirclecontroller.fxml"));
            try {
                Node node = fxmlLoader.load();
                Progresscirclecontroller pcirclecontroller = fxmlLoader.getController();
                pcirclecontroller.setNumber(i + 1);
                pcirclecontroller.setDefaultColor();
                progressControllers.add(pcirclecontroller); // Add the controller to the list
                progresscontainer.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void switchToScore(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Score.fxml")));
        // If event is null, use the 'back' button to get the current stage
        Stage stage;
        if (event != null) {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        } else {
            stage = (Stage) back.getScene().getWindow();
        }

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
