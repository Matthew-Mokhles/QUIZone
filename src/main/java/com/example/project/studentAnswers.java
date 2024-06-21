package com.example.project;

public class studentAnswers {
    private final int studentID;
    private final int questionID;

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    private  String answer;
    private  boolean isCorrect;

    // Constructors
    public studentAnswers(int studentID, int questionID, String answer, boolean isCorrect) {
        this.studentID = studentID;
        this.questionID = questionID;
        this.answer = answer;
        this.isCorrect = isCorrect;
    }
    public int getStudentID() {
        return studentID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }


}
