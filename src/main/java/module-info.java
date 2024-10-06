module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires controlsfx;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires langchain4j.core;
    requires langchain4j.ollama;
    requires layout;
    requires kernel;


    opens com.example.project to javafx.fxml;
    exports com.example.project;

}