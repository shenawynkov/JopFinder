package com.example.shenawynkov.jopfinder.model;

/**
 * Created by Shenawynkov on 8/22/2017.
 */

public class Job {
    private String title;
    private int  salary_min;
    private int  salary_max;
    private String career_level;
    private String description;


    Job()
{

}
    public Job(String title, int salary_min, int salary_max, String career_level, String description) {
        this.title = title;
        this.salary_min = salary_min;
        this.salary_max = salary_max;
        this.career_level = career_level;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSalary_min() {
        return salary_min;
    }

    public void setSalary_min(int salary_min) {
        this.salary_min = salary_min;
    }

    public int getSalary_max() {
        return salary_max;
    }

    public void setSalary_max(int salary_max) {
        this.salary_max = salary_max;
    }

    public String getCareer_level() {
        return career_level;
    }

    public void setCareer_level(String career_level) {
        this.career_level = career_level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
