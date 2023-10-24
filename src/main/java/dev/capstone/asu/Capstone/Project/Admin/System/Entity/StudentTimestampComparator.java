package dev.capstone.asu.Capstone.Project.Admin.System.Entity;

import java.util.Comparator;
import java.util.Objects;

public class StudentTimestampComparator implements Comparator<Student> {

    @Override
    public int compare(Student student1, Student student2)
    {
        if (Objects.isNull(student1.getSignupTimestamp()))
        {
            if (Objects.isNull(student2.getSignupTimestamp())) return 0;
            return 1;
        }
        else if (Objects.isNull(student2.getSignupTimestamp())) return -1;
        return student1.getSignupTimestamp().compareTo(student2.getSignupTimestamp());
    }

}
