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