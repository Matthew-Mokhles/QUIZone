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

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;

public class Quizmaker implements Initializable {
    @FXML
    private Button refresh;


    @FXML
    private TextField Title;

    @FXML
    private TableColumn<Student, String> studentsColumn;

    @FXML
    private Label nameLabel;

    @FXML
    private Label numberOfQuestionsLabel;

    @FXML
    private Label wrongAnswersLabel;

    @FXML
    private Label correctAnswersLabel;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private TableView<Student> tabelstu;

    @FXML
    private Label Titletext;

    private final ObservableList<Student> studentObservableList = FXCollections.observableArrayList();
    @FXML
    private Button generatepdf;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private Label QuizTitle;

    @FXML
    private Label TotalQuestions;
    @FXML
    private Label Course;
    @FXML
    private Label QuizTime;

    @FXML
    private TextField title;

    @FXML
    private TextField numofQuestion;

    @FXML
    private Spinner<Integer> minutes;

    @FXML
    private Spinner<Integer> hours;

    @FXML
    private Spinner<Integer> seconds;

    @FXML
    private Button startbtn;
    @FXML
    private ChoiceBox<String> courseChoiceBox;
    @FXML
    private TableColumn<Quiz, String> QuizTitleColumn;
    @FXML
    private TableColumn<Quiz, String> CourseColumn;
    @FXML
    private TableColumn<Quiz, Integer> TotalQuestionsColumn;
    @FXML
    private TableColumn<Quiz, String> QuizTimeColumn;

    @FXML
    private TableView<Quiz> quizTableView;
    private final ObservableList<Quiz> quizObservableList = FXCollections.observableArrayList();
    private String quizID;
    @FXML
    private BarChart<String, Integer> statisticsChart;

    @FXML
    private CategoryAxis questionAxis;

    @FXML
    private NumberAxis countAxis;


