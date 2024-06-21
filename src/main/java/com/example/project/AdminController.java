package com.example.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.controlsfx.control.Notifications;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    @FXML
    private Label header;
    @FXML
    private Label header1;
    @FXML
    private Label header2;
    @FXML
    private Label header3;
    @FXML
    private Label message =null;
    @FXML
    private Label message1=null;
    @FXML
    private Label message2=null;
    @FXML
    private Label message3=null;
    static String title;
    //---------------//
    @FXML
    private TextField name;
    @FXML
    private TextField id;
    @FXML
    private TextField pinCode;
    //---------------//
    @FXML
    private TextField id1;
    //---------------//
    @FXML
    private TextField name1;
    @FXML
    private TextField id2;
    @FXML
    private TextField pinCode1;
    //---------------//
    @FXML
    private TextField studentId;
    @FXML
    private TextField Courseid;
    //---------------//
    @FXML
    private TableView<Student> tableView;
    @FXML
    private TableColumn<Student,Integer> studentid;
    @FXML
    private TableColumn<Student,String> studentName;
    @FXML
    private TableColumn<Student,String> studentPincode;
    //---------------//
    @FXML
    private TableView<Student> tableView1;
    @FXML
    private TableColumn<Student,Integer> studentid1;
    @FXML
    private TableColumn<Student,String> studentName1;
    @FXML
    private TableColumn<Student,String> studentPincode1;
    //---------------//
    @FXML
    private TableView<Student> tableView2;
    @FXML
    private TableColumn<Student,Integer> studentid2;
    @FXML
    private TableColumn<Student,String> studentName2;
    @FXML
    private TableColumn<Student,String> studentPincode2;
    //---------------//
    @FXML
    private TableView<Registration> tableView3;
    @FXML
    private TableColumn<Registration,Integer> studentid3;
    @FXML
    private TableColumn<Registration,String> CourseCode;
    @FXML
    private TableColumn<Registration,String> CourseName;
    @FXML
    private TableColumn<Registration,String> studentName3;
    private double xOffset = 0;
    private double yOffset = 0;
    private Scene scene;
    private Parent root;
    private Stage stage;
    private final ObservableList<Student> studentObservableList = FXCollections.observableArrayList();
    private final ObservableList<Registration> registrationObservableList = FXCollections.observableArrayList();

    public static boolean isValidName(String name) {
        // Check if the name is null or empty
        if (name == null || name.isEmpty()) {
            return false;
        }
        if (!name.matches("[a-zA-Z ]+")) {
            return false;
        }

        return true;
    }
    public boolean isValidID(String id) {
        // Check if ID is empty or null
        if (id == null || id.isEmpty()) {
            return false;
        }
        if (id.length()<9) {
            return false;
        }
        try {
            // Parse ID to integer
            int parsedID = Integer.parseInt(id);

            // Check if ID is a positive integer
            if (parsedID <= 0) {
                return false;
            }

            // Additional validation criteria (if any)
            // For example, check if the ID falls within a specific range

            // Return true if all validation passes
            return true;
        } catch (NumberFormatException e) {
            // ID cannot be parsed as an integer
            return false;
        }
    }
    public String validateAndFormatCourseId(String courseId) {
        // Regular expression to match the pattern: three alphabets followed by four digits
        String regex = "^[A-Za-z]{3}\\d{4}$";

        // Trim the input courseId
        courseId = courseId.trim();

        // Check if the courseId matches the pattern
        if (courseId.matches(regex)) {
            // Convert the first three characters (letters) to uppercase
            String uppercaseLetters = courseId.substring(0, 3).toUpperCase();
            // Get the digits part of the courseId
            String digits = courseId.substring(3);
            // Concatenate the uppercase letters and digits
            return uppercaseLetters + digits;
        } else {
            // Return null if the courseId is not valid
            return null;
        }
    }

    public void saveStu(ActionEvent event) throws SQLException {
        if(this.id.getText().isBlank()||this.pinCode.getText().isBlank()||this.name.getText().isBlank()){
            message.setText("Please fill all data fields");
            message.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Students Form Correctly")
                    .text("Please fill all data fields")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
            clear();
            return;
        }
        String id = this.id.getText().trim();
        if(!isValidID(id)){
            message.setText("Enter Valid id...");
            message.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Students Form Correctly")
                    .text("Enter Valid id...")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
            return;
        }
        String name = this.name.getText().trim();
        if (!isValidName(name)){
            message.setText("Enter Valid Name...");
            message.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Students Form Correctly")
                    .text("Enter Valid Name...")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
            return;
        }
        String pinCode = this.pinCode.getText().trim();
        if(pinCode.length()<6){
            message.setText("Enter Valid Pin Code...");
            message.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Students Form Correctly")
                    .text("Enter Valid Pin Code...")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
            return;
        }
        System.out.println("Saving......");
        String sql = "INSERT INTO `quiz_mangement`.`students` (`StudentID`, `Name`, `PIN_Code`) VALUES (?, ?, ?)";
        JDBC jdbc = new JDBC();
        PreparedStatement preparedStatement = jdbc.getPreparedstatement(sql);
        try {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, pinCode);
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
            message.setTextFill(Color.GREEN);
            message.setText("Saved Successfully");
            refreshStudentTable();
            Notifications.create()
                    .title("Success")
                    .text("Student Registered Successfully!")
                    .darkStyle()
                    .position(Pos.TOP_RIGHT)
                    .showConfirm();
            clear();
            System.out.println("Saved Successfully");
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // MySQL error code for duplicate entry
                System.out.println("Error: Student ID already exists");
                message.setTextFill(Color.RED);
                message.setText("Error: Student ID already exists");
                Notifications.create()
                        .title("Failed")
                        .text("Error: Student ID already exists")
                        .darkStyle()
                        .position(Pos.BOTTOM_RIGHT)
                        .showWarning();
                clear();
            }
            else
                throw e;
        }finally {
            jdbc.closeConnection();
        }
    }

    public static void setTitle(String title) {
        AdminController.title = title;
    }
    public void clear(){
        name.clear();
        id.clear();
        pinCode.clear();
    }
    public void clear1(){
        id1.clear();
    }
    public void clear2(){
        name1.clear();
        id2.clear();
        pinCode1.clear();
    }
    public void clear3(){
        studentId.clear();
        Courseid.clear();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        header.setText("Hello, "+title);
        header1.setText("Hello, "+title);
        header2.setText("Hello, "+title);
        header3.setText("Hello, "+title);
        studentid.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentPincode.setCellValueFactory(new PropertyValueFactory<>("pincode"));
        tableView.setItems(studentObservableList);
        //----------//
        studentid1.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentName1.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentPincode1.setCellValueFactory(new PropertyValueFactory<>("pincode"));
        tableView1.setItems(studentObservableList);
        //----------//
        studentid2.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentName2.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentPincode2.setCellValueFactory(new PropertyValueFactory<>("pincode"));
        tableView2.setItems(studentObservableList);
        //----------//
        studentid3.setCellValueFactory(new PropertyValueFactory<>("studentid"));
        CourseCode.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        CourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        studentName3.setCellValueFactory(new PropertyValueFactory<>("StudentName"));
        tableView3.setItems(registrationObservableList);
        //----------//
        refreshRegistrationTable();
        refreshStudentTable();
        tableView2.setOnMouseClicked(e ->{
            Student student = tableView2.getSelectionModel().getSelectedItem();
            if(student!=null){
                id2.setText(String.valueOf(student.getId()));
                name1.setText(student.getName());
                pinCode1.setText(student.getPincode());
            }

        });
        tableView1.setOnMouseClicked(e ->{
            Student student = tableView1.getSelectionModel().getSelectedItem();
            if(student!=null){
                id1.setText(String.valueOf(student.getId()));
            }

        });
        tableView3.setOnMouseClicked(e ->{
            Registration registration = tableView3.getSelectionModel().getSelectedItem();
            if(registration!=null){
                Courseid.setText(registration.getCourseCode());
            }

        });
    }
    void refreshStudentTable(){
        JDBC jdbc = new JDBC("SELECT * FROM quiz_mangement.students");
        try {
        studentObservableList.clear();
        ResultSet resultSet = jdbc.executeQuery();
        while (resultSet.next()){
        studentObservableList.add(new Student(Integer.parseInt(resultSet.getString("StudentID")),resultSet.getString("Name"),resultSet.getString("PIN_Code")));
        }
    }catch (SQLException e){
        e.printStackTrace();
    }finally {
            jdbc.closeConnection();
        }

    }
    public void refreshRegistrationTable() {
        JDBC jdbc = new JDBC("SELECT r.StudentID, s.Name, r.CourseID, c.CourseName " +
                "FROM quiz_mangement.registration r " +
                "INNER JOIN quiz_mangement.courses c ON r.CourseID = c.CourseID " +
                "INNER JOIN quiz_mangement.students s ON r.StudentID = s.StudentID");

        try {
            registrationObservableList.clear();
            ResultSet resultSet = jdbc.executeQuery();

            while (resultSet.next()) {
                int studentId = resultSet.getInt("StudentID");
                String studentName = resultSet.getString("Name");
                String courseId = resultSet.getString("CourseID");
                String courseName = resultSet.getString("CourseName");
                registrationObservableList.add(new Registration(studentId, courseId, courseName, studentName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbc.closeConnection();
        }
    }

    public void deleteStu(ActionEvent event) throws SQLException{
        if(this.id1.getText().isBlank()){
            message1.setText("Please fill all data fields");
            message1.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Students Form Correctly")
                    .text("Please fill the id field")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
            clear();
            return;
        }
        String id = this.id1.getText().trim();
        if(!isValidID(id)){
            message1.setText("Enter Valid id...");
            message1.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Students Form Correctly")
                    .text("Enter Valid id...")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
            return;
        }
        System.out.println("Deleting......");
        String sql = "DELETE FROM `quiz_mangement`.`students` WHERE (`StudentID` = ?)";
        JDBC jdbc = new JDBC();
        PreparedStatement preparedStatement = jdbc.getPreparedstatement(sql);
        preparedStatement.setString(1,id);
        System.out.println(preparedStatement);
        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected == 0) {
            // No rows were deleted because the specified ID was not found
            message1.setText("Student with ID " + id + " not found.");
            message1.setTextFill(Color.RED);
            Notifications.create()
                    .title("Delete Student")
                    .text("Student with ID " + id + " not found.")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
        } else {
            // Rows were deleted successfully
            message1.setTextFill(Color.GREEN);
            message1.setText("Deleted Successfully");
            refreshStudentTable();
            Notifications.create()
                    .title("Success")
                    .text("Student Deleted Successfully!")
                    .darkStyle()
                    .position(Pos.TOP_RIGHT)
                    .showConfirm();
        }

        clear();

    }
    public void updateStu(ActionEvent event) throws SQLException {
        if(this.pinCode1.getText().isBlank()||this.name1.getText().isBlank()){
            message2.setText("Please fill all data fields");
            message2.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Students Form Correctly")
                    .text("Please fill all data fields")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
            clear();
            return;
        }
        String name = this.name1.getText().trim();
        if (!isValidName(name)){
            message2.setText("Enter Valid Name...");
            message2.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Students Form Correctly")
                    .text("Enter Valid Name...")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
            return;
        }
        String pinCode = this.pinCode1.getText().trim();
        if(pinCode.length()<6){
            message2.setText("Enter Valid Pin Code...");
            message2.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Students Form Correctly")
                    .text("Enter Valid Pin Code...")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
            return;
        }
        String id = this.id2.getText().trim();
        System.out.println("Updating......");
        String sql = "UPDATE `quiz_mangement`.`students` SET `Name` = ?, `PIN_Code` = ? WHERE (`StudentID` = ?);";
        JDBC jdbc = new JDBC();
        PreparedStatement preparedStatement = jdbc.getPreparedstatement(sql);
        try {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, pinCode);
            preparedStatement.setString(3, id);
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
            message2.setTextFill(Color.GREEN);
            message2.setText("Updated Successfully");
            refreshStudentTable();
            Notifications.create()
                    .title("Success")
                    .text("Student Data Updated Successfully!")
                    .darkStyle()
                    .position(Pos.TOP_RIGHT)
                    .showConfirm();
            clear();
            System.out.println("Updated Successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            jdbc.closeConnection();
        }
    }
    public void importFromExcel(ActionEvent event) throws SQLException {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        System.out.println(file.getAbsolutePath());
        if (file!=null){
        Excel excel = new Excel(file.getAbsolutePath());
        ArrayList<Student> studentArrayList = excel.getStudentsList();
        for(int i =0;i<studentArrayList.size();i++){
            saveStu(String.valueOf(studentArrayList.get(i).getId()),studentArrayList.get(i).getName(),studentArrayList.get(i).getPincode());
        }
        }else {
            System.out.println("Error Handling file");
            message.setTextFill(Color.RED);
            message.setText("Error");
        }

    }
    public void saveStu(String currentid,String currentName,String currentPincode) throws SQLException {
        if(currentid.isBlank()||currentName.isBlank()||currentPincode.isBlank()){
            message.setText("Please fill all data fields");
            message.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Students Form Correctly")
                    .text("Please fill all data fields")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
            clear();
            return;
        }
        String id = currentid.trim();
        if(!isValidID(id)){
            message.setText("Enter Valid id...");
            message.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Students Form Correctly")
                    .text("Enter Valid id...")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
            return;
        }
        String name = currentName.trim();
        if (!isValidName(name)){
            message.setText("Enter Valid Name...");
            message.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Students Form Correctly")
                    .text("Enter Valid Name...")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
            return;
        }
        String pinCode = currentPincode.trim();
        if(pinCode.length()<6){
            message.setText("Enter Valid Pin Code...");
            message.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Students Form Correctly")
                    .text("Enter Valid Pin Code...")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
            return;
        }
        System.out.println("Saving......");
        String sql = "INSERT INTO `quiz_mangement`.`students` (`StudentID`, `Name`, `PIN_Code`) VALUES (?, ?, ?)";
        JDBC jdbc = new JDBC();
        PreparedStatement preparedStatement = jdbc.getPreparedstatement(sql);
        try {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, pinCode);
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
            message.setTextFill(Color.GREEN);
            message.setText("Saved Successfully");
            refreshStudentTable();
            Notifications.create()
                    .title("Success")
                    .text("Student Registered Successfully!")
                    .darkStyle()
                    .position(Pos.TOP_RIGHT)
                    .showConfirm();
            clear();
            System.out.println("Saved Successfully");
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // MySQL error code for duplicate entry
                System.out.println("Error: Student ID already exists");
                message.setTextFill(Color.RED);
                message.setText("Error: Student ID already exists");
                Notifications.create()
                        .title("Failed")
                        .text("Error: Student ID already exists")
                        .darkStyle()
                        .position(Pos.BOTTOM_RIGHT)
                        .showWarning();
                clear();
            }
            else
                throw e;
        }finally {
            jdbc.closeConnection();
        }
    }
    public void RegisterStudent(ActionEvent event) throws SQLException {
        if (this.studentId.getText().isBlank() || this.Courseid.getText().isBlank()) {
            message3.setText("Please fill all data fields");
            message3.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Students Form Correctly")
                    .text("Please fill all data fields")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
            clear2();
            return;
        }
        String studentId = this.studentId.getText().trim();
        String courseId = this.Courseid.getText().trim();
        if (!isValidID(studentId)) {
            message3.setText("Enter Valid Student ID...");
            message3.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Students Form Correctly")
                    .text("Enter Valid Student ID...")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
            return;
        }

        String formattedCourseId = validateAndFormatCourseId(courseId);
        if (formattedCourseId == null) {
            message3.setText("Enter Valid Course ID...");
            message3.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Students Form Correctly")
                    .text("Enter Valid Course ID...")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
            return;
        }
        System.out.println(formattedCourseId);
        if(isStudentRegistered(Integer.parseInt(studentId),formattedCourseId)){
            System.out.println("Error: Student is already registered in this course.");
            message3.setTextFill(Color.RED);
            message3.setText("Error: Student is already registered in this course");
            Notifications.create()
                    .title("Failed")
                    .text("Error: Student is already registered in this course")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showWarning();
            clear2();
            return;
        }
        System.out.println("Saving......");
        String sql = "INSERT INTO `quiz_mangement`.`registration` (`StudentID`, `CourseID`) VALUES (?,?);";
        JDBC jdbc = new JDBC();
        PreparedStatement preparedStatement = jdbc.getPreparedstatement(sql);
        try {
            preparedStatement.setString(1, studentId);
            preparedStatement.setString(2, formattedCourseId);
            preparedStatement.executeUpdate();
            message3.setTextFill(Color.GREEN);
            message3.setText("Registered Successfully");
            refreshRegistrationTable();
            Notifications.create()
                    .title("Success")
                    .text("Student Registered Successfully!")
                    .darkStyle()
                    .position(Pos.TOP_RIGHT)
                    .showConfirm();

            clear();
            System.out.println("Saved Successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbc.closeConnection();
        }
    }
    public boolean isStudentRegistered(int studentId, String courseId) {
        String sql = "SELECT COUNT(*) AS count FROM quiz_mangement.registration WHERE StudentID = ? AND CourseID = ?";
        JDBC jdbc = new JDBC();
        PreparedStatement preparedStatement = jdbc.getPreparedstatement(sql);

        try {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setString(2, courseId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0; // If count > 0, student is registered; otherwise, not registered
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbc.closeConnection();
        }

        // Return false if an error occurred during database operation
        return false;
    }
    public void dropStudent(ActionEvent event) throws SQLException {
        if (this.studentId.getText().isBlank() || this.Courseid.getText().isBlank()) {
            message3.setText("Please fill all data fields");
            message3.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Students Form Correctly")
                    .text("Please fill all data fields")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
            clear2();
            return;
        }

        String studentId = this.studentId.getText().trim();
        String courseId = this.Courseid.getText().trim();

        if (!isValidID(studentId)) {
            message3.setText("Enter Valid Student ID...");
            message3.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Students Form Correctly")
                    .text("Enter Valid Student ID...")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
            return;
        }

        String formattedCourseId = validateAndFormatCourseId(courseId);
        if (formattedCourseId == null) {
            message3.setText("Enter Valid Course ID...");
            message3.setTextFill(Color.RED);
            Notifications.create()
                    .title("Fill Students Form Correctly")
                    .text("Enter Valid Course ID...")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showError();
            return;
        }
        System.out.println(formattedCourseId);
        if(!isStudentRegistered(Integer.parseInt(studentId),formattedCourseId)){
            System.out.println("Error: Student not Found.");
            message3.setTextFill(Color.RED);
            message3.setText("Error: Student not Found");
            Notifications.create()
                    .title("Failed")
                    .text("Error: Student not Found")
                    .darkStyle()
                    .position(Pos.BOTTOM_RIGHT)
                    .showWarning();
            clear2();
            return;
        }
        System.out.println("Dropping......");
        String sql = "DELETE FROM `quiz_mangement`.`registration` WHERE `StudentID` = ? AND `CourseID` = ?";
        JDBC jdbc = new JDBC();
        PreparedStatement preparedStatement = jdbc.getPreparedstatement(sql);

        try {
            preparedStatement.setString(1, studentId);
            preparedStatement.setString(2, formattedCourseId);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                // No rows were deleted because the student is not registered for the course
                System.out.println("Error: Student is not registered in this course.");
                message3.setTextFill(Color.RED);
                message3.setText("Error: Student is not registered in this course");
                Notifications.create()
                        .title("Failed")
                        .text("Error: Student is not registered in this course")
                        .darkStyle()
                        .position(Pos.BOTTOM_RIGHT)
                        .showWarning();
            } else {
                // Rows were deleted successfully
                message3.setTextFill(Color.GREEN);
                message3.setText("Student dropped from the course Successfully");
                refreshRegistrationTable();
                Notifications.create()
                        .title("Success")
                        .text("Student dropped from the course Successfully!")
                        .darkStyle()
                        .position(Pos.TOP_RIGHT)
                        .showConfirm();
            }
            clear();
            System.out.println("Dropped Successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbc.closeConnection();
        }
    }





}

