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