    public void initialize(URL url, ResourceBundle rb) {
        if (progressIndicator != null) {
            progressIndicator.setProgress(0.0);
        }
        studentsColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        SpinnerValueFactory<Integer> minutesValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        minutes.setValueFactory(minutesValueFactory);
        SpinnerValueFactory<Integer> secondsValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        seconds.setValueFactory(secondsValueFactory);
        SpinnerValueFactory<Integer> hoursValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
        hours.setValueFactory(hoursValueFactory);
        QuizTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        CourseColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        TotalQuestionsColumn.setCellValueFactory(new PropertyValueFactory<>("TotalQuestions"));
        quizTableView.setItems(quizObservableList);
        // Populate the ChoiceBox with course options
        courseChoiceBox.getItems().addAll("Advanced Programming Applications", "Object-Oriented Programming");
        quizTableView.setOnMouseClicked(e -> {
            Quiz quiz = quizTableView.getSelectionModel().getSelectedItem();
            if (quiz != null) {
                QuizTitle.setText(quiz.getTitle());
                TotalQuestions.setText(String.valueOf(quiz.getTotalQuestions()));
                Course.setText(quiz.getCourseName());
                QuizTime.setText(quiz.getQuizTime());
            }
        });
        refreshquizTable();
        questionAxis = new CategoryAxis();
        countAxis = new NumberAxis();
        statisticsChart.setTitle("Question Statistics");
        questionAxis.setLabel("Question");
        countAxis.setLabel("Number of Answers");
        statisticsChart.setVerticalGridLinesVisible(false);
    }
    private String getQuizTime() {
        int hoursValue = hours.getValue() != null ? hours.getValue() : 0;
        int minutesValue = minutes.getValue() != null ? minutes.getValue() : 0;
        int secondsValue = seconds.getValue() != null ? seconds.getValue() : 0;

        // Format time to HH:MM:SS
        return String.format("%02d:%02d:%02d", hoursValue, minutesValue, secondsValue);
    }
    private List<Question> getQuestionsByIds(List<Integer> questionIDs) {
        List<Question> questions = new ArrayList<>();
        JDBC jdbc = new JDBC();
        String query = "SELECT * FROM Questions WHERE QuestionID = ?";

        for (Integer id : questionIDs) {
            try (PreparedStatement preparedStatement = jdbc.getPreparedstatement(query)) {
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    questions.add(new Question(
                            resultSet.getString("QuestionText"),
                            resultSet.getString("Answer1Text"),
                            resultSet.getString("Answer2Text"),
                            resultSet.getString("Answer3Text"),
                            resultSet.getString("Answer4Text"),
                            resultSet.getString("CorrectAnswerText"),
                            resultSet.getString("Category")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Log total fetched questions
        System.out.println("Total questions fetched from database: " + questions.size());

        return questions;
    }



    @FXML
    private void generatePDFs() {
        try {
            if(numofQuestion.getText().isBlank()){
                showAlert("Error","Quiz size not declared");
                return;
            }
            // Step 1: Generate quiz question IDs using the algorithm from QuizQuestionGenerator
            int quizSize = Integer.parseInt(numofQuestion.getText());
            List<Integer> questionIDs = QuizQuestionGenerator.generateQuestions(quizSize);

            // Log the generated question IDs
            System.out.println("Generated Question IDs: " + questionIDs);

            // Step 2: Fetch actual Question objects based on question IDs
            List<Question> questions = getQuestionsByIds(questionIDs);

            // Log the number of questions fetched
            System.out.println("Fetched Questions Count: " + questions.size());

            // Check if the number of fetched questions matches the quiz size
            if (questions.size() < quizSize) {
                showAlert("Error", "Not enough questions available. Please increase the quiz size or check your question database.");
                return;
            }

            // Step 3: Generate two PDFs
            generateQuestionsOnlyPDF(questions, "quiz_questions_only_" + System.currentTimeMillis() + ".pdf");
            generateQuestionsWithAnswersPDF(questions, "quiz_questions_with_answers_" + System.currentTimeMillis() + ".pdf");

            // Step 4: Show success message
            showAlert("PDF Generated", "Both PDFs have been generated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to generate PDFs.");
        }
    }
    // PDF with only questions
    // PDF with only questions
    private void generateQuestionsOnlyPDF(List<Question> questions, String filename) throws IOException {
        PdfWriter writer = new PdfWriter(filename);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, PageSize.A4);
        doc.setMargins(30, 35, 20, 35); // Set margins for the document (increased right margin)

        // Load the header file
        String headerFile = "header.pdf";
        PdfDocument headerDoc = new PdfDocument(new PdfReader(headerFile));
        headerDoc.copyPagesTo(1, headerDoc.getNumberOfPages(), pdfDoc);
        headerDoc.close(); // Close the header document

        // Adding space between header and questions
        doc.add(new Paragraph("\n\n\n\n\n\n\n\n")); // Adjust this value as needed for more spacing

        // Adding Questions without borders
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            doc.add(new Paragraph((i + 1) + ". " + question.getQuestion())
                    .setFontSize(12)
                    .setMarginBottom(5)
                    .setFixedLeading(15)
                    .setMarginRight(20)); // Add right margin to move text away from the border
            doc.add(new Paragraph("    A) " + question.getAns1())
                    .setFontSize(12)
                    .setMarginBottom(5)
                    .setFixedLeading(15)
                    .setMarginRight(20));
            doc.add(new Paragraph("    B) " + question.getAns2())
                    .setFontSize(12)
                    .setMarginBottom(5)
                    .setFixedLeading(15)
                    .setMarginRight(20));
            doc.add(new Paragraph("    C) " + question.getAns3())
                    .setFontSize(12)
                    .setMarginBottom(5)
                    .setFixedLeading(15)
                    .setMarginRight(20));
            doc.add(new Paragraph("    D) " + question.getAns4())
                    .setFontSize(12)
                    .setMarginBottom(15) // Add some space between questions
                    .setFixedLeading(15)
                    .setMarginRight(20));
        }

        // Add a page border to all pages
        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            PdfPage page = pdfDoc.getPage(i);
            PdfCanvas canvas = new PdfCanvas(page);
            canvas.rectangle(20, 20, pdfDoc.getDefaultPageSize().getWidth() - 40, pdfDoc.getDefaultPageSize().getHeight() - 40) // Draw a rectangle (border) around the page
                    .setStrokeColor(new DeviceRgb(0, 0, 0)) // Black color
                    .setLineWidth(1.5F)
                    .stroke();
        }

        doc.close();
    }
    // PDF with questions and answers
    private void generateQuestionsWithAnswersPDF(List<Question> questions, String filename) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(filename);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        // Add Header
        doc.add(new Paragraph("Arab Academy for Science, Technology and Maritime Transport")
                .setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));
        doc.add(new Paragraph("College of Computing and Information Technology")
                .setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));
        doc.add(new Paragraph("Quiz")
                .setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER));
        doc.add(new Paragraph("---------------------------------------------------")
                .setTextAlignment(TextAlignment.CENTER));

        // Add Details
        doc.add(new Paragraph("Lecturer       : Dr. Shady Zahran").setFontSize(12));
        doc.add(new Paragraph("Course Title   : ").setFontSize(12));
        doc.add(new Paragraph("Course Code    : ").setFontSize(12));
        doc.add(new Paragraph("Time           : ").setFontSize(12));
        doc.add(new Paragraph("Date           : ").setFontSize(12));
        doc.add(new Paragraph("\n")); // Adding space before questions

        // Adding Questions with Answers
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            doc.add(new Paragraph((i + 1) + ". " + question.getQuestion())
                    .setFontSize(12));
            doc.add(new Paragraph("    A) " + question.getAns1())
                    .setFontSize(12));
            doc.add(new Paragraph("    B) " + question.getAns2())
                    .setFontSize(12));
            doc.add(new Paragraph("    C) " + question.getAns3())
                    .setFontSize(12));
            doc.add(new Paragraph("    D) " + question.getAns4())
                    .setFontSize(12));
            // Add correct answer
            doc.add(new Paragraph("Answer: " + question.getCorrect())
                    .setFontSize(12));
            doc.add(new Paragraph("\n")); // Adding space after each question
        }
        doc.close();
    }


    @FXML
    void START(ActionEvent event) {
        String sql = "INSERT INTO `quiz_mangement`.`quizzes` (`QuizTitle`, `CourseID`, `TotalQuestions`, `quizTime`) VALUES (?, ?, ?, ?)";
        JDBC jdbc = new JDBC();
        PreparedStatement preparedStatement = jdbc.getPreparedstatement(sql);
        try {
            if (title.getText().isEmpty() || numofQuestion.getText().isEmpty() || (minutes.getValue() == null && hours.getValue() == null) || courseChoiceBox.getValue() == null) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }

            if (!numofQuestion.getText().matches("\\d+")) {
                showAlert("Error", "Number of questions must contain only digits.");
                return;
            }

            int numOfQuestions = Integer.parseInt(numofQuestion.getText());
            if (numOfQuestions <= 0) {
                showAlert("Error", "Number of questions must be greater than 0.");
                return;
            }

            int totalMinutes = (hours.getValue() != null ? hours.getValue() : 0) * 120 + (minutes.getValue() != null ? minutes.getValue() : 0)*2+seconds.getValue()/30;
            if (totalMinutes < numOfQuestions) {
                showAlert("Error", "Total minutes must be greater than or equal to the number of questions.");
                return;
            }

            String courseID = getCourseID(courseChoiceBox.getValue());
            String quizTime = getQuizTime();

            preparedStatement.setString(1, title.getText());
            preparedStatement.setString(2, courseID);
            preparedStatement.setInt(3, numOfQuestions);
            preparedStatement.setString(4, quizTime);
            preparedStatement.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Hoooraaay");
            alert.setHeaderText(null);
            alert.setContentText("Quiz Created Successfully");
            alert.showAndWait();
            title.clear();
            numofQuestion.clear();
            minutes.getValueFactory().setValue(0);
            hours.getValueFactory().setValue(0);
            seconds.getValueFactory().setValue(0);
            courseChoiceBox.setValue(null);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCourseID(String courseName) {
        switch (courseName) {
            case "Advanced Programming Applications":
                return "CSS2304";
            case "Object-Oriented Programming":
                return "CSS2303";
            default:
                return null;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void switchToProfHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Prof-home.fxml")));
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
    public void refreshquizTable() {
        JDBC jdbc = new JDBC("SELECT q.QuizID, q.QuizTitle, q.TotalQuestions, q.QuizTime, c.CourseName " +
                "FROM quiz_mangement.quizzes q " +
                "INNER JOIN quiz_mangement.courses c ON q.CourseID = c.CourseID");

        try {
            quizObservableList.clear();
            ResultSet resultSet = jdbc.executeQuery();

            while (resultSet.next()) {
                int quizID = resultSet.getInt("QuizID");
                String quizTitle = resultSet.getString("QuizTitle");
                int totalQuestions = resultSet.getInt("TotalQuestions");
                String quizTime = resultSet.getString("QuizTime");
                String courseName = resultSet.getString("CourseName");
                quizObservableList.add(new Quiz(quizID, quizTitle, totalQuestions, courseName, quizTime));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbc.closeConnection();
        }
    }
    public void deleteQuiz() {
        // Prepare the SQL statement to delete the quiz with the given ID
        String sql = "DELETE FROM quiz_mangement.quizzes WHERE QuizID = ?";
        JDBC jdbc = new JDBC();
        try {
            // Create a prepared statement
            PreparedStatement preparedStatement = jdbc.getPreparedstatement(sql);
            // Set the quiz ID parameter
            Quiz quiz = quizTableView.getSelectionModel().getSelectedItem();
            preparedStatement.setInt(1, quiz.getQuizId());
            // Execute the deletion query
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                // If deletion is successful, show a confirmation message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Quiz Deleted");
                alert.setHeaderText(null);
                alert.setContentText("The quiz has been deleted successfully.");
                alert.showAndWait();
            } else {
                // If no rows were affected, show an error message
                showAlert("Error", "Failed to delete quiz. Please try again.");
            }
        } catch (SQLException e) {
            // Handle SQL exception
            throw new RuntimeException(e);
        } finally {
            // Close JDBC connection
            jdbc.closeConnection();
        }
        // After deletion, refresh the quiz table
        refreshquizTable();
    }
    @FXML
    private void refreshStudentTable() {
        String quizTitle = Title.getText().trim().toLowerCase();
        if (quizTitle.isEmpty()) {
            Title.setText("");
            showAlert("Title is required!");
            return;
        }

        // Check if the quiz title exists in the database
        quizID = getQuizIDFromTitle(quizTitle);
        if (quizID == null) {
            Title.setText("");
            showAlert("Quiz title not found!");
            return;
        }
        Titletext.setText(quizTitle);
        Title.setText("");
        // Fetch students who participated in the quiz
        fetchStudentsForQuiz(quizID);
    }
    private String getQuizIDFromTitle(String quizTitle) {
        JDBC jdbc = new JDBC();
        try {
            PreparedStatement preparedStatement = jdbc.getPreparedstatement("SELECT QuizID FROM quizzes WHERE QuizTitle = ?");
            preparedStatement.setString(1, quizTitle);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("QuizID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (jdbc != null) {
                jdbc.closeConnection();
            }
        }
        return null;
    }

    private void fetchStudentsForQuiz(String quizID) {
        JDBC jdbc = new JDBC();
        try {
            studentObservableList.clear();
            PreparedStatement preparedStatement = jdbc.getPreparedstatement("SELECT s.StudentID, s.Name FROM students s INNER JOIN results r ON s.StudentID = r.StudentID WHERE r.QuizID = ?");
            preparedStatement.setString(1, quizID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                studentObservableList.add(new Student(resultSet.getInt("StudentID"), resultSet.getString("Name")));
            }
            tabelstu.setItems(studentObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (jdbc != null) {
                jdbc.closeConnection();
            }
        }
    }
    @FXML
    private void handleOneStudent(MouseEvent event) {
        Student selectedStudent = tabelstu.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            String studentName = selectedStudent.getName();
            String studentID = getStudentIDFromName(studentName);

            if (studentID != null) {
                updateStudentDetails(studentName, studentID);
            }
        }
    }
    private String getStudentIDFromName(String studentName) {
        JDBC jdbc = new JDBC();
        try {
            PreparedStatement preparedStatement = jdbc.getPreparedstatement("SELECT StudentID FROM students WHERE Name = ?");
            preparedStatement.setString(1, studentName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString("StudentID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (jdbc != null) {
                jdbc.closeConnection();
            }
        }
        return null;
    }

    private void updateStudentDetails(String studentName, String studentID) {
        JDBC jdbc = new JDBC();
        try {
            PreparedStatement preparedStatement = jdbc.getPreparedstatement("SELECT r.Score, q.TotalQuestions " +
                    "FROM results r INNER JOIN quizzes q ON r.QuizID = q.QuizID " +
                    "WHERE r.StudentID = ? AND r.QuizID = ?");
            preparedStatement.setString(1, studentID);
            preparedStatement.setString(2,quizID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int totalQuestions = resultSet.getInt("TotalQuestions");
                int score = resultSet.getInt("Score");
                int wrongAnswers = totalQuestions - score;
                double rank = ((double) score / totalQuestions) * 100;

                nameLabel.setText(studentName);
                numberOfQuestionsLabel.setText(String.valueOf(totalQuestions));
                correctAnswersLabel.setText(String.valueOf(score));
                wrongAnswersLabel.setText(String.valueOf(wrongAnswers));
                progressIndicator.setProgress(rank / 100);
            } else {
                nameLabel.setText("NOT YET");
                numberOfQuestionsLabel.setText("");
                correctAnswersLabel.setText("");
                wrongAnswersLabel.setText("");

                progressIndicator.setProgress(0.0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (jdbc != null) {
                jdbc.closeConnection();
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void showStatistics(ActionEvent event) {
        // Fetch statistics data from the database
        fetchStatisticsFromDatabase();
    }
    private void fetchStatisticsFromDatabase() {
        // Clear existing data
        statisticsChart.getData().clear();

        // Perform database query to get statistics data
        String sql = "SELECT q.QuestionID, COUNT(sa.QuestionID) AS AnswerCount " +
                "FROM StudentAnswers sa " +
                "JOIN Questions q ON sa.QuestionID = q.QuestionID " +
                "GROUP BY q.QuestionID";
        JDBC jdbc = new JDBC();
        try {
            PreparedStatement preparedStatement = jdbc.getPreparedstatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Create a new series for the chart
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.setName("Answers Count");

            // Populate the series with data from the result set
            while (resultSet.next()) {
                String questionText = resultSet.getString("QuestionId");
                int answerCount = resultSet.getInt("AnswerCount");
                series.getData().add(new XYChart.Data<>(questionText, answerCount));
            }

            // Add the series to the chart
            statisticsChart.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}

