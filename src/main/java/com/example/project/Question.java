package com.example.project;


public class Question
{
        private final String Question;

        private final String ans1;
        private final String  ans2;

        private final String  ans3 ;

        private final String  ans4;

        private final String  correct;

        private final String  cat;
        private int questionId;


    public Question(String question, String ans1, String ans2, String ans3, String ans4, String correct, String cat) {
        Question = question;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.ans4 = ans4;
        this.correct = correct;
        this.cat = cat;
    }
    public String getQuestion() {
        return Question;
    }
    public String getAns1() {
        return ans1;
    }
    public String getAns2() {
        return ans2;
    }
    public String getAns3() {
        return ans3;
    }
    public String getAns4() {
        return ans4;
    }
    public String getCorrect() {
        return correct;
    }
    public String getCat() {
        return cat;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
}








