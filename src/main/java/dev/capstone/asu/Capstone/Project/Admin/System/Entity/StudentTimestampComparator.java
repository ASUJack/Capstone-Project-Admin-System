package dev.capstone.asu.Capstone.Project.Admin.System.Entity;

import java.util.Comparator;

public class StudentTimestampComparator implements Comparator<Student> {

    @Override
    public int compare(Student student1, Student student2)
    {
        return student1.getSignupTimestamp().compareTo(student2.getSignupTimestamp());
    }

}
