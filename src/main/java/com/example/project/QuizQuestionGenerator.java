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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizQuestionGenerator {
    // Method to insert questions into the QuizQuestions table
    public static void insertQuestions(int quizID, int quizSize) throws SQLException {
        // Calculate the number of questions for each category
        int easyQuestions = (int) (quizSize * 0.5);
        int mediumQuestions = (int) (quizSize * 0.3);
        int hardQuestions = quizSize - easyQuestions - mediumQuestions;

        // Retrieve and shuffle questions from the Questions table
        List<Integer> easyQuestionIDs = retrieveShuffledQuestionIDs( "Easy", easyQuestions);
        List<Integer> mediumQuestionIDs = retrieveShuffledQuestionIDs( "Medium", mediumQuestions);
        List<Integer> hardQuestionIDs = retrieveShuffledQuestionIDs( "Hard", hardQuestions);

        // Insert questions into the QuizQuestions table
        insertQuestionIDs(quizID, easyQuestionIDs);
        insertQuestionIDs(quizID, mediumQuestionIDs);
        insertQuestionIDs(quizID, hardQuestionIDs);
    }

    // Method to retrieve shuffled question IDs based on category
    private static List<Integer> retrieveShuffledQuestionIDs( String category, int count) throws SQLException {
        List<Integer> questionIDs = new ArrayList<>();
        String query = "SELECT QuestionID FROM Questions WHERE Category = ? ORDER BY RAND() LIMIT ?";
        JDBC jdbc = new JDBC();
        try (PreparedStatement statement = jdbc.getPreparedstatement(query)) {
            statement.setString(1, category);
            statement.setInt(2, count);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                questionIDs.add(resultSet.getInt("QuestionID"));
            }
        }
        Collections.shuffle(questionIDs);
        return questionIDs;
    }

    // Method to insert question IDs into the QuizQuestions table
    private static void insertQuestionIDs( int quizID, List<Integer> questionIDs) throws SQLException {
        String query = "INSERT INTO QuizQuestions (QuizID, QuestionID) VALUES (?, ?)";
        JDBC jdbc = new JDBC();
        try (PreparedStatement statement = jdbc.getPreparedstatement(query)) {
            for (int questionID : questionIDs) {
                statement.setInt(1, quizID);
                statement.setInt(2, questionID);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }
    public static List<Integer> generateQuestions(int quizSize) throws SQLException {
        int easyQuestions = (int) (quizSize * 0.5);
        int mediumQuestions = (int) (quizSize * 0.3);
        int hardQuestions = quizSize - easyQuestions - mediumQuestions;

        // Fetch IDs from database (replace with real database query)
        List<Integer> easyQuestionIDs = retrieveShuffledQuestionIDs("Easy", easyQuestions);
        List<Integer> mediumQuestionIDs = retrieveShuffledQuestionIDs("Medium", mediumQuestions);
        List<Integer> hardQuestionIDs = retrieveShuffledQuestionIDs("Hard", hardQuestions);

        List<Integer> allQuestions = new ArrayList<>();
        allQuestions.addAll(easyQuestionIDs);
        allQuestions.addAll(mediumQuestionIDs);
        allQuestions.addAll(hardQuestionIDs);
        Collections.shuffle(allQuestions);

        return allQuestions;
    }
}
