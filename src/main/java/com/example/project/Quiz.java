package com.example.project;

import java.util.*;

public class Quiz {
    private Integer quizId;
    private String title;
    private String courseId;
    private Integer totalQuestions;
    private String courseName;
    private String quizTime;

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public Quiz(Integer quizId, String title, String courseId, Integer totalQuestions) {
        this.quizId = quizId;
        this.title = title;
        this.courseId = courseId;
        this.totalQuestions = totalQuestions;
    }
    public Quiz(Integer quizId, String title, Integer totalQuestions,String courseName,String quizTime) {
        this.quizId = quizId;
        this.title = title;
        this.courseName = courseName;
        this.totalQuestions = totalQuestions;
        this.quizTime = quizTime;
    }



    public String getTitle() {
        return title;
    }


    @Override
    public String toString() {
        return this.title;
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

    public String getCourseName() {
        return courseName;
    }

    public String getQuizTime() {
        return quizTime;
    }

    public void setQuizTime(String quizTime) {
        this.quizTime = quizTime;
    }

    public Integer getQuizId() {
        return quizId;
    }
}