package com.example.project;

import java.sql.*;

public class JDBC {
    private String query;
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;
    // Constructors
    public JDBC() {}

    public JDBC(String query) {
        this.query = query;
    }

    // Setters and Getters
    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }



    // Methods
    public ResultSet executeQuery() {
        try {
            // Establish connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz_mangement", "root", "1234");

            // Create statement
            statement = connection.createStatement();

            // Execute query
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }
    public PreparedStatement getPreparedstatement(String sql) {
        try {
            // Establish connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz_mangement", "root", "1234");

            // Create statement
            preparedStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return preparedStatement;
    }
    public void closeConnection() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
