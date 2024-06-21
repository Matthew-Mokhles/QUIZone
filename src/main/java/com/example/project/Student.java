package com.example.project;

public class Student {
    private final int id;
    private final String name;
    private String pincode;
    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPincode() {
        return pincode;
    }

    public Student(int id, String name, String pincode) {
        this.id = id;
        this.name = name;
        this.pincode = pincode;
    }
}
