package com.example.project;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class QuestionController implements Initializable {
    // FXML variables
    @FXML
    private TextField QS1;
    @FXML
    private TextField ans11;
    @FXML
    private TextField ans21;
    @FXML
    private TextField ans31;
    @FXML
    private TextField ans41;
    @FXML
    private Label category;
    @FXML
    private Label correctanswer;
    @FXML
    private TableColumn<Question, String> questions1;
    @FXML
    private TableColumn<Question, String> categoryColumn;
    @FXML
    private TableView<Question> tableofquestion1;
    @FXML
    private Label message;
    @FXML
    private Label header1;


    @FXML
    private TextField QS;
    @FXML
    private TextField ans1;
    @FXML
    private TextField ans2;
    @FXML
    private TextField ans3;
    @FXML
    private TextField ans4;
    @FXML
    private ChoiceBox<String> compo;
    @FXML
    private ChoiceBox<String> correctAns;
    @FXML
    private TableColumn<Question, String> questions;
    @FXML
    private TableColumn<Question, String> categoryColumn1;
    @FXML
    private TableView<Question> tableofquestion;
    @FXML
    private Label header;

    @FXML
    private TextField QS2;
    @FXML
    private TextField ans12;
    @FXML
    private TextField ans22;
    @FXML
    private TextField ans32;
    @FXML
    private TextField ans42;
    @FXML
    private Label category1;
    @FXML
    private Label correctanswer1;
    @FXML
    private TableColumn<Question, String> questions2;
    @FXML
    private TableColumn<Question, String> categoryColumn2;
    @FXML
    private TableView<Question> tableofquestion2;
    @FXML
    private Label message2;
    @FXML
    private Label header2;

    @FXML
    private TextField QS3;
    @FXML
    private TextField ans13;
    @FXML
    private TextField ans23;
    @FXML
    private TextField ans33;
    @FXML
    private TextField ans43;
    @FXML
    private ChoiceBox<String> compo1;
    @FXML
    private ChoiceBox<String> correctAns1;
    @FXML
    private TableColumn<Question, String> questions3;
    @FXML
    private TableColumn<Question, String> categoryColumn3;
    @FXML
    private TableView<Question> tableofquestion3;
    @FXML
    private Label message3;
    @FXML
    private Label header3;
    @FXML
    private AnchorPane loadingPane;
    private static String title;
    // Observable list for storing questions
    private ObservableList<Question> questionObservableList = FXCollections.observableArrayList();

    // Prepared statement for database queries
    private PreparedStatement pst;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private TextField searchtxt;
    private Map<String, Integer> categoryMap = new HashMap<>();

    // Initialize method
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadingPane.setVisible(false);
        initializeComboBox();
        table();
        initializeCorrect();
        questions.setCellValueFactory(new PropertyValueFactory<>("Question"));
        questions1.setCellValueFactory(new PropertyValueFactory<>("Question"));
        questions2.setCellValueFactory(new PropertyValueFactory<>("Question"));
        questions3.setCellValueFactory(new PropertyValueFactory<>("Question"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("cat"));
        categoryColumn1.setCellValueFactory(new PropertyValueFactory<>("cat"));
        categoryColumn2.setCellValueFactory(new PropertyValueFactory<>("cat"));
        categoryColumn3.setCellValueFactory(new PropertyValueFactory<>("cat"));

        header.setText(title);
        header1.setText(title);
        header2.setText(title);
        header3.setText(title);
        tableofquestion2.setOnMouseClicked(e -> {
            Question question = tableofquestion2.getSelectionModel().getSelectedItem();
            if (question != null) {
                QS2.setText(String.valueOf(question.getQuestion()));
                ans12.setText(question.getAns1());
                ans22.setText(question.getAns2());
                ans32.setText(question.getAns3());
                ans42.setText(question.getAns4());
                category1.setText(question.getCat());
                correctanswer1.setText(question.getCorrect());
            }
        });
        tableofquestion3.setOnMouseClicked(e -> {
            Question question = tableofquestion3.getSelectionModel().getSelectedItem();
            if (question != null) {
                QS3.setText(String.valueOf(question.getQuestion()));
                ans13.setText(question.getAns1());
                ans23.setText(question.getAns2());
                ans33.setText(question.getAns3());
                ans43.setText(question.getAns4());
                compo1.setValue(question.getCat());
                correctAns1.setValue(question.getCorrect());
            }
        });
        tableofquestion1.setOnMouseClicked(e -> {
            Question question = tableofquestion1.getSelectionModel().getSelectedItem();
            if (question != null) {
                QS1.setText(String.valueOf(question.getQuestion()));
                ans11.setText(question.getAns1());
                ans21.setText(question.getAns2());
                ans31.setText(question.getAns3());
                ans41.setText(question.getAns4());
                category.setText(question.getCat());
                correctanswer.setText(question.getCorrect());
            }
        });
        searchtxt();
    }

    // Initialize choice box for Question category
    private void initializeComboBox() {
        ObservableList<String> categories = FXCollections.observableArrayList(
                "Easy",
                "Medium",
                "Hard"
        );
        compo.setItems(categories);
        compo1.setItems(categories);
    }

    // Initialize choice box for correct answer selection
    private void initializeCorrect() {
        ObservableList<String> correctChoices = FXCollections.observableArrayList(
                "First Choice",
                "Second Choice",
                "Third Choice",
                "Fourth Choice"
        );
        correctAns.setItems(correctChoices);
        correctAns1.setItems(correctChoices);
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

    // Populate the second table with questions
    private void table() {
        // Define category mappings
        Map<String, Integer> categoryMap = new HashMap<>();
        categoryMap.put("Easy", 1);
        categoryMap.put("Medium", 2);
        categoryMap.put("Hard", 3);

        JDBC jdbc = new JDBC("SELECT QuestionText, Answer1Text, Answer2Text, Answer3Text, Answer4Text, CorrectAnswerText, Category FROM questions");
        try {
            questionObservableList.clear();
            ResultSet resultSet = jdbc.executeQuery();
            while (resultSet.next()) {
                Question st = new Question(resultSet.getString("QuestionText"), resultSet.getString("Answer1Text"),
                        resultSet.getString("Answer2Text"), resultSet.getString("Answer3Text"), resultSet.getString("Answer4Text"),
                        resultSet.getString("CorrectAnswerText"), resultSet.getString("Category"));
                questionObservableList.add(st);
            }

            // Define a comparator based on category mappings
            Comparator<Question> categoryComparator = Comparator.comparingInt(q -> categoryMap.getOrDefault(q.getCat(), Integer.MAX_VALUE));

            // Set the TableView comparator to null so that the sortedList comparator is used
            SortedList<Question> sortedList = new SortedList<>(questionObservableList, categoryComparator);
            tableofquestion.setItems(sortedList);
            tableofquestion1.setItems(sortedList);
            tableofquestion2.setItems(sortedList);
            tableofquestion3.setItems(sortedList);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            jdbc.closeConnection();
        }
    }



    // Clear text fields for adding a new Question

    public void clear(ActionEvent event) {
        QS.clear();
        ans1.clear();
        ans2.clear();
        ans3.clear();
        ans4.clear();
        compo.getSelectionModel().clearSelection();
        correctAns.getSelectionModel().clearSelection();
    }

    // Clear text fields for viewing a Question

    public void clear1(ActionEvent event) {
        QS1.clear();
        ans11.clear();
        ans21.clear();
        ans31.clear();
        ans41.clear();
        category.setText(null);
        correctanswer.setText(null);
    }

    public void clear2(ActionEvent event) {
        QS.clear();
        ans1.clear();
        ans2.clear();
        ans3.clear();
        ans4.clear();
        compo.getSelectionModel().clearSelection();
        correctAns.getSelectionModel().clearSelection();
    }

    // Add a new Question to the database
    @FXML
    void addQuestion(ActionEvent event) {
        if (QS.getText().isBlank() || ans1.getText().isBlank() || ans2.getText().isBlank() || ans3.getText().isBlank() || ans4.getText().isBlank() || correctAns.getValue() == null || compo.getValue() == null) {
            message.setText("Please fill all data fields");
            message.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Question Form Correctly")
                    .text("Please fill all data fields")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
            return;
        }
        String Qquestion = QS.getText().trim();
        String Qans1 = ans1.getText().trim();
        String Qans2 = ans2.getText().trim();
        String Qans3 = ans3.getText().trim();
        String Qans4 = ans4.getText().trim();
        String Qcorrect = correctAns.getValue().trim();
        String Qcat = compo.getValue().trim();
        JDBC jdbc = new JDBC();
        try {
            pst = jdbc.getPreparedstatement("insert into questions (QuestionText, Answer1Text, Answer2Text, Answer3Text, Answer4Text, CorrectAnswerText, Category) values (?, ?, ?, ?, ?, ?, ?)");
            pst.setString(1, Qquestion);
            pst.setString(2, Qans1);
            pst.setString(3, Qans2);
            pst.setString(4, Qans3);
            pst.setString(5, Qans4);
            pst.setString(6, Qcorrect);
            pst.setString(7, Qcat);
            pst.executeUpdate();
            table();
            Notifications.create()
                    .title("Success")
                    .text("Question Added Successfully!")
                    .darkStyle()
                    .position(Pos.TOP_RIGHT)
                    .showConfirm();
            clear(new ActionEvent());
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            jdbc.closeConnection();
        }
    }

    // Delete a Question from the database
    @FXML
    void deleteQuestion(ActionEvent event) {
        int selectedIndex = tableofquestion2.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            String question = tableofquestion2.getItems().get(selectedIndex).getQuestion().trim();
            JDBC jdbc = new JDBC();
            try {
                pst = jdbc.getPreparedstatement("DELETE FROM questions WHERE QuestionText = ?");
                pst.setString(1, question);
                pst.executeUpdate();
                message2.setTextFill(Color.GREEN);
                message2.setText("Deleted Successfully");
                Notifications.create()
                        .title("Success")
                        .text("Question Deleted Successfully!")
                        .darkStyle()
                        .position(Pos.TOP_RIGHT)
                        .showConfirm();
                table();
                clear1(new ActionEvent());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            message2.setText("Nothing Selected");
            message2.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Question Form Correctly")
                    .text("Please Select a Field")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
        }
    }

    // Update a Question in the database
    @FXML
    void updateQuestion(ActionEvent event) {
        if (QS3.getText().isBlank() || ans13.getText().isBlank() || ans23.getText().isBlank() || ans33.getText().isBlank() || ans43.getText().isBlank() || correctAns1.getValue() == null || compo1.getValue() == null) {
            message.setText("Please fill all data fields");
            message.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Question Form Correctly")
                    .text("Please fill all data fields")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
            return;
        }
        JDBC jdbc = new JDBC();
        try {
            pst = jdbc.getPreparedstatement("UPDATE questions SET QuestionText = ?, Answer1Text = ?, Answer2Text = ?, " +
                    "Answer3Text = ?, Answer4Text = ?, CorrectAnswerText = ?, Category = ? WHERE QuestionText = ?");
            pst.setString(1, QS3.getText().trim());
            pst.setString(2, ans13.getText().trim());
            pst.setString(3, ans23.getText().trim());
            pst.setString(4, ans33.getText().trim());
            pst.setString(5, ans43.getText().trim());
            pst.setString(6, correctAns1.getValue().trim());
            pst.setString(7, compo1.getValue().trim());
            pst.setString(8, QS3.getText().trim());
            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                message3.setTextFill(Color.GREEN);
                message3.setText("Updated Successfully");
                Notifications.create()
                        .title("Success")
                        .text("Question Data Updated Successfully!")
                        .darkStyle()
                        .position(Pos.TOP_RIGHT)
                        .showConfirm();
                table(); // Refresh the table
                clear2(new ActionEvent());
            } else {
                message3.setText("Question Not found");
                message3.setTextFill(Color.RED);
                Notifications.create()
                        .title("Fill Questions Form Correctly")
                        .text("Enter Valid Question")
                        .darkStyle()
                        .position(Pos.BOTTOM_RIGHT)
                        .showError();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void setTitle(String title) {
        QuestionController.title = title;
    }

    public void generateQuestion() {
        loadingPane.setVisible(true);
        Thread thread = new Thread(()->{
        try {
        ChatBot chatBot = new ChatBot("Act as professor,I want you to generate in one line an MCQ question related to Java, OOP with Java, and JavaFX from Introduction to Java programming Book by Y. Daniel Liang with the following format: Question: [Generate the question here] First Choice: [First option] Second Choice: [Second option] Third Choice: [Third option] Fourth Choice: [Fourth option] Correct answer: [Correct choice, e.g., Third Choice] Category: [Easy/Medium/Hard] without * and without any opening sentence like here is your question or what ever so i need the format directly, I need your response in one line");
        String answer = chatBot.getAnswer();
        System.out.println(answer);
        Platform.runLater(() -> processQuestion(answer));
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            Platform.runLater(() -> loadingPane.setVisible(false));
        }
    });
        thread.start();
    }
    private void processQuestion(String answer) {
        // Split the chatbot answer into parts
        String[] parts = answer.split("\\s*(Question:|First Choice:|Second Choice:|Third Choice:|Fourth Choice:|Correct answer:|Category:)\\s*");

        // Initialize a map to hold the extracted information
        Map<String, String> infoMap = new HashMap<>();

        // Fields to extract
        String[] fields = {"Question", "First Choice", "Second Choice", "Third Choice", "Fourth Choice", "Correct answer", "Category"};

        // Iterate through the parts array
        for (int i = 1; i < parts.length; i++) {
            // Trim the part to remove leading and trailing whitespaces
            String part = parts[i].trim();
            // Add the part to the map with the corresponding field
            infoMap.put(fields[i-1], part);
        }

        // Print the extracted information
        infoMap.forEach((key, value) -> System.out.println(key + ": " + value));
        QS.setText(infoMap.get("Question"));
        ans1.setText(infoMap.get("First Choice"));
        ans2.setText(infoMap.get("Second Choice"));
        ans3.setText(infoMap.get("Third Choice"));
        ans4.setText(infoMap.get("Fourth Choice"));
        correctAns.setValue(infoMap.get("Correct answer"));
        compo.setValue(infoMap.get("Category"));


    }
    private void searchtxt() {
        FilteredList<Question> filteredData = new FilteredList<>(questionObservableList, p -> true);
        Map<String, Integer> categoryMap = new HashMap<>();
        categoryMap.put("Easy", 1);
        categoryMap.put("Medium", 2);
        categoryMap.put("Hard", 3);
        searchtxt.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(question -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Check if any of the columns contain the search text
                if (question.getQuestion() != null && question.getQuestion().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (question.getCat() != null && question.getCat().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false; // Does not match any filter
            });
        });

        // Define a comparator based on category mappings
        Comparator<Question> categoryComparator = Comparator.comparingInt(q -> categoryMap.getOrDefault(q.getCat(), Integer.MAX_VALUE));
        // Create a sorted list with the filtered data and apply the sorting comparator
        SortedList<Question> sortedList = new SortedList<>(filteredData, categoryComparator);
        // Bind the sorted and filtered data to the table
        tableofquestion1.setItems(sortedList);
    }



}
