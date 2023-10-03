package dev.capstone.asu.Capstone.Project.Admin.System.Students;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Arrays;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private int asuriteID;
    private long[] projectPreferences;
    private long assignedProject;

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public int getAsuriteID() {
        return asuriteID;
    }

    public long[] getProjectPreferences() {
        return projectPreferences;
    }

    public long getAssignedProject() {
        return assignedProject;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAsuriteID(int asuriteID) {
        this.asuriteID = asuriteID;
    }

    public void setProjectPreferences(long[] projectPreferences) {
        this.projectPreferences = projectPreferences;
    }

    public void setAssignedProject(long assignedProject) {
        this.assignedProject = assignedProject;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", asuriteID=" + asuriteID +
                ", projectPreferences=" + Arrays.toString(projectPreferences) +
                ", assignedProject=" + assignedProject +
                '}';
    }
}
