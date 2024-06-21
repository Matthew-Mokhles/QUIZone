package com.example.project;

public class Results {
    private int studentId;
    private int quizId;
    private int score;

    // Constructors


    public Results(int studentId, int quizId, int score) {
        this.studentId = studentId;
        this.quizId = quizId;
        this.score = score;
    }

    // Getter and Setter methods
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
