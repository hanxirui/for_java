package com.solaris.rxjava;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hanxirui on 2017/6/15.
 */

public class Student {
    private String name;
    private List<com.solaris.rxjava.Course> courses;

    public Student(String name,Course... courses){
        this.name = name;
        this.courses = Arrays.asList(courses);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
