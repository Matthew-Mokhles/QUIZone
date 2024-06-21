package com.example.project;

public class Registration {
    private final int studentid;
    private final String courseCode;
    private final String courseName;
    private final String StudentName;

    public String getStudentName() {
        return StudentName;
    }

    public int getStudentid() {
        return studentid;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public Registration(int studentid, String courseCode, String courseName, String studentName) {
        this.studentid = studentid;
        this.courseCode = courseCode;
        this.courseName = courseName;
        StudentName = studentName;
    }
}
